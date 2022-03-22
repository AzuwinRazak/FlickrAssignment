package com.azuwinrazak.flickrassignment.android.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azuwinrazak.flickrassignment.android.R
import com.azuwinrazak.flickrassignment.android.data.api.FlickrApiInterface
import com.azuwinrazak.flickrassignment.android.data.modals.FlickrImageData
import com.azuwinrazak.flickrassignment.android.data.repository.FlickrImageRepo
import kotlinx.coroutines.*

import javax.inject.Inject


class FlickrImageViewModel @Inject constructor(private val flickrImageRepo: FlickrImageRepo) : ViewModel() {

    val query = mutableStateOf("")
    val errorMessage = MutableLiveData<String>()
    var imageList:List<FlickrImageData> by mutableStateOf(listOf())
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val state = MutableLiveData<String>()

    fun fetchElectroluxImages(query: String) {
        state.value = "Loading"
        var tag = query
        if(query.isEmpty())
            tag = "Electrolux"
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = flickrImageRepo.fetchFlickrImages(tag)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                   imageList = response.body()?.photos!!.photo!! as List<FlickrImageData>
                    state.value = "Success"
                } else {
                    onError("Error : ${response.message()} ")
                    state.value = "Failed"
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        state.value = "Failed"
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun onQueryChanged(query: String){
        this.query.value = query
    }

    fun clearList(){
        imageList = emptyList()
    }






}