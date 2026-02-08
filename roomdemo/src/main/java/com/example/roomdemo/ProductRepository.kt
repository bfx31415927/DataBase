package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class ProductRepository(private val productDao: ProductDao) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
    val searchResults = MutableLiveData<List<Product>>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertProduct(newproduct: Product) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.insertProduct(newproduct)
        }
    }

    fun deleteProduct(name: String) {
        coroutineScope.launch(Dispatchers.IO) {
            productDao.deleteProduct(name)
        }
    }

    fun findProduct(name: String): LiveData<List<Product>> {
        // Обновляем MutableLiveData в репозитории
        coroutineScope.launch {
            searchResults.value = asyncFind(name).await()
        }
        return searchResults
    }

    private fun asyncFind(name: String): Deferred<List<Product>?> =
        coroutineScope.async(Dispatchers.IO) {
            productDao.findProduct(name)
        }
}