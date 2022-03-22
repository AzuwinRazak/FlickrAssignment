package com.azuwinrazak.flickrassignment.android.data.api

import com.azuwinrazak.flickrassignment.android.data.modals.FlickrImageResponseData
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

 interface FlickrApiInterface {

    @GET(ApiConstants.END_POINT)
    suspend fun fetchElectroluxImages(
        @Query("api_key") key: String = "81e8b32c79e27b27ab3e7a64a25b171f", //supposed put in properties file --BuildConfig.API_KEY,
        @Query("method") method: String = "flickr.photos.search",
        @Query("tags") tags: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noCallback: String = "true",
        @Query("extras") extra_url_m: String = "url_m",
        @Query("per_page") perPage: Int = 21,
        @Query("page") page: Int = 1,

    ): Response<FlickrImageResponseData>

    companion object {
//        var gson = GsonBuilder()
//            .setLenient()
//            .create()

        var flickrService: FlickrApiInterface? = null
        fun getInstance() : FlickrApiInterface {
            if (flickrService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(ApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                flickrService = retrofit.create(FlickrApiInterface::class.java)
            }
            return flickrService!!
        }

    }
}


