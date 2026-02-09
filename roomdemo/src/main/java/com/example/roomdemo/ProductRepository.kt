package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*


class ProductRepository(private val productDao: ProductDao) {

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()
    val searchResults = MutableLiveData<List<Product>>()

// ----- Просто делегируем вызовы DAO (без корутин!) ------
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

    suspend fun markAllProducts() {
        productDao.unmarkAllProducts()
    }

    suspend fun unmarkAllProducts() {
        productDao.unmarkAllProducts()
    }

    suspend fun deleteAllMarkedProducts() {
        productDao.deleteAllMarkedProducts()
    }

    suspend fun deleteAllUnmarkedProducts() {
        productDao.deleteAllUnmarkedProducts()
    }

    suspend fun clearProducts() {
        productDao.clearProducts()
    }
}