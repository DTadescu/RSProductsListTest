package shdv.iotdev.rsproductstest.viewmodels.base

import androidx.databinding.ObservableInt

interface IProductVM {

    fun getCount(): ObservableInt

    fun addToBasket()

    fun incQuantity()

    fun decQuantity()

    fun showDetails()

//    fun onResume()
//
//    fun onPause()
}