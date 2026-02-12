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

    val nameForSearchQuery: MutableLiveData<String>

    val searchResults: LiveData<List<Product>>

    private val repository: ProductRepository

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

        nameForSearchQuery = repository.nameForSearchQuery
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
    }

    fun cancelSearch() {
        repository.setSearchQuery("")
    }


    fun deleteProduct(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(name)
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