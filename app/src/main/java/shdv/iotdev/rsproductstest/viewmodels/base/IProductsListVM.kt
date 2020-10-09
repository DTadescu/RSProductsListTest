package shdv.iotdev.rsproductstest.viewmodels.base

import androidx.databinding.ObservableBoolean
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.impl.*

typealias AddedHandler = (list: List<ProductTaxVM<ProductDetailModel>>) -> Unit
typealias DetailHandler = () -> Unit

interface IProductsListVM<T: IProductVM> {

    fun checkBusy():ObservableBoolean

    fun getCurrentProduct():T

    fun getProducts()

    fun update()

    fun productsCount(): Int

    fun resetDetailListener(listener: DetailHandler)

    fun setDetailListener(listener: DetailHandler)

    fun unregisterProductsAddedListener(listener: AddedHandler)

    fun registerProductsAddedListener(listener: AddedHandler)

    fun getVM(position: Int): T
}