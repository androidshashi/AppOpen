package com.shashifreeze.appopen.apputils

import android.text.Editable
import android.text.TextWatcher

/**
 *@author = Shashi
 *@date = 01/09/21
 *@description = This class handles
 */
abstract class TextChangeWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }
}