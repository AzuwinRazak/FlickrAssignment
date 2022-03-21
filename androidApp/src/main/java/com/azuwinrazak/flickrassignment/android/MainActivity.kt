package com.azuwinrazak.flickrassignment.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.azuwinrazak.flickrassignment.android.data.api.FlickrApiInterface
import com.azuwinrazak.flickrassignment.android.data.di.FlickrApiFactory
import com.azuwinrazak.flickrassignment.android.data.modals.FlickrImageData
import com.azuwinrazak.flickrassignment.android.data.repository.FlickrImageRepo
import com.azuwinrazak.flickrassignment.android.data.ui.theme.RecyclerviewFlickrTheme
import com.azuwinrazak.flickrassignment.android.viewmodels.FlickrImageViewModel

//fun greet(): String {
//    return Greeting().greeting()
//}

class MainActivity  : ComponentActivity() {
    lateinit var viewModel: FlickrImageViewModel
    val flickrApi = FlickrApiInterface.getInstance()
    val repo = FlickrImageRepo(flickrApi)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecyclerviewFlickrTheme {
                viewModel = ViewModelProvider(this, FlickrApiFactory(repo)).get(FlickrImageViewModel::class.java)

                Surface(color = MaterialTheme.colors.background) {
                    ImageList(flickrImageList = viewModel.imageList)
                    //fetch image using 'Electrolux'
                    viewModel.fetchFlickrImages("Electrolux")
                }
            }
        }
    }
}

@Composable
fun ImageList(flickrImageList: List<FlickrImageData>) {
    var selectedIndex by remember { mutableStateOf(-1) }
    LazyColumn {
        itemsIndexed(items = flickrImageList) { index, item ->
            ImageItem(image = item, index, selectedIndex) { i ->
                selectedIndex = i
            }
        }
    }
}

@Composable
fun ImageItem(image: FlickrImageData, index: Int, selectedIndex: Int, onClick: (Int) -> Unit) {

    val backgroundColor =
        if (index == selectedIndex) MaterialTheme.colors.primary else MaterialTheme.colors.background
    Card(
        modifier = Modifier
            .padding(8.dp, 4.dp)
            .fillMaxWidth()
            .clickable { onClick(index) }
            .height(150.dp), shape = RoundedCornerShape(8.dp), elevation = 4.dp
    ) {
        Surface(color = backgroundColor) {

            Row(
                Modifier
                    .padding(4.dp)
                    .fillMaxSize()
            ) {

                Image(
                    painter = rememberImagePainter(
                        data = image.url_m,

                        builder = {
                            scale(Scale.FILL)
                            placeholder(R.drawable.placeholder)

                        }
                    ),
                    contentDescription = image.title,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                )

                }
            }
        }
    }

