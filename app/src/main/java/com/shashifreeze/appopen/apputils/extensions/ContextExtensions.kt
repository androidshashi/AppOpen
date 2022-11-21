package com.shashifreeze.appopen.apputils.extensions

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.shashifreeze.appopen.BuildConfig
import com.shashifreeze.appopen.R
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File contains Context extension functions
 */



/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File contains Context extension functions
 */
/**
 * Shows alert dialog
 */
fun Context.showAlertDialog(
    title: String? = null,
    message: String,
    posBtnText: String? = null,
    negBtnText: String? = null,
    showNegBtn: Boolean = true,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title ?: getString(R.string.alert))
        it.setMessage(message)
        it.setPositiveButton(posBtnText ?: getString(R.string.yes)) { _, _ ->
            callback()
        }
        if (showNegBtn) {
            it.setNegativeButton(negBtnText ?: getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        }

    }.create().show()
}

/**
 * Shows alert dialog
 */
fun Context.adInfoDialog(
    title: String? = null,
    message: String,
    posBtnText: String? = null,
    negBtnText: String? = null,
    showNegBtn: Boolean = true,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title ?: getString(R.string.alert))
        it.setMessage(message)
        it.setPositiveButton(posBtnText ?: getString(R.string.yes)) { dialog, _ ->
            callback()
            dialog.dismiss()
        }
        if (showNegBtn) {
            it.setNegativeButton(negBtnText ?: getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        }

    }.create().show()
}

/**shows toast to the context
 * @param: text: String
 * @return: Unit
 * @author: Shashi
 */

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.notificationPermissionEnabled(): Boolean {
    val pkgName = packageName
    val flat = Settings.Secure.getString(
        contentResolver,
        "enabled_notification_listeners"
    )
    if (!TextUtils.isEmpty(flat)) {
        val names = flat.split(":").toTypedArray()
        for (name in names) {
            val cn = ComponentName.unflattenFromString(name)
            if (cn != null) {
                if (TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
    }
    return false
}

fun Context.openNotificationPermissionSettings() {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } else {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }
    } catch (e: ActivityNotFoundException) {
        showToast("Your Device Does Not Support this service.")
    }
}


fun Context.getCurrentTime(format: String = "dd.MM.yyyy. HH:mm a"): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(format)
        current.format(formatter)

    } else {
        val date = Date()
        val formatter = SimpleDateFormat(format, Locale.US)
        formatter.format(date)
    }
}


/**
 * Share MULTIPLE the file
 */

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.shareApp() {
    val text =
        "100% Free Keyword Research Tool:\nGet Most searched keywords on Google Related to your keyword. Our web version tool is: https://earningdesire.com/google-keyword-suggestion-tool/ \nDownload the app\nhttps://play.google.com/store/apps/details?id=$packageName"
    val intent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(intent, null)
    startActivity(shareIntent)
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


/**
 * Shows alert dialog
 */
fun Context.showInfoDialog(
    title: String? = null,
    message: String,
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title ?: "Info")
        it.setMessage(message)
        it.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

    }.create().show()
}


/**
 * Function to init and add test device to the configuration request
 */
fun Context.initAndAddTestDevices() {
    //INIT MOBILE ADS
    MobileAds.initialize(this)
    //CONFIGURE TEST DEVICE
    if (BuildConfig.DEBUG) {
        val testDeviceIds: MutableList<String> = java.util.ArrayList()
        testDeviceIds.add("6A21E8F0FC95071FE5DD44AACE3EF9B7")
        testDeviceIds.add("299FA257E48144EFB8E3FD79F88E5B17")
        testDeviceIds.add("F19F8A6D523D7F550A2A39677B72FB33")
        testDeviceIds.add("EFACA5EFDE21A04D73AE7F51F9C4BCE4")
        testDeviceIds.add("FC1E9D7DD0644832509E1D039125D962")
        testDeviceIds.add("ED7693BF7698A6EAA8560CEDF261A442")

        val configuration =
            RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
    }
}

/**
 * Share MULTIPLE the file
 */
fun Context.shareFiles(list: ArrayList<File>) {

    val uriList: ArrayList<Uri> = arrayListOf()
    //get uri from Custom files
    for (f in list) {

        val path: Uri? = FileProvider.getUriForFile(this, "whatsapp.status.saver", f)
        path?.let { uriList.add(it) }

    }

    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND_MULTIPLE
    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    shareIntent.type = "*/*"
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        "Shared By WhatsAll\n https://play.google.com/store/apps/details?id=${this.packageName}"
    )
    try {

        this.startActivity(Intent.createChooser(shareIntent, "Share To"))
    } catch (e: Exception) {
        showToast("Oops! Something went wrong.")

    }
}
