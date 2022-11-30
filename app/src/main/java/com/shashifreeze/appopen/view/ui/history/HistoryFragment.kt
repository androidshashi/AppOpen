package com.shashifreeze.appopen.view.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.apputils.extensions.showToast
import com.shashifreeze.appopen.apputils.extensions.visible
import com.shashifreeze.appopen.database.entity.HistoryData
import com.shashifreeze.appopen.databinding.FragmentHistoryBinding
import com.shashifreeze.appopen.apputils.extensions.copyToClipboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history), HistoryAdapter.HistoryClickListener {

    private val viewModel by viewModels<HistoryViewModel>()

    private var _binding: FragmentHistoryBinding? = null

    private var historyAdapter = HistoryAdapter(this, arrayListOf())

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var historyList: ArrayList<HistoryData> = arrayListOf()

    //Image count
    private var historyCount: Int = 0

    companion object {
        const val TAG = "HistoryFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHistoryBinding.bind(view)

        // Setup views
        setUpViews()

        // Fetch data
        fetchData()

        //click listeners
        clickListeners()
    }

    private fun clickListeners() {
        binding.deleteAllBtn.setOnClickListener {
            if (historyCount <= 0) {
                showToast("No History Found")
            } else
                viewModel.deleteAllHistory()
        }
    }

    private fun setUpViews() {
        historyAdapter = HistoryAdapter(this, arrayListOf())
        binding.historyRv.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRv.adapter = historyAdapter
        binding.deleteAllBtn.visible(historyCount>0)
    }

    private fun fetchData() {

        lifecycleScope.launchWhenCreated {
            viewModel.getHistory().observe(viewLifecycleOwner) { list ->
                //binding.alert.visible(list.isEmpty())
                //clear list
                historyList.clear()
                //add new items
                historyList.addAll(list.reversed())
                //total count
                historyCount = historyList.size
                //refresh count
                requireActivity().invalidateOptionsMenu()
                //refresh adapter
                historyAdapter.update(list.reversed())
                //
                binding.deleteAllBtn.visible(historyCount>0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onHistoryCopyClick(data: HistoryData) {
        data.shortUrl.copyToClipboard(requireContext())
    }

    override fun onHistoryDeleteClick(data: HistoryData) {
        viewModel.deleteHistory(data.id)
    }
}