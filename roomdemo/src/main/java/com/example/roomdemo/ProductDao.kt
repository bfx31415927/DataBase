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

    @Query("SELECT * FROM products WHERE productName = :name ORDER BY productName")
    fun findProductLive(name: String): LiveData<List<Product>> //Live Search

    @Query("DELETE FROM products WHERE productName = :name")
    fun deleteProduct(name: String) //Del

    @Query("SELECT * FROM products ORDER BY productName")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("UPDATE products SET productMarked = 1 WHERE productId = :id")
    fun markProductOnId(id: Int)//клик по строке

    @Query("UPDATE products SET productMarked = 0 WHERE productId = :id")
    fun unmarkProductOnId(id: Int) //клик по строке

    //------------------------- mark All  ---------------------

    @Query("UPDATE products SET productMarked = 1 WHERE productMarked = 0")
    fun markAllProducts()

    @Query(
        "UPDATE products SET productMarked = 1 WHERE productMarked = 0" +
                " AND productName = :name"
    )
    fun markAllProductsWithProductName(name: String)

    //----------------------------------------------

    //------------------------- unmark All  ---------------------

    @Query("UPDATE products SET productMarked = 0 WHERE productMarked = 1")
    fun unmarkAllProducts()

    @Query(
        "UPDATE products SET productMarked = 0 WHERE productMarked = 1" +
                " AND productName = :name"
    )
    fun unmarkAllProductsWithProductName(name: String)

    //----------------------------------------------

    //------------------------- Del All Marked  ---------------------

    @Query("DELETE FROM products WHERE productMarked = 1")
    fun deleteAllMarkedProducts()

    @Query(
        "DELETE FROM products WHERE productMarked = 1" +
                " AND productName = :name"
    )
    fun deleteAllMarkedProductsWithProductName(name: String)

    //----------------------------------------------

    //------------------------- Del All Unmarked  ---------------------

    @Query("DELETE FROM products WHERE productMarked = 0")
    fun deleteAllUnmarkedProducts()

    @Query(
        "DELETE FROM products WHERE productMarked = 0" +
                " AND productName = :name"
    )
    fun deleteAllUnmarkedProductsWithProductName(name: String)

    //----------------------------------------------

    @Query("DELETE FROM products")
    fun deleteAllProducts() //Del All
}