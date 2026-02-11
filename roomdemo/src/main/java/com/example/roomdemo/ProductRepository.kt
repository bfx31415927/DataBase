package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import kotlinx.coroutines.*


class ProductRepository(private val productDao: ProductDao) {

//    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    private val _searchQuery = MutableLiveData<String>("")

    val searchQuery = _searchQuery

    // Трансформируем запрос через switchMap
    val searchResults: LiveData<List<Product>> = _searchQuery.switchMap { query ->
        if (query.isNotBlank()) {
            productDao.findProductLive(query)
        } else {
            // Если запрос пустой — возвращаем все продукты
            productDao.getAllProducts()
        }
    }

    // Метод для установки поискового запроса
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    suspend fun insertProduct(newproduct: Product) {
        productDao.insertProduct(newproduct)
    }

    suspend fun findProduct(name: String): List<Product> {
        return productDao.findProduct(name)
    }

    suspend fun deleteProduct(name: String) {
        productDao.deleteProduct(name)
    }

    suspend fun getAllMarkedProducts(): List<Product> {
        return productDao.getAllMarkedProducts()
    }

    suspend fun getAllUnmarkedProducts(): List<Product> {
        return productDao.getAllUnmarkedProducts()
    }

    suspend fun markProductOnId(id: Int) {
        productDao.markProductOnId(id)
    }

    suspend fun unmarkProductOnId(id: Int) {
        productDao.unmarkProductOnId(id)
    }

    suspend fun conditionalMarkAllProducts(name: String) {
        if (name == "") {
            productDao.markAllProducts()
        } else {
            productDao.markAllProductsWithProductName(name)
        }
    }

    suspend fun conditionalUnmarkAllProducts(name: String) {
        if (name == "") {
            productDao.unmarkAllProducts()
        } else {
            productDao.unmarkAllProductsWithProductName(name)
        }
    }

    suspend fun conditionalDeleteAllMarkedProducts(name: String) {
        if (name == "") {
            productDao.deleteAllMarkedProducts()
        } else {
            productDao.deleteAllMarkedProductsWithProductName(name)
        }
    }

    suspend fun conditionalDeleteAllUnmarkedProducts(name: String) {
        if (name == "") {
            productDao.deleteAllUnmarkedProducts()
        } else {
            productDao.deleteAllUnmarkedProductsWithProductName(name)
        }
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}