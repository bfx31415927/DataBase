package com.example.roomdemo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Insert
    fun insertProduct(product: Product) //Add

    @Query("SELECT * FROM products WHERE productName = :name ORDER BY productName")
    fun findProduct(name: String): List<Product> //Search

    @Query("DELETE FROM products WHERE productName = :name")
    fun deleteProduct(name: String) //Del

    @Query("SELECT * FROM products ORDER BY productName")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE productMarked = 1 ORDER BY productName")
    fun getAllMarkedProducts(): List<Product> //Get All Marked

    @Query("SELECT * FROM products WHERE productMarked = 0 ORDER BY productName")
    fun getAllUnmarkedProducts(): List<Product> //Get All Unmarked

    @Query("UPDATE products SET productMarked = 1 WHERE productId = :id")
    fun markProductOnId(id: Int)//клик по строке

    @Query("UPDATE products SET productMarked = 0 WHERE productId = :id")
    fun unmarkProductOnId(id: Int) //клик по строке

    @Query("UPDATE products SET productMarked = 0 WHERE productMarked = 1")
    fun unmarkAllProducts() //Unmark All

    @Query("DELETE FROM products WHERE productMarked = 1")
    fun deleteAllMarkedProducts() //Del All Marked

    @Query("DELETE FROM products WHERE productMarked = 0")
    fun deleteAllUnmarkedProducts() //Del All Unmarked

    @Query("DELETE FROM products")
    fun deleteAllProducts() //Del All
}