package com.shashifreeze.appopen.view.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shashifreeze.appopen.database.entity.HistoryData
import com.shashifreeze.appopen.databinding.ItemHistoryLayoutBinding

/**
 *@author = Shashi
 *@date = 04/09/21
 *@description = This class handles
 */
class HistoryAdapter(
    private val listener: HistoryClickListener,
    private val historyList: ArrayList<HistoryData>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    companion object{
        const val TAG ="HistoryAdapter"
    }

    interface HistoryClickListener {
        fun onHistoryCopyClick(data: HistoryData)
        fun onHistoryDeleteClick(data: HistoryData)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list:List<HistoryData>)
    {
        historyList.clear()
        historyList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            com.shashifreeze.appopen.databinding.ItemHistoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount() = historyList.size

    inner class HistoryViewHolder(private val binding: ItemHistoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HistoryData) {
            binding.longUrl.text = data.longUrl
            binding.shortUrl.text = data.shortUrl
            binding.copyIv.setOnClickListener {
                listener.onHistoryCopyClick(data)
            }
            binding.deleteIv.setOnClickListener {
                listener.onHistoryDeleteClick(data)
            }
        }
    }
}