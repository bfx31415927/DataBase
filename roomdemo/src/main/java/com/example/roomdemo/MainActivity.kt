package com.example.roomdemo

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
//    var productName by remember { mutableStateOf("") }
//    var productQuantity by remember { mutableStateOf("") }

    val productName by viewModel.productName
    val productQuantity by viewModel.productQuantity
    val searching by viewModel.searching  // Получаем из ViewModel

//    var searching by remember { mutableStateOf(false) }

//    val onProductTextChange = { text: String ->
//        productName = text
//    }
//
//    val onQuantityTextChange = { text: String ->
//        productQuantity = text
//    }

    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        CustomTextField(
            title = "Product Name",
            textState = productName,
//            onTextChange = onProductTextChange,
            onTextChange = { viewModel.setProductName(it) },
            keyboardType = KeyboardType.Text
        )

        CustomTextField(
            title = "Quantity",
            textState = productQuantity,
//            onTextChange = onQuantityTextChange,
            { viewModel.setProductQuantity(it) },
            keyboardType = KeyboardType.Number
        )

        Row(
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
                    if(productName.isNotBlank() && isNaturalNumber(productQuantity))
                    {
                        viewModel.insertProduct(
                            Product(
                                productName,
                                productQuantity.toInt()
                            ),
                        )
                    }
                    viewModel.cancelSearch()
                },
                contentPadding = contentPadding
            ) {
                Text("Add")
            }

            Button(
                onClick = {
                    viewModel.startSearch()
                    viewModel.findProduct(productName)
                },
                contentPadding = contentPadding
            ) {
                Text("Search")
            }

            Button(
                onClick = {
                    viewModel.cancelSearch()
                    viewModel.deleteProduct(productName)
                },
                contentPadding = contentPadding
            ) {
                Text("Delete")
            }

            Button(
                onClick = {
                    viewModel.cancelSearch()
//                    productName = ""
//                    productQuantity = ""
                    viewModel.setProductName("")
                    viewModel.setProductQuantity("")
                },
                contentPadding = contentPadding
            ) {
                Text("Clear")
            }
        }

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            val list = if (searching) searchResults else allProducts

//            item {
//                TitleRow(head1 = "ID", head2 = "Product", head3 = "Quantity")
//            }
            stickyHeader {
                TitleRow(head1 = "ID", head2 = "Product", head3 = "Quantity")
            }

            items(list) { product ->
                ProductRow(
                    id = product.id, name = product.productName,
                    quantity = product.quantity
                )
            }
        }

    }
}

@Composable
fun TitleRow(head1: String, head2: String, head3: String) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            head1, color = Color.White,
            modifier = Modifier
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
fun ProductRow(id: Int, name: String, quantity: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            id.toString(), modifier = Modifier
                .weight(0.1f)
        )
        Text(name, modifier = Modifier.weight(0.2f))
        Text(quantity.toString(), modifier = Modifier.weight(0.2f))
    }
}

@Composable
fun CustomTextField(
    title: String,
    textState: String,
    onTextChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = textState,
        onValueChange = { onTextChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = true,
        label = { Text(title) },
        modifier = Modifier.padding(10.dp),
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
    )
}

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}
