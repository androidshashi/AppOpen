package com.shashifreeze.appopen.view.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
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
import com.shashifreeze.appopen.apputils.getUrlTypeInfo
import com.shashifreeze.appopen.databinding.FragmentSearchBinding
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.network.response.UrlResponse
import com.shashifreeze.appopen.preference.MyPreferences
import com.shashifreeze.appopen.view.ui.history.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var mInterstitialAd: InterstitialAd? = null
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchViewModel>()
    private val historyViewModel by viewModels<HistoryViewModel>()

    companion object {
        const val TAG = "ResearchFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        _binding = FragmentSearchBinding.bind(view)

        //listeners
        listener()

        //observe
        observe()

        if (Network.checkConnectivity(requireContext())) //has internet connection
        {
            //showOnceADay()
        }
    }

    /**
     * Loads and shows interstitial ads
     */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),
            getInterstitialAdUnitID(),
            adRequest,
            object : InterstitialAdLoadCallback() {
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

    private fun observe() {
        viewModel.shortCodeInfoLiveData.observe(viewLifecycleOwner) {

            if (it is NetworkResource.Loading) {
                binding.progressBar.visible(true)
                binding.infoLayout.visible(false)
            } else {
                binding.progressBar.visible(false)
            }

            if (it is NetworkResource.Success<*>) {
                val urlData = (it.value as UrlResponse).urlData
                if (urlData.errorMessage != null) {
                    requireContext().showInfoDialog(title = "Error", urlData.errorMessage)
                } else {
                    val shortLink = "https://appopen.me/${urlData.type}/${urlData.shortCode}"
                    //@todo show short link info
                    binding.apply {
                        hitsValue.text = urlData.hits.toString()
                        longUrlValue.text = urlData.longUrl
                        shortUrlValue.text = shortLink
                        typeValue.text = urlData.getUrlTypeInfo()
                    }
                    binding.infoLayout.visible(true)
                    //Add url to local history
                    lifecycleScope.launch {
                        historyViewModel.saveHistory(
                            longUrl =urlData.longUrl, shortUrl = shortLink
                        )
                    }
                }

            } else if (it is NetworkResource.Failure) {
                Log.d(TAG, "${it.isNetworkError}, ${it.errorBody}, ${it.message}")
                binding.infoLayout.visible(false)
                requireContext().showInfoDialog(title = "Error", "Something went wrong!")
            }
        }

    }

    private fun listener() {

        binding.searchBtn.setOnClickListener {
            requireActivity().hideKeyboard()

            val shortUrlType = binding.shortUrlET.getUrlType()
            val shortUrl = binding.shortUrlET.text.trim().toString()
            val shortCode = binding.shortUrlET.getShortCode()
            if (shortUrlType == null || shortCode == null) {
                binding.shortUrlET.requestFocus()
                binding.shortUrlET.error = requireContext().getString(R.string.not_a_valid_url)
                return@setOnClickListener
            }
            if (URLUtil.isHttpsUrl(shortUrl)) {
                viewModel.getShortCodeInfo(shortCode = shortCode, type = shortUrlType)
            } else {
                binding.shortUrlET.requestFocus()
                binding.shortUrlET.error = requireContext().getString(R.string.not_a_valid_url)
            }
        }

        binding.shortUrlET.addTextChangedListener(object : TextChangeWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //enable the button if any text is entered
                if (s != null && s.isNotEmpty() && s.isNotBlank() && (s.startsWith(prefix = "https://appopen.me/yt/") || s.startsWith(
                        prefix = "https://appopen.me/web/"
                    ) || s.startsWith(prefix = "https://appopen.me/ig/") || s.startsWith(prefix = "https://www.appopen.me/ig/") || s.startsWith(
                        prefix = "https://www.appopen.me/web/"
                    ) || s.startsWith(prefix = "https://www.appopen.me/yt/"))
                ){
                   val notEnable =  s.contentEquals("https://appopen.me/yt/") ||
                           s.contentEquals("https://appopen.me/web/") ||
                           s.contentEquals("https://appopen.me/ig/") ||
                           s.contentEquals("https://www.appopen.me/yt/") ||
                           s.contentEquals( "https://www.appopen.me/web/") ||
                           s.contentEquals("https://www.appopen.me/ig/")

                    binding.searchBtn.enable(!notEnable)
                }else{
                    binding.searchBtn.enable(false)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


