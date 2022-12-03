package com.shashifreeze.appopen.view.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.apputils.Network
import com.shashifreeze.appopen.apputils.TextChangeWatcher
import com.shashifreeze.appopen.apputils.extensions.*
import com.shashifreeze.appopen.apputils.getInterstitialAdUnitID
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.network.response.UrlResponse
import com.shashifreeze.appopen.preference.MyPreferences
import com.shashifreeze.appopen.view.ui.history.HistoryViewModel
import com.shashifreeze.appopen.apputils.extensions.copyToClipboard
import com.shashifreeze.appopen.databinding.FragmentHomeBinding
import com.shashifreeze.appopen.view.ui.contact.ContactActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var mInterstitialAd: InterstitialAd? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private var solveCaptchaDialog: Dialog? = null
    private val historyViewModel by viewModels<HistoryViewModel>()
    private var ratingDialog: Dialog? = null

    companion object {
        const val TAG = "ResearchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.bind(view)

        //setup RV
        setUpViews()

        //listeners
        listener()

        //observe
        observe()

        if (Network.checkConnectivity(requireContext())) //has internet connection
        {
            showOnceADay()
        }
    }

    /**
     * Loads and shows interstitial ads
     */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),
            getInterstitialAdUnitID(), adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                mInterstitialAd = null
                                Log.d(TAG, "Ad was shown.")
                                lifecycleScope.launch {
                                    MyPreferences.setAdSeenDate(
                                        requireContext(),
                                        requireContext().getCurrentTime("dd-MM-yyyy")
                                    )
                                }
                            }
                        }
                }
            })
    }

    private fun solveCaptchaDialog(longUrl: String) {
        solveCaptchaDialog = Dialog(requireContext())
        solveCaptchaDialog?.let { dialog ->
            dialog.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.solve_captcha_dialog)
            val tv1 = dialog.findViewById(R.id.input1ET) as TextView
            val tv2 = dialog.findViewById(R.id.shortUrlTV) as TextView
            val resultET = dialog.findViewById(R.id.resultET) as EditText
            //generate two random number and set to tv1 and tv2
            val num1 = Random(SystemClock.uptimeMillis()).nextInt(20)
            val num2 = Random(SystemClock.uptimeMillis()).nextInt(10)
            val correctResult = num1 + num2
            tv1.text = num1.toString()
            tv2.text = num2.toString()

            val closeIV = dialog.findViewById(R.id.closeIV) as ImageView
            val submitBtn = dialog.findViewById(R.id.submitBtn) as AppCompatButton
            submitBtn.setOnClickListener {
                if (!resultET.validateEmpty("") && correctResult.toString() == resultET.text.toString()) {
                    //validation passed proceed for link creation
                    requireActivity().hideKeyboard()
                    viewModel.createShortLink(longUrl)
                } else {
                    resultET.requestFocus()
                    resultET.error = "Please solve this"
                }
            }
            closeIV.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }

    }

    private fun urlCreatedDialog(shortUrl: String) {

        val dialog = Dialog(requireContext())
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.url_created_dialog)

        val tv1 = dialog.findViewById(R.id.shortUrlTV) as TextView
        tv1.text = shortUrl
        val closeIV = dialog.findViewById(R.id.closeIV) as ImageView
        val copyBtn = dialog.findViewById(R.id.copyBtn) as AppCompatButton
        val shareBtn = dialog.findViewById(R.id.shareBtn) as AppCompatButton
        copyBtn.setOnClickListener {
            shortUrl.copyToClipboard(requireContext())
        }
        shareBtn.setOnClickListener {
            requireContext().shareText(shortUrl)
        }
        closeIV.setOnClickListener {
            dialog.dismiss()
            ratingDialog()
        }
        dialog.show()
    }

    /**
     * Checks whether ad is seen today or not, if not then show
     */
    private fun showOnceADay() {
        lifecycleScope.launchWhenCreated {
            val lastAdSeenDate = MyPreferences.getAdSeenDate(requireContext()).first()
            Log.d(TAG, "Last seen date:$lastAdSeenDate")
            if (requireContext().getCurrentTime("dd-MM-yyyy") != lastAdSeenDate) {
                //init and load interstitial
                loadInterstitialAd()
            }
        }
    }

    private fun setUpViews() {

    }

    private fun observe() {
        viewModel.createShortLinkLiveData.observe(viewLifecycleOwner) {
            if (it is NetworkResource.Loading) {
                //close captcha dialog
                solveCaptchaDialog?.dismiss()
                binding.progressBar.visible(true)
            } else {
                binding.progressBar.visible(false)
            }

            if (it is NetworkResource.Success<*>) {
                //@todo handle response
                val urlData = (it.value as UrlResponse).urlData
                if (urlData.errorMessage != null) {
                    requireContext().showInfoDialog(title = "Error", urlData.errorMessage)
                } else {
                    val shortLink = "https://appopen.me/${urlData.type}/${urlData.shortCode}"
                    //show short link dialog
                    urlCreatedDialog(shortLink)
                    //Add url to local history
                    lifecycleScope.launch {
                        historyViewModel.saveHistory(
                            longUrl = binding.longUrlET.text.toString(),
                            shortUrl = shortLink
                        )
                    }
                }

            } else if (it is NetworkResource.Failure) {
                Log.d(TAG, "${it.isNetworkError}, ${it.errorBody}, ${it.message}")
                requireContext().showInfoDialog(title = "Error", "Something went wrong!")
            }
        }
    }

    private fun ratingDialog() {
        var alreadyRated = false
        runBlocking {
            alreadyRated = MyPreferences.hasRated(requireContext()).first()
        }

        if (!alreadyRated) {
            ratingDialog = requireContext().showRatingDialog(rateBtnCallback = {
                runBlocking {
                    MyPreferences.setRated(requireContext(), true)
                }
                ratingDialog?.dismiss()
                requireActivity().startNewActivity(ContactActivity::class.java)

            },
                exitCallback = {
                    ratingDialog?.dismiss()
                },
                ratingBarCallback = {
                    runBlocking {
                        MyPreferences.setRated(requireContext(), true)
                    }
                    //close dialog
                    ratingDialog?.dismiss()
                    //Open play store
                    requireContext().openAppOnPlayStore()
                })
        }
    }

    private fun listener() {

        binding.generateBtn.setOnClickListener {
            requireActivity().hideKeyboard()
            val longUrl = binding.longUrlET.text.trim().toString()
            if (URLUtil.isHttpsUrl(longUrl)) {
                solveCaptchaDialog(longUrl)
            } else {
                binding.longUrlET.requestFocus()
                binding.longUrlET.error = "Not a valid Url"
            }
        }

        binding.longUrlET.addTextChangedListener(object : TextChangeWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //enable the button if any text is entered
                binding.generateBtn.enable(
                    s != null
                            && s.isNotEmpty()
                            && s.isNotBlank()
                )
            }
        })

        binding.pasteBtn.setOnClickListener {
            binding.longUrlET.pasteCopiedUrl()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


