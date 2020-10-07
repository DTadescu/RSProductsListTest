package shdv.iotdev.rsproductstest.viewmodels.base

interface IProductsListVM<T: IProductVM> {

    fun getCurrentProduct():T

    fun setCurrentProduct(id: Int):Boolean

    fun getProducts()

    fun getProductsByFilter(filter: String)
}