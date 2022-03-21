package com.azuwinrazak.flickrassignment.android.data.modals

import java.io.Serializable

data class FlickrImageResponseData (
    val photos: Photos? = null,
    val stat: String? = null
) : Serializable

data class FlickrImageData(
    val id: String? = null,
    val owner: String? = null,
    val secret: String? = null,
    val server: String? = null,
    val farm: Int? = null,
    val title: String? = null,
    val ispublic: Int? = null,
    val isfriend: Int? = null,
    val isfamily: Int? = null,
    val url_m: String? = null,
    val height_m: Int? = null,
    val width_m: Int? = null,
) : Serializable

data class Photos(
    val page: Int? = null,
    val pages: Int? = null,
    val perpage: Int? = null,
    val total: Int? = null,
    val photo: List<FlickrImageData?>? = null
) : Serializable
