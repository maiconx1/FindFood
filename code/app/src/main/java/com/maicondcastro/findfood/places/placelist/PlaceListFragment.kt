package com.maicondcastro.findfood.places.placelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maicondcastro.findfood.R
import com.maicondcastro.findfood.base.BaseFragment
import com.maicondcastro.findfood.databinding.FragmentPlaceListBinding
import com.maicondcastro.findfood.places.PlaceListAdapter
import com.maicondcastro.findfood.utils.setDisplayHomeAsUpEnabled
import com.maicondcastro.findfood.utils.setTitle
import com.maicondcastro.findfood.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaceListFragment : BaseFragment() {

    override val viewModel: PlaceListViewModel by viewModel()

    private lateinit var binding: FragmentPlaceListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_list, container, false)
        binding.viewModel = viewModel

        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.nearby_food))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaces(0.0, 0.0, 100)
    }

    private fun setupRecyclerView() {
        val adapter = PlaceListAdapter {
            //viewModel.navigationCommand.postValue(NavigationCommand.To())
        }

        binding.savedList.setup(adapter)
    }
}