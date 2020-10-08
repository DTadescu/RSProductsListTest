package shdv.iotdev.rsproductstest.models.impl


import shdv.iotdev.rsproductstest.models.base.TaxModel

data class ProductDetailModel(
    override val id: Int = 0,
    val imageUrl: String = "https://picsum.photos/id/124/200/300",
    val categories: List<String> = listOf("Category1", "Category2", "Category3"),
    val category: String = categories.takeIf { it.isNotEmpty() }?.first()?:"",
    val name: String = "Product",
    val producer: String = "Producer is not available",
    val description: String = "Sorry, description is not available. " +
            "I say, description is not available. Look at me, description is not available. " +
            "Fuck off, description is not available. ",
    val price: Double = 999.99
):TaxModel(
    id = id
)