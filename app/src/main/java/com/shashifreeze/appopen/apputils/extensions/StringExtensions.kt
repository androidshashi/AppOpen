package com.vendeor.util.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.shashifreeze.appopen.apputils.extensions.showToast
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File String Context extension functions
 */


/**
 * Copy string to clipboard
 */
fun String.copyToClipboard(context: Context) {
    val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("KRT", this)
    clipBoard.setPrimaryClip(clip)
    context.showToast("Copied Successfully!")
}




