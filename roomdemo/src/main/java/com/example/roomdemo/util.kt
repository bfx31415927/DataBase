package com.example.roomdemo

fun isNaturalNumber(str: String): Boolean {
    // 1. Проверяем, что строка не пустая
    if (str.isEmpty()) return false

    // 2. Пытаемся преобразовать в Int; если не получилось — не число
    val num = str.toIntOrNull() ?: return false


    // 3. Проверяем, что число ≥ 1
    return num >= 1
}
