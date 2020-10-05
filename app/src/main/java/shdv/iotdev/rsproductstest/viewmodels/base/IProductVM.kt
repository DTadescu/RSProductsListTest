package shdv.iotdev.rsproductstest.viewmodels.base

import shdv.iotdev.rsproductstest.models.base.TaxModel

interface IProductVM {

    fun addToBasket()

    fun incQuantity()

    fun decQuantity()

    fun showDetails()

//    fun onResume()
//
//    fun onPause()
}