package main.com.cineramamaps.utils

import android.content.Context
import android.content.SharedPreferences

object FavoriteManager {
    private const val PREF_NAME = "favorites_pref"
    private const val KEY_FAVORITES = "fav_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveFavorite(context: Context, placeId: String, isFavorite: Boolean) {
        val prefs = getPrefs(context)
        val favorites = prefs.getStringSet(KEY_FAVORITES, mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        if (isFavorite) favorites.add(placeId) else favorites.remove(placeId)
        prefs.edit().putStringSet(KEY_FAVORITES, favorites).apply()
    }

    fun isFavorite(context: Context, placeId: String): Boolean {
        val prefs = getPrefs(context)
        val favorites = prefs.getStringSet(KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
        return favorites.contains(placeId)
    }

    fun getAllFavorites(context: Context): Set<String> {
        val prefs = getPrefs(context)
        return prefs.getStringSet(KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
    }
}
