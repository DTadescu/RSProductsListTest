package shdv.iotdev.rsproductstest.viewmodels.base

interface IProductsListVM<T: IProductVM> {

    val productsList: MutableList<T>

    fun getCurrentProduct():T

    fun setCurrentProduct(id: Int):Boolean

    fun getProducts()

    fun getProductsByFilter(filter: String)
}