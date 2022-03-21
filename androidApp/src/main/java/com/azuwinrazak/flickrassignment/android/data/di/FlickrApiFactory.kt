package com.azuwinrazak.flickrassignment.android.data.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azuwinrazak.flickrassignment.android.data.repository.FlickrImageRepo
import com.azuwinrazak.flickrassignment.android.viewmodels.FlickrImageViewModel


class FlickrApiFactory constructor(private val repository: FlickrImageRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FlickrImageViewModel::class.java)) {
            FlickrImageViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}