package com.example.roomdemo

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["productId"], unique = true),
        Index(value = ["productName"]) //из двух полей: Index(value = ["productName", ""quantity")
    ]
)
class Product {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "productId")
    var id: Int = 0

    @ColumnInfo(name = "productName", defaultValue = "")
    var productName: String = ""
    var quantity: Int = 0

    // Новое поле
    @ColumnInfo(name = "productMarked", defaultValue = "0")
    var marked: Boolean = false

    constructor() {}

    constructor(id: Int, productname: String, quantity: Int) {
        this.productName = productname
        this.quantity = quantity
    }

    constructor(productname: String, quantity: Int) {
        this.productName = productname
        this.quantity = quantity
    }
}