package com.example.roomdemo

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : ViewModel() {

//    private val _searching = mutableStateOf(false)
//    val searching: State<Boolean> = _searching

//    fun startSearch() {
//        _searching.value = true
//    }

//    val allProducts: LiveData<List<Product>>

    val searchQuery: MutableLiveData<String>

    val searchResults: LiveData<List<Product>>

    private val repository: ProductRepository

    // Состояние UI
    private val _productName = mutableStateOf("")
    val productName: State<String> = _productName
    fun setProductName(name: String) {
        _productName.value = name
    }


    private val _productQuantity = mutableStateOf("")
    val productQuantity: State<String> = _productQuantity
    fun setProductQuantity(quantity: String) {
        _productQuantity.value = quantity
    }

    init {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        repository = ProductRepository(productDao)

//        allProducts = repository.allProducts

        searchQuery = repository.searchQuery
        searchResults = repository.searchResults

    }

    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(product)
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun findProduct(name: String) {
        repository.setSearchQuery(name)
//        _searching.value = true
    }

    fun cancelSearch() {
        repository.setSearchQuery("")
//        _searching.value = false
    }

//    fun setSearching(b: Boolean) {
//        _searching.value = b
//    }

    // Удаление продукта
    fun deleteProduct(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(name)
//            withContext(Dispatchers.Main) {
//                cancelSearch()
//            }
        }
    }

    fun getAllMarkedProducts() { //toDo
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем фоновый поток
            val results = repository.getAllMarkedProducts()

            // Переключаемся на Main для обновления LiveData
            withContext(Dispatchers.Main) {
//                _searchResults.value = results
//                _searching.value = true
            }
        }
    }

    fun getAllUnmarkedProducts() { //toDo
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем фоновый поток
            val results = repository.getAllUnmarkedProducts()

            // Переключаемся на Main для обновления LiveData
            withContext(Dispatchers.Main) {
//                _searchResults.value = results
//                _searching.value = true
            }
        }
    }

    fun markProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markProductOnId(id)
//            withContext(Dispatchers.Main) {
//                cancelSearch()
//            }
        }
    }

    fun unmarkProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkProductOnId(id)
//            withContext(Dispatchers.Main) {
//                cancelSearch()
//            }
        }
    }

    fun conditionalMarkAllProducts(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.conditionalMarkAllProducts(name)
        }
    }

    fun conditionalUnmarkAllProducts(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.conditionalUnmarkAllProducts(name)
        }
    }

    fun conditionalDeleteAllMarkedProducts(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.conditionalDeleteAllMarkedProducts(name)
        }
    }

    fun conditionalDeleteAllUnmarkedProducts(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.conditionalDeleteAllUnmarkedProducts(name)
        }
    }

    fun deleteAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllProducts()
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

}