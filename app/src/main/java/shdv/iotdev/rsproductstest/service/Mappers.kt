package shdv.iotdev.rsproductstest.service

import shdv.iotdev.retrofit.models.CategoryDTO
import shdv.iotdev.retrofit.models.ProductDTO
import shdv.iotdev.retrofit.models.ProductDataDTO
import shdv.iotdev.rsproductstest.models.impl.DTOException
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel

/**
 * Mapper to convert pojo of id request to product model
 */
fun ProductDataDTO.toModel() = ProductDetailModel(
    id = data?.id ?: throw DTOException("Server error"),
    imageUrl = data?.image_url?:"",
    name = data?.title ?: throw DTOException("Server error"),
    producer = data?.producer ?: "",
    categories = data?.categories?.toCategoriesList() ?: kotlin.collections.emptyList(),
    description = data?.short_description ?: "",
    price = data?.price ?: throw DTOException("Server error")
)


fun ProductDTO.toModel() = ProductDetailModel(
    id = id?:throw DTOException("Server error"),
    imageUrl = image_url?:"",
    name = title?:throw DTOException("Server error"),
    producer = producer?:"",
    categories = categories?.toCategoriesList()?: emptyList(),
    description = short_description?:"",
    price = price ?: throw DTOException("Server error")
)


private fun List<CategoryDTO>.toCategoriesList(): List<String>{
    val categories: MutableList<String> = mutableListOf()
    forEach { dto ->
        dto.title?.let { categories.add(it) }
    }
    return categories
}
