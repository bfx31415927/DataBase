package com.example.roomdemo

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun isNaturalNumber(str: String): Boolean {
    // 1. Проверяем, что строка не пустая
    if (str.isEmpty()) return false

    // 2. Пытаемся преобразовать в Int; если не получилось — не число
    val num = str.toIntOrNull() ?: return false


    // 3. Проверяем, что число ≥ 1
    return num >= 1
}

fun hideKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}