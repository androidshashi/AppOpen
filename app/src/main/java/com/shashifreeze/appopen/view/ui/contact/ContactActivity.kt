package com.shashifreeze.appopen.view.ui.contact

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.apputils.Constants
import com.shashifreeze.appopen.apputils.extensions.openCustomChromeTab
import com.shashifreeze.appopen.apputils.extensions.showToast
import com.shashifreeze.appopen.apputils.extensions.validateEmpty
import com.shashifreeze.appopen.databinding.ActivityContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactActivity : AppCompatActivity() {
    private var _binding: ActivityContactBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityContactBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        binding.apply {
            sendMessageBtn.setOnClickListener {
                sendEmail()
            }

            launchBtn.setOnClickListener {
                openCustomChromeTab(Constants.APP_OPEN_URL)
            }
        }
    }

    private fun sendEmail() {

        if (binding.yourNameET.validateEmpty(getString(R.string.your_name)))
        {
            return
        }

        if (binding.messageET.validateEmpty(getString(R.string.message)))
        {
            return
        }


        try{
            val intent =  Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,  arrayOf(getString(R.string.contact_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, "${binding.yourNameET.text}-${getString(R.string.app_name)}")
            intent.putExtra(Intent.EXTRA_TEXT, binding.messageET.text)
            intent.type = "text/html"
            intent.setPackage("com.google.android.gm");
            startActivity(intent);

        } catch (e:Exception) {  // allow user to choose a different app to send email with
            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            selectorIntent.data = Uri.parse("mailto:")

            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "${binding.yourNameET.text}-${getString(R.string.app_name)}")
            emailIntent.putExtra(Intent.EXTRA_TEXT, binding.messageET.text)
            emailIntent.selector = selectorIntent

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                showToast("There are no email clients installed.")
            }
        }
    }
}