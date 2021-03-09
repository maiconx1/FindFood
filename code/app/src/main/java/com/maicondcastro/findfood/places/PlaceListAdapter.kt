package com.maicondcastro.findfood.places

import androidx.recyclerview.widget.DiffUtil
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.base.BaseRecyclerViewAdapter
import com.maicondcastro.findfood.places.models.PlaceItem

class PlaceListAdapter(callBack: (selectedPlace: PlaceItem) -> Unit) :
    BaseRecyclerViewAdapter<PlaceItem>(callBack, PlaceDiffCallback()) {

    override fun getLayoutRes(viewType: Int) = R.layout.place_item

    class PlaceDiffCallback : DiffUtil.ItemCallback<PlaceItem>() {
        override fun areItemsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(oldItem: PlaceItem, newItem: PlaceItem): Boolean {
            return oldItem == newItem
        }

    }
}