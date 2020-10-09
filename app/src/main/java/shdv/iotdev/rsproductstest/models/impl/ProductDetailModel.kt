package shdv.iotdev.rsproductstest.models.impl


import shdv.iotdev.rsproductstest.models.base.TaxModel

data class ProductDetailModel(
    override val id: Int = Int.MIN_VALUE,
    val imageUrl: String = "https://picsum.photos/id/124/200/300",
    val categories: List<String> = emptyList(),
    val category: String = categories.takeIf { it.isNotEmpty() }?.first()?:"",
    val name: String = "",
    val producer: String = "",
    val description: String = "",
    val price: Double = 9999.99
):TaxModel(
    id = id
)