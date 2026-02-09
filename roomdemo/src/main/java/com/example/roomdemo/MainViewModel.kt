package com.example.roomdemo

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): ViewModel() {

    private val _searching = mutableStateOf(false)
    val searching: State<Boolean> = _searching

    fun startSearch() {
        _searching.value = true
    }

    fun cancelSearch() {
        _searching.value = false
    }

    val allProducts: LiveData<List<Product>>
    private val repository: ProductRepository
    val searchResults: MutableLiveData<List<Product>>

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
        searchResults = repository.searchResults
    }

    // Вставка продукта
    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(product)
        }
    }


    // Поиск продукта
    fun findProduct(name: String) {
        viewModelScope.launch {
            val results = repository.findProduct(name)
            searchResults.value = results
        }
    }

    // Удаление продукта
    fun deleteProduct(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(name)
        }
    }

    fun getAllMarkedProducts() {
        viewModelScope.launch {
            val marked = repository.getAllMarkedProducts()
            searchResults.value = marked
        }
    }

    fun getAllUnmarkedProducts() {
        viewModelScope.launch {
            val marked = repository.getAllUnmarkedProducts()
            searchResults.value = marked
        }
    }

    fun markProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markProductOnId(id)
        }
    }

    fun unmarkProductOnId(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkProductOnId(id)
        }
    }

    fun markAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.markAllProducts()
        }
    }

    fun unmarkAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unmarkAllProducts()
        }
    }

    fun deleteAllMarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMarkedProducts()
        }
    }

    fun deleteAllUnmarkedProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUnmarkedProducts()
        }
    }

    fun clearProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearProducts()
        }
    }

}