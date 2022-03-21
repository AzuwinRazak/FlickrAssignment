package com.azuwinrazak.flickrassignment.android.data.repository

import com.azuwinrazak.flickrassignment.android.data.api.FlickrApiInterface

import javax.inject.Inject

class FlickrImageRepo @Inject constructor(private val flickrImageApi: FlickrApiInterface) {
     suspend fun fetchFlickrImages(queryStr: String) = flickrImageApi.fetchElectroluxImages(tags = queryStr)
    }
