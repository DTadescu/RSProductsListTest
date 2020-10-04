package shdv.iotdev.rsproductstest.viewmodels.base

import shdv.iotdev.rsproductstest.models.base.TaxModel

interface IProductVM<T: TaxModel> {

    val model: T

    fun addToBasket()

    fun incQuantity()

    fun decQuantity()

//    fun onResume()
//
//    fun onPause()
}