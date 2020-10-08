package shdv.iotdev.rsproductstest.viewmodels.base

import androidx.databinding.ObservableInt

interface IProductVM {

    fun getCount(): ObservableInt

    fun incQuantity(): Boolean

    fun decQuantity(): Boolean

    fun showDetails()

//    fun onResume()
//
//    fun onPause()
}