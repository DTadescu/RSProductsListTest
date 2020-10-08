package shdv.iotdev.rsproductstest.service

import shdv.iotdev.retrofit.service.GetProductsApiService

class GetProducts {

    private val apiService =
        GetProductsApiService.create()

    fun getProductsListTitleSorted(startFrom: Int, maxItems: Int) =
        apiService.getProductsList(startFrom = startFrom, maxItems = maxItems, sort = "title")

    fun getProductById(id: Int) = apiService.getProductById(id)


    companion object{
        val provider = GetProducts()
    }
}