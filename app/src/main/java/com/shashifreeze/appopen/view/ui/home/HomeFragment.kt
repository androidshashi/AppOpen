package com.shashifreeze.appopen.view.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.shashifreeze.appopen.databinding.FragmentHomeBinding
import com.shashifreeze.appopen.network.NetworkResource
import com.shashifreeze.appopen.preference.MyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
            } else {
                //fire in app review dialog

            }
        }
    }

    private fun setUpViews() {

    }

    private fun observe() {
        viewModel.keywordResponseLiveData.observe(viewLifecycleOwner) {

            if (it is NetworkResource.Success<*>) {
                //@todo handle response
            } else if (it is NetworkResource.Failure) {
                Log.d(TAG, "${it.isNetworkError}, ${it.errorBody}, ${it.message}")
                //hide options
                binding.queryInputLayout.helperText = "Something went wrong!"
            }
        }
    }


    private fun listener() {

        binding.searchBtn.setOnClickListener {
            requireActivity().hideKeyboard()
            viewModel.getKeywordData(binding.keywordEt.text.trim().toString(), "ds")
        }

        binding.keywordEt.addTextChangedListener(object : TextChangeWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //enable the button if any text is entered
                binding.searchBtn.enable(
                    s != null
                            && s.isNotEmpty()
                            && s.isNotBlank()
                )
            }
        })

        binding.pasteBtn.setOnClickListener {
            binding.keywordEt.pasteCopiedText()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


