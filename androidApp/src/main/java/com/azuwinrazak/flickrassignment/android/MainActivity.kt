package com.azuwinrazak.flickrassignment.android

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.azuwinrazak.flickrassignment.android.data.api.FlickrApiInterface
import com.azuwinrazak.flickrassignment.android.data.di.FlickrApiFactory
import com.azuwinrazak.flickrassignment.android.data.modals.FlickrImageData
import com.azuwinrazak.flickrassignment.android.data.repository.FlickrImageRepo
import com.azuwinrazak.flickrassignment.android.data.ui.ui.RecyclerviewFlickrTheme
import com.azuwinrazak.flickrassignment.android.utils.*
import com.azuwinrazak.flickrassignment.android.viewmodels.FlickrImageViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity  : ComponentActivity() {
    lateinit var viewModel: FlickrImageViewModel

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flickrApi = FlickrApiInterface.getInstance()
        val repo = FlickrImageRepo(flickrApi)
        viewModel = ViewModelProvider(this, FlickrApiFactory(repo)).get(FlickrImageViewModel::class.java)
        viewModel.fetchElectroluxImages(resources.getString(R.string.electrolux))
        setContent {
            MainScreen(viewModel)
        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(viewModel: FlickrImageViewModel){
    val query = viewModel.query.value
    val flickrImageList = viewModel.imageList
    RecyclerviewFlickrTheme {
        Surface(color = MaterialTheme.colors.background) {
            val keyboardController = LocalSoftwareKeyboardController.current
            var selectedIndex by remember { mutableStateOf(-1) }
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 8.dp
                )
                {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true,
                            value = query,
                            onValueChange = { newValue -> viewModel.onQueryChanged(newValue)
                            },
                            label = {
                                Text(text = "Search Here")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search,
                            ),
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_search),
                                    contentDescription = null
                                )
                            },
                            keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.clearList()
                                selectedIndex = -1
                                viewModel.fetchElectroluxImages(query)
                                keyboardController?.hide()
                                viewModel.onQueryChanged("")
                            })
                        )
                    }
                }

            //to load images into list
            LazyColumn {
                itemsIndexed(items = flickrImageList) { index, item ->
                    ImageItem(image = item, index, selectedIndex) { i ->
                        selectedIndex = i
                    }
                }
            }
            val r = viewModel.state.value
                when (r) {
                    "Loading" -> CustomizeCircularProgressIndicator()
                    "Success" -> Unit
                    else -> Log.d("EROR", "Failure")
                }

            } } }
}

@Composable
fun CustomizeCircularProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            progress = 0.8f,
            color = MaterialTheme.colors.primary,
            strokeWidth = 2.dp
        )
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ImageItem(image: FlickrImageData, index: Int, selectedIndex: Int, onClick: (Int) -> Unit) {
    var backgroundColor : Color
        if (index == selectedIndex) {
            backgroundColor = MaterialTheme.colors.primary

            CallSnackBar(image.url_m)
        }

        else {
            backgroundColor = MaterialTheme.colors.background
        }
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

@Composable
fun CallSnackBar(imageURL: String?) {
    val context = LocalContext.current
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {

        // Snackbar with action item
        Snackbar(
            modifier = Modifier
                .padding(4.dp),
            action = {
                TextButton(onClick = {
                    //Save image to local
                    scopeForSaving.launch {
                    val bitmapUrlImage = imageURL?.let { urlToBitmap(it, context) }
                    SaveMediaToStorage(bitmapUrlImage, context){
                        scopeForMain.launch{
                            ShowToast(context, "Saved to Gallery")
                            //Toast.makeText(context , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
                        }
                    } }
                }) {
                    Text(text = "Download")
                }

            }
        ) {
            Text(text = "Download Image?")
        }
    }
}

 suspend fun urlToBitmap(urlImage: String, context: Context) : Bitmap{
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(urlImage)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap

}







