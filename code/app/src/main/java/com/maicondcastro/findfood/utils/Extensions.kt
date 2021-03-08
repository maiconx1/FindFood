package com.maicondcastro.findfood.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maicondcastro.findfood.base.BaseRecyclerViewAdapter
import com.maicondcastro.findfood.places.PlaceItem
import com.maicondcastro.findfood.database.dto.PlaceDto
import com.maicondcastro.findfood.domain.models.Place
import org.json.JSONObject
import java.lang.Exception

fun <T> RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter<T>
) {
    this.apply {
        layoutManager = LinearLayoutManager(this.context)
        this.adapter = adapter
    }
}

fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            bool
        )
    }
}

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun PlaceDto.asDomainModel() = Place(placeId, name, rating, userRatingTotal, vicinity, openNow, lat, lng, businessStatus)
fun Place.asDatabaseModel() = PlaceDto(placeId, name, rating, userRatingTotal, vicinity, openNow, lat, lng, businessStatus)

fun Place.asItemModel() = PlaceItem(placeId, name, rating, userRatingTotal, vicinity, openNow)

fun List<PlaceDto>.asDomain() = map { it.asDomainModel() }
fun List<Place>.asDatabase() = map { it.asDatabaseModel() }
fun List<Place>.asItem() = map { it.asItemModel() }

fun JSONObject.getStringOrNull(name: String): String? {
    return try {
        getString(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getDoubleOrNull(name: String): Double? {
    return try {
        getDouble(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getIntOrNull(name: String): Int? {
    return try {
        getInt(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getJSONObjectOrNull(name: String): JSONObject? {
    return try {
        getJSONObject(name)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getBooleanOrNull(name: String): Boolean? {
    return try {
        getBoolean(name)
    } catch (_: Exception) {
        null
    }
}