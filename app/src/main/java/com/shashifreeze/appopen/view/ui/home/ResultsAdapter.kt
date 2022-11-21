package com.shashifreeze.appopen.view.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shashifreeze.appopen.data.KeywordModel
import com.shashifreeze.appopen.databinding.ItemKeywordResultRowLayoutBinding

/**
 *@author = Shashi
 *@date = 04/09/21
 *@description = This class handles
 */
class ResultsAdapter(private val listener:ResultsListener,  var keywordList:ArrayList<KeywordModel>) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    interface ResultsListener{
        fun onCheckedChanged(keywordList: ArrayList<KeywordModel>,keyword:String)
    }

    companion object{
        const val TAG="ResultsAdapter"
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(keywords:ArrayList<KeywordModel>){
        keywordList=keywords
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemKeywordResultRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(keywordList[position])
    }

    override fun getItemCount() = keywordList.size

     inner class ResultViewHolder(private val binding: ItemKeywordResultRowLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(data:KeywordModel){
           binding.keywordTV.text = data.keyword
            binding.checkbox.isChecked=data.checked
            binding.checkbox.setOnCheckedChangeListener { _, checked ->
                keywordList[adapterPosition].checked=checked
            }
            binding.keywordTV.setOnClickListener {
                binding.checkbox.isChecked=!data.checked
            }

            Log.d(TAG,data.checked.toString())

        }
    }
}