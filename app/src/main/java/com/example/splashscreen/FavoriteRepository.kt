package com.example.splashscreen

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared favorite state for the whole application.
 *
 * Favorites are persisted locally and exposed as a StateFlow so
 * Product List, Product Details, and Favorites screens update immediately.
 */
@Singleton
class FavoriteRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val preferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private val _favoriteIds =
        MutableStateFlow(loadFavoriteIds())

    val favoriteIds: StateFlow<Set<Int>> =
        _favoriteIds

    fun isFavorite(productId: Int): Boolean =
        productId in _favoriteIds.value

    fun toggleFavorite(productId: Int) {
        val updated = _favoriteIds.value.toMutableSet()

        if (!updated.add(productId)) {
            updated.remove(productId)
        }

        val immutable = updated.toSet()
        _favoriteIds.value = immutable

        preferences.edit()
            .putStringSet(KEY_FAVORITE_IDS, immutable.map(Int::toString).toSet())
            .apply()
    }

    private fun loadFavoriteIds(): Set<Int> {
        return preferences
            .getStringSet(KEY_FAVORITE_IDS, emptySet())
            .orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    }

    private companion object {
        const val PREFERENCES_NAME = "zoony_favorites"
        const val KEY_FAVORITE_IDS = "favorite_product_ids"
    }
}
