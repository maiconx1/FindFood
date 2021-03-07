package com.maicondcastro.findfood.extensions

import com.maicondcastro.findfood.database.dto.PlaceDto
import com.maicondcastro.findfood.domain.models.Place

fun PlaceDto.asDomainModel() = Place(placeId, name, rating, userRatingTotal, vicinity, openNow, lat, lng, businessStatus)
fun Place.asDatabaseModel() = PlaceDto(placeId, name, rating, userRatingTotal, vicinity, openNow, lat, lng, businessStatus)

fun List<PlaceDto>.asDomain() = map { it.asDomainModel() }
fun List<Place>.asDatabase() = map { it.asDatabaseModel() }