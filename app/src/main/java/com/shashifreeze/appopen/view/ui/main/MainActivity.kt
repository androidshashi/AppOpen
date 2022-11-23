package com.shashifreeze.appopen.view.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.navigation.NavigationView
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.apputils.*
import com.shashifreeze.appopen.apputils.Constants.DEVELOPER_ID
import com.shashifreeze.appopen.apputils.Constants.KEYWORD_TOOL_GOOGLE_URL
import com.shashifreeze.appopen.apputils.Constants.KEYWORD_TOOL_YT_URL
import com.shashifreeze.appopen.apputils.Constants.PRIVACY_POLICY_URL
import com.shashifreeze.appopen.apputils.Constants.TNC_URL
import com.shashifreeze.appopen.apputils.extensions.*
import com.shashifreeze.appopen.apputils.internet.ConnectionLiveData
import com.shashifreeze.appopen.databinding.ActivityMainBinding
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.view.ui.contact.AboutActivity
import com.shashifreeze.appopen.view.ui.contact.ContactActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /**Binding*/
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var connectionLiveData: ConnectionLiveData? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel by viewModels<MainViewModel>()
    private var mAdView: AdView? = null
    private var productImageView: ImageView? = null
    private var productTitle: TextView? = null
    private var installNow: TextView? = null
    private var headerView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //init ad sdk
        initAndAddTestDevices()

        //init banner ad
        showBannerAd()

        //setup navigation drawer
        setUpNavigationView()

        //stop progress bar
        connectionLiveData = ConnectionLiveData(this)

        //getAppData
        viewModel.getAppData(applicationContext.packageName)

        listeners()

        observe()
    }

    private fun observe() {
        connectionLiveData?.observe(this) {
            binding.appBarMain.connectionTV.visible(!it)
        }
    }

    private fun listeners() {
        binding.appBarMain.mainContentLayout.menuIV.setOnClickListener {
            handleDrawer()
        }

        binding.appBarMain.mainContentLayout.historyIV.setOnClickListener {
            navController.navigate(R.id.nav_fragment_history)
        }

        binding.appBarMain.mainContentLayout.logoIV.setOnClickListener {
            navController.navigate(R.id.nav_fragment_home)
        }

        viewModel.appDataLiveData.observe(this) { it1 ->

            if (it1 is NetworkResource.Success) {
                if (it1.value.appError == null) {
                    productImageView?.loadImage(it1.value.appData.appIconUrl)
                    productTitle?.text = it1.value.appData.appTitle
                    headerView?.setOnClickListener {
                        openAppOnPlayStore(it1.value.appData.appPackageName)
                    }
                } else {
                    productTitle?.text = getString(R.string.app_name)
                    productImageView?.setImageResource(R.drawable.logo)
                    installNow?.visible(false)
                    headerView?.setOnClickListener(null)
                }
            } else if (it1 is NetworkResource.Failure) {
                productImageView?.setImageResource(R.drawable.logo)
                productTitle?.text = getString(R.string.app_name)
                installNow?.visible(false)
                headerView?.setOnClickListener(null)
            }
        }
    }

    private fun showBannerAd() {
        mAdView = AdView(this)
        mAdView?.setAdSize(AdSize.BANNER)
        mAdView!!.setBannerAdUnitID()
        val adRequest = AdRequest.Builder()
            .build()
        if (!(this.isDestroyed && !this.isFinishing)) {
            mAdView!!.loadAd(adRequest)
            binding.appBarMain.mainContentLayout.bannerContainerLL.addView(mAdView)
        }
    }

    private fun setUpNavigationView() {
        // nav host fragment
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        if (navHostFragment != null) {
            navController = navHostFragment.navController
            // Setup NavigationUI here
        }

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_fragment_home,
                R.id.nav_fragment_history,
                R.id.nav_fragment_settings,
            ), drawerLayout
        )

        // to make the Navigation drawer icon always appear on the action bar
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //inflating the header view
        headerView = binding.navView.getHeaderView(0)
        // val profileImage = headerView.findViewById(R.id.navProfileCIV) as CircleImageView
        productImageView = headerView?.findViewById<ImageView>(R.id.imageView)
        productTitle = headerView?.findViewById<TextView>(R.id.title)
        installNow = headerView?.findViewById<TextView>(R.id.installNow)

        binding.navView.setNavigationItemSelectedListener { dest ->

            when (dest.itemId) {

                R.id.nav_share_this_app -> {
                    shareApp()
                }

                R.id.nav_rate_us -> {
                    openAppOnPlayStore()
                }
                R.id.nav_more_apps -> {
                    moreApps(DEVELOPER_ID)
                }

                R.id.nav_privacy_policy -> {
                    openSite(PRIVACY_POLICY_URL)
                }

                R.id.nav_tnc -> {
                    openSite(TNC_URL)
                }

                R.id.nav_your_feedback -> {
                   startNewActivity(ContactActivity::class.java,false)
                }

                R.id.nav_keyword_tool_google -> {
                    openCustomChromeTab(KEYWORD_TOOL_GOOGLE_URL)
                }

                R.id.nav_keyword_tool_yt -> {
                    openCustomChromeTab(KEYWORD_TOOL_YT_URL)
                }

                else ->{
                    navController.navigate(dest.itemId)
                }

            }
            //close drawer
            binding.drawerLayout.closeDrawers()

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return super.onOptionsItemSelected(item)
        } else {
            when (item.itemId) {

                R.id.action_feedback -> {
                    startNewActivity(ContactActivity::class.java,false)
                    return true
                }

                R.id.action_about -> {
                    startNewActivity(AboutActivity::class.java)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
//        val menuItem = menu.findItem(R.id.action_game)
//        val actionView = menuItem.actionView
//        actionView.setOnClickListener { onOptionsItemSelected(menuItem) }
        return true
    }

    override fun onBackPressed() {

        showAlertDialog(
            title = "Want to exit?",
            message = "Thanks for using\uD83D\uDE0A",
            callback = { finish() }
        )
    }

    fun handleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}