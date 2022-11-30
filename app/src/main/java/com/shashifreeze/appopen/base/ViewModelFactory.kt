package com.shashifreeze.appopen.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shashifreeze.appopen.view.ui.history.HistoryRepository
import com.shashifreeze.appopen.view.ui.history.HistoryViewModel
import com.shashifreeze.appopen.view.ui.home.HomeRepository
import com.shashifreeze.appopen.view.ui.home.HomeViewModel
import com.shashifreeze.appopen.view.ui.main.MainRepository
import com.shashifreeze.appopen.view.ui.main.MainViewModel
import com.shashifreeze.appopen.view.ui.search.SearchRepository
import com.shashifreeze.appopen.view.ui.search.SearchViewModel

/**
 * @author: Shashi
 * Model view factory to create view models */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repo1: BaseRepository, private val repo2: BaseRepository?=null) :
    ViewModelProvider.NewInstanceFactory() {
    /**
     * @author: Shashi
     * Creates objects of a view model specified*/
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repo1 as MainRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repo1 as HomeRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repo1 as HistoryRepository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repo1 as SearchRepository) as T
            }

            else -> throw IllegalArgumentException("View model not found")
        }
    }
}