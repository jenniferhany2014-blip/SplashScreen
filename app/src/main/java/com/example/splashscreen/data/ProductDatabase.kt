package com.example.splashscreen.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room database used for locally cached products.
 *
 * Library:
 * - AndroidX Room
 *
 * Responsibility:
 * - Defines the application's local database.
 * - Exposes ProductDao for database operations.
 *
 * Database creation is handled by Hilt's DatabaseModule.
 */
@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProductConverters::class)
abstract class ProductDatabase : RoomDatabase() {

    /**
     * Provides access to product database operations.
     *
     * @return ProductDao instance.
     */
    abstract fun productDao(): ProductDao
}