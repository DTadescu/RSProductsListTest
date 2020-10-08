package shdv.iotdev.rsproductstest.service

import shdv.iotdev.retrofit.models.CategoryDTO
import shdv.iotdev.retrofit.models.ProductDTO
import shdv.iotdev.retrofit.models.ProductsListDTO
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM


fun ProductsListDTO.toModelList(): Collection<ProductDetailModel>{
    val models: MutableList<ProductDetailModel> = mutableListOf()
    data?.forEach {
        models.add(it.toModel())
    }
    return models
}

fun ProductDTO.toModel() = ProductDetailModel(
    id = id?:0,
    name = title?:"",
    producer = producer?:"",
    categories = categories?.toCategoriesList()?: emptyList(),
    description = short_description?:"",
    price = price?:0.0
)


private fun List<CategoryDTO>.toCategoriesList(): List<String>{
    val categories: MutableList<String> = mutableListOf()
    forEach { dto ->
        dto.title?.let { categories.add(it) }
    }
    return categories
}
