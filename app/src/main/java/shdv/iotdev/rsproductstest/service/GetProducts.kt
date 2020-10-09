package shdv.iotdev.rsproductstest.service

import shdv.iotdev.retrofit.service.GetProductsApiService

/**
 * API service to get data from server
 */
class GetProducts {

    private val apiService =
        GetProductsApiService.create()

    fun getProductsListTitleSorted(startFrom: Int, maxItems: Int, filter: String? = null) =
        apiService.getProductsList(startFrom = startFrom, maxItems = maxItems, sort = "title", filter = filter)

    fun getProductById(id: Int) = apiService.getProductById(id)


    companion object{
        val provider = GetProducts()
    }
}