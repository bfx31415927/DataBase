package com.example.roomdemo

import android.R.attr.top
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomdemo.ui.theme.RoomDemoTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDemoTheme {

                StatusBarController()

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val owner = LocalViewModelStoreOwner.current
                    owner?.let {
                        val viewModel: MainViewModel = viewModel(
                            it,
                            "MainViewModel",
                            MainViewModelFactory(
                                LocalContext.current.applicationContext as Application
                            )
                        )
                        ScreenSetup(viewModel)
                    }
                }
            }
        }
    }
}

// Отдельный Composable для управления статус‑баром
@Composable
fun StatusBarController() {
    val systemUiController = rememberSystemUiController()
    val bgColor = MaterialTheme.colorScheme.background
    val useDarkIcons = bgColor.luminance() > 0.5

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        onDispose {}
    }
}

@Composable
fun ScreenSetup(
    viewModel: MainViewModel
) {

    val allProducts by viewModel.allProducts.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    MainScreen(
        allProducts = allProducts,
        searchResults = searchResults,
        viewModel = viewModel

    )
}

@Composable
fun MainScreen(
    allProducts: List<Product>,
    searchResults: List<Product>,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val view = LocalView.current

    val productName by viewModel.productName
    val productQuantity by viewModel.productQuantity
    val searching by viewModel.searching  // Получаем из ViewModel

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomTextField(
                title = "Product Name",
                textState = productName,
                onTextChange = { viewModel.setProductName(it) },
                keyboardType = KeyboardType.Text,
                width = 160.dp
            )

            CustomTextField(
                title = "Quantity",
                textState = productQuantity,
                onTextChange = { viewModel.setProductQuantity(it) },
                keyboardType = KeyboardType.Number,
                width = 110.dp
            )
        }
        Column() {
            val contentPadding = PaddingValues(  // внутренние отступы
                start = 4.dp,   // (8.dp по умолчанию)
                top = 4.dp,
                end = 4.dp,   // (8.dp по умолчанию)
                bottom = 4.dp
            )

            Row(//first Row
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Button(
                    onClick = {
                        if (productName.isNotBlank() && isNaturalNumber(productQuantity)) {
                            viewModel.insertProduct(
                                Product(
                                    productName,
                                    productQuantity.toInt()
                                ),
                            )
                        }
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Add")
                }

                Button(
                    onClick = {
                        viewModel.findProduct(productName)
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Search")
                }

                Button(
                    onClick = {
                        if (productName.isNotBlank()) {
                            viewModel.deleteProduct(productName)
                        }
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Del")
                }

                Button(
                    onClick = {
                        viewModel.setProductName("")
                        viewModel.setProductQuantity("")
                        viewModel.cancelSearch()
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Clear")
                }
            } //first Row

            Row(//second Row
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                val contentPadding = PaddingValues(  // внутренние отступы
                    start = 4.dp,   // (8.dp по умолчанию)
                    top = 4.dp,
                    end = 4.dp,   // (8.dp по умолчанию)
                    bottom = 4.dp
                )

                Button(
                    onClick = {
                        viewModel.unmarkAllProducts()
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Unmark All")
                }

                Button(
                    onClick = {
                        viewModel.deleteAllMarkedProducts()
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Del All Marked")
                }

                Button(
                    onClick = {
                        viewModel.deleteAllUnmarkedProducts()
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Del All Unmarked")
                }

            } //second Row

            Row(//third Row
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.deleteAllProducts()
                        hideKeyboard(context, view)
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Del All")
                }

                Button(
                    onClick = {
                        viewModel.getAllMarkedProducts()
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Get All Marked")
                }

                Button(
                    onClick = {
                        viewModel.getAllUnmarkedProducts()
                    },
                    contentPadding = contentPadding
                ) {
                    Text("Get All Unmarked")
                }

                Button(
                    onClick = {
                        viewModel.cancelSearch()
                        viewModel.setProductName("")
                        viewModel.setProductQuantity("")
                    },
                    contentPadding = contentPadding
                ) {
                    Text("")
                }
            } //third Row
        }

        TitleRow(head1 = "ID", head2 = "Product", head3 = "Quantity")
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 0.dp, bottom = 20.dp)
        ) {
            val list = if (searching) searchResults else allProducts

            items(list) { product ->
                ProductRow(
                    id = product.id, name = product.productName,
                    quantity = product.quantity,
                    marked = product.marked,
                    onItemClick = {
                        if( ! product.marked )
                            viewModel.markProductOnId(product.id)
                        else
                            viewModel.unmarkProductOnId(product.id)

                        viewModel.cancelSearch()
                    }
                )
            }
        }

    }
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(40.dp)
            .padding(start = 10.dp, end = 10.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
    ) {
        Text(
            head1, color = Color.White,
            modifier = Modifier
                .padding(start = 5.dp)
                .weight(0.1f)
        )
        Text(
            head2, color = Color.White,
            modifier = Modifier
                .weight(0.2f)
        )
        Text(
            head3, color = Color.White,
            modifier = Modifier.weight(0.2f)
        )
    }
}

@Composable
fun ProductRow(id: Int, name: String, quantity: Int, marked: Boolean, onItemClick: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
//            .padding(start = 5.dp, end = 5.dp)
            .height(30.dp)
//            .background(if (marked) Color.Green else Color.White)
            //вместо закомментированной выше строки, делаю строку ниже,
            //чтобы помечать зеленым цветом, а не помеченные - цветом родителя (а не белым)
            .then(if (marked) Modifier.background(Color.Green) else Modifier)
            .clickable { onItemClick(id) }
    ) {
        Text(id.toString(), modifier = Modifier.weight(0.1f))
        Text(name, modifier = Modifier.weight(0.2f))
        Text(quantity.toString(), modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType,
    width: Dp
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title) },
        modifier = Modifier
            .padding(1.dp)
            .width(width)
            .height(60.dp)
            .background(Color.White),

        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
        )
    )
}

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}