package com.shashifreeze.appopen.apputils.extensions

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shashifreeze.appopen.R
import com.shashifreeze.appopen.network.NetworkResource

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Fragment extension functions
 */

private const val TAG = "FragmentExtensions"

fun Fragment.showToast(text: String?) {
    Toast.makeText(requireContext(), text?:"", Toast.LENGTH_SHORT).show()
}


/**
 * @param: (failure: Resource.Failure? = null, retry: (() -> Unit)? = null)
 * @return: Unit
 * @author: Shashi
 * handles api error for the api calls and shows snakbar according to the found error
 */
fun Fragment.handleApiError(failure: NetworkResource.Failure? = null, retry: (() -> Unit)? = null) {
    failure?.let { it ->
        when {
            it.isNetworkError -> {
                if (it.message != null) {
                    requireView().snackBar(
                        it.message,
                        retry
                    )

                } else {
                    requireView().snackBar(
                        getString(R.string.check_net_con),
                        retry
                    )
                }

            }

            else -> {

                failure.errorCode?.let { code->

                    when(code){

                        500 -> {requireView().snackBar("Server down! Please try again later")

                           }

                        422->{requireView().snackBar("Unable to fetch! Please try again later")

                            Log.d(TAG,"Error:${it.message}")
                        }

                        else->{
                            failure.message?.let { msg ->

                                requireView().snackBar(msg)
                                Log.d(TAG,"Error:$msg")
                            }
                        }
                    }
                }

            }
        }
    }
}
