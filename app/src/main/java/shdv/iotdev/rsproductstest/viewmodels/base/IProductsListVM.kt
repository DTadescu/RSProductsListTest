package shdv.iotdev.rsproductstest.viewmodels.base

interface IProductsListVM {

    fun getProducts()

    fun getProductsByFilter(filter: String)
}