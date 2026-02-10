package com.example.roomdemo

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application): ViewModel() {

    private val _searching = mutableStateOf(false)
    val searching: State<Boolean> = _searching

    fun startSearch() {
        _searching.value = true
    }

    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    private val _searchResults = MutableLiveData<List<Product>>()
    val searchResults: LiveData<List<Product>> = _searchResults

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

        allProducts = repository.allProducts
    }

    // Вставка продукта
    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(product)
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }


    // Поиск продукта
    fun findProduct(name: String) {
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем фоновый поток
            val results = repository.findProduct(name)

            // Переключаемся на Main для обновления LiveData
            withContext(Dispatchers.Main) {
                _searchResults.value = results
                _searching.value = true
            }
        }
    }
    fun cancelSearch() {
        _searchResults.value = emptyList()
        _searching.value = false
    }


    // Удаление продукта
    fun deleteProduct(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(name)
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun getAllMarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем фоновый поток
            val results = repository.getAllMarkedProducts()

            // Переключаемся на Main для обновления LiveData
            withContext(Dispatchers.Main) {
                _searchResults.value = results
                _searching.value = true
            }
        }
    }

    fun getAllUnmarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {  // Явно указываем фоновый поток
            val results = repository.getAllUnmarkedProducts()

            // Переключаемся на Main для обновления LiveData
            withContext(Dispatchers.Main) {
                _searchResults.value = results
                _searching.value = true
            }
        }
    }

    fun markProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markProductOnId(id)
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun unmarkProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkProductOnId(id)
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun markAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAllProducts()
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun unmarkAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkAllProducts()
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun deleteAllMarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMarkedProducts()
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
        }
    }

    fun deleteAllUnmarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUnmarkedProducts()
            withContext(Dispatchers.Main) {
                cancelSearch()
            }
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