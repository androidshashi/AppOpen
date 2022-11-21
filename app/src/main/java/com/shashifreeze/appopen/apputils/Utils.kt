package com.shashifreeze.appopen.apputils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.shashifreeze.appopen.BuildConfig
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.apputils.Constants.ADMOB_BANNER_LIVE_UNIT_ID
import com.shashifreeze.appopen.apputils.Constants.ADMOB_BANNER_TEST_UNIT_ID
import com.shashifreeze.appopen.apputils.Constants.ADMOB_INTERSTITIAL_LIVE_UNIT_ID
import com.shashifreeze.appopen.apputils.Constants.ADMOB_INTERSTITIAL_TEST_UNIT_ID
import com.shashifreeze.appopen.apputils.extensions.showToast
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
@Author: Shashi
@Date: 16-03-2021
@Description: Contains all common functions and extension functions for this specific project*/

const val TAG = "UtilsTAG"


//shows and hide progressbar
fun ProgressBar.hide(b: Boolean) {

    if (b) {
        this.visibility = View.INVISIBLE
    } else {
        this.visibility = View.VISIBLE
    }
}

/**
 * shows and hides all the munu items
 */
fun Menu.showAllItems(mVisible:Boolean = false)
{
    for ( m in this)
    {
        m.isVisible = mVisible
    }
}


/**
 * Add divider to recyclerview
 * @param: (orientation :Int = LinearLayoutManager.VERTICAL)
 * @return: Unit
 * @author: Shashi
 * */
fun RecyclerView.addDivider() {

    if (layoutManager !is LinearLayoutManager)
        return
    addItemDecoration(
        DividerItemDecoration(
            context,
            (layoutManager as LinearLayoutManager).orientation
        )
    )
}

/**
 * Format timestamp to formatted date
 */
fun Long.toTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("HH:mm a yyyy-MM-dd ", Locale.ENGLISH)
    return format.format(date)
}

fun Int.formatStringSize(): String {
    val hrSize: String
    val b = this.toDouble()
    val k = this / 1024.0
    val m = this / 1024.0 / 1024.0
    val g = this / 1024.0 / 1024.0 / 1024.0
    val t = this / 1024.0 / 1024.0 / 1024.0 / 1024.0
    val dec = DecimalFormat("0.00")
    hrSize = when {
        t > 1 -> {
            dec.format(t) + " TB"
        }
        g > 1 -> {
            dec.format(g) + " GB"
        }
        m > 1 -> {
            dec.format(m) + " MB"
        }
        k > 1 -> {
            dec.format(k) + " KB"
        }
        else -> {
            dec.format(b) + " Bytes"
        }
    }
    return hrSize
}

/**
 * Delete all the files from storage those are in array list
 */

/**
 * Selects the tab from its tab name
 */


fun aboveSDK29()= Build.VERSION.SDK_INT > 29


/**
 * Sets ad unit id to banner ads
 */
fun AdView.setBannerAdUnitID()
{
    adUnitId = if (BuildConfig.DEBUG) ADMOB_BANNER_TEST_UNIT_ID else ADMOB_BANNER_LIVE_UNIT_ID
}

fun getInterstitialAdUnitID():String = if (BuildConfig.DEBUG) ADMOB_INTERSTITIAL_TEST_UNIT_ID else ADMOB_INTERSTITIAL_LIVE_UNIT_ID


/**
 * Created by Shashi on 07/11/21
 * @return : String
 * @params : context
 * @modified by : NA
 * @modified on : NA
 * @description : Convert long to time ago
 */

private const val SECOND_MILLIS: Int = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

/**
 * launch Custom chrome tab
 */

fun Context.openCustomChromeTab(url:String)
{

    // initializing object for custom chrome tabs.
    // initializing object for custom chrome tabs.
    val customIntent = CustomTabsIntent.Builder()

    // below line is setting toolbar color
    // for our custom chrome tab.
    customIntent.setToolbarColor(ContextCompat.getColor(this, R.color.primary))
    val customTabsIntent = customIntent.build()
    // package name is the default package
    // for our custom chrome tab
    // package name is the default package
    // for our custom chrome tab
    val packageName = "com.android.chrome"
    try{
        // we are checking if the package name is not null
        // if package name is not null then we are calling
        // that custom chrome tab with intent by passing its
        // package name.
        customTabsIntent.intent.setPackage(packageName)

        // in that custom tab intent we are passing
        // our url which we have to browse.
        customTabsIntent.launchUrl( this, Uri.parse(url))
    } catch (e:java.lang.Exception) {
        // if the custom tabs fails to load then we are simply
        // redirecting our user to users device default browser.
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}


/**
 * Open play store with our app
 */
fun Context.openAppOnPlayStore(package_name: String = packageName) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$package_name")))
    } catch (e: java.lang.Exception) {
        showToast("Unable to open play store")
    }
}

/**
 * Open play store with developer id or page id
 */
fun Context.moreApps(developerId: String? = null, devPageId: String? = null) {
    val pageUri =
        if (devPageId != null) "https://play.google.com/store/apps/dev?id=$devPageId" else "https://play.google.com/store/apps/developer?id=$developerId"
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(pageUri)
            )
        )
    } catch (e: java.lang.Exception) {
        showToast("Unable to open play store")
    }
}

fun Context.openSite(siteUrl: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl)))
    } catch (e: java.lang.Exception) {
        showToast("Unable to open the site")
    }
}









