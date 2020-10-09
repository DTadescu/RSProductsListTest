package shdv.iotdev.retrofit.service

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shdv.iotdev.retrofit.models.ProductDTO
import shdv.iotdev.retrofit.models.ProductDataDTO
import shdv.iotdev.retrofit.models.ProductsListDTO

interface GetProductsApiService {

    @GET("v1/products")
    fun getProductsList(
        @Query("filter[title]") filter: String? = null,
        @Query("startFrom") startFrom: Int = 0,
        @Query("maxItems") maxItems: Int = 10,
        @Query("sort") sort: String = "title"
    ): Observable<ProductsListDTO>

    @GET("v1/products/{productId}")
    fun getProductById(
        @Path("productId") productId: Int
    ): Observable<ProductDataDTO>

    companion object Factory{
        fun create(): GetProductsApiService = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://rstestapi.redsoftdigital.com/api/")
            .build()
            .create(GetProductsApiService::class.java)
    }
}