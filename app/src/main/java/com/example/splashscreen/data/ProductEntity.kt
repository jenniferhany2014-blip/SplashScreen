package com.example.splashscreen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.splashscreen.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val thumbnail: String,
    val images: List<String>
)

fun ProductEntity.toProduct(): Product = Product(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    rating = rating,
    thumbnail = thumbnail,
    images = images
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    title = title,
    description = description,
    category = category,
    price = price,
    rating = rating,
    thumbnail = thumbnail,
    images = images
)
