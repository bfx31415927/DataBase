package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert
    fun insertProduct(product: Product)

    @Query("SELECT * FROM products WHERE productName = :name ORDER BY productName")
    fun findProduct(name: String): List<Product>

    @Query("DELETE FROM products WHERE productName = :name")
    fun deleteProduct(name: String)

    @Query("SELECT * FROM products ORDER BY productName")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE productMarked = 1")
    fun getAllMarkedProducts(): List<Product>

    @Query("SELECT * FROM products WHERE productMarked = 0")
    fun getAllUnmarkedProducts(): List<Product>

    @Query("UPDATE products SET productMarked = 1 WHERE productId = :id")
    fun markProductOnId(id: Int)

    @Query("UPDATE products SET productMarked = 0 WHERE productId = :id")
    fun unmarkProductOnId(id: Int)

    @Query("UPDATE products SET productMarked = 0 WHERE productMarked = 1")
    fun unmarkAllProducts()

    @Query("DELETE FROM products WHERE productMarked = 1")
    fun deleteAllMarkedProducts()
}