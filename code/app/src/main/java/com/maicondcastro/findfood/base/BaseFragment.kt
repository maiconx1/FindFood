package com.maicondcastro.findfood.base

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

/**
 * Base Fragment to observe on the common LiveData objects
 */
abstract class BaseFragment : Fragment() {
    /**
     * Every fragment has to have an instance of a view model that extends from the BaseViewModel
     */
    abstract val viewModel: BaseViewModel

    override fun onStart() {
        super.onStart()
        viewModel.showSnackBar.observe(this, {
            Snackbar.make(this.requireView(), it, Snackbar.LENGTH_LONG).show()
        })
        viewModel.showSnackBarAction.observe(this, {
            Snackbar.make(this.requireView(), getString(it.message), Snackbar.LENGTH_LONG)
                .setAction(it.btnText, it.action).show()
        })

        viewModel.navigationCommand.observe(this, { command ->
            when (command) {
                is NavigationCommand.To -> findNavController().navigate(command.directions)
                is NavigationCommand.Back -> findNavController().popBackStack()
                is NavigationCommand.BackTo -> findNavController().popBackStack(
                    command.destinationId,
                    false
                )
            }
        })
        viewModel.openUrl.observe(this, { url ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        })
    }
}