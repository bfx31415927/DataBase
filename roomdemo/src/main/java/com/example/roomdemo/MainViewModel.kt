package com.example.roomdemo

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State

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


    private val _productQuantity = mutableStateOf("")
    val productQuantity: State<String> = _productQuantity



    init {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        repository = ProductRepository(productDao)

        allProducts = repository.allProducts
        searchResults = repository.searchResults
    }

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    fun findProduct(name: String) {
        repository.findProduct(name)
    }

    fun deleteProduct(name: String) {
        repository.deleteProduct(name)
    }

    // Методы для обновления
    fun setProductName(name: String) {
        _productName.value = name
    }

    fun setProductQuantity(quantity: String) {
        _productQuantity.value = quantity
    }

}