package com.example.splashscreen.di

import android.content.Context
import androidx.room.Room
import com.example.splashscreen.data.ProductDao
import com.example.splashscreen.data.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides Room database dependencies.
 *
 * Libraries:
 * - AndroidX Room
 * - Dagger Hilt
 *
 * Responsibility:
 * - Creates the ProductDatabase.
 * - Provides ProductDao to classes that need database access.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "zoony_products.db"

    /**
     * Creates the application's Room database.
     *
     * @param context application context supplied by Hilt.
     * @return ProductDatabase instance.
     */
    @Provides
    @Singleton
    fun provideProductDatabase(
        @ApplicationContext context: Context
    ): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    /**
     * Provides the ProductDao used to access cached products.
     *
     * @param database application ProductDatabase.
     * @return ProductDao instance.
     */
    @Provides
    fun provideProductDao(
        database: ProductDatabase
    ): ProductDao {
        return database.productDao()
    }
}