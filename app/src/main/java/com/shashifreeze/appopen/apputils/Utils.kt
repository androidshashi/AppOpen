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
import com.shashifreeze.appopen.network.api.UrlDataApi
import com.shashifreeze.appopen.network.response.UrlData
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


fun UrlData.getUrlTypeInfo():String
{
    return when (this.type) {
        UrlDataApi.URL_TYPE_YT -> {
            "YouTube"
        }
        UrlDataApi.URL_TYPE_IG -> {
            "Instagram"
        }
        else -> UrlDataApi.URL_TYPE_WEB
    }
}







