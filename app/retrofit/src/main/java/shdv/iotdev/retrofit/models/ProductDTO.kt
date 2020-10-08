package shdv.iotdev.retrofit.models

data class ProductDTO(
    val id : Int? = null,
    val title : String? = null,
    val short_description : String? = null,
    val image_url : String? = null,
    val amount : Int? = null,
    val price : Double? = null,
    val producer : String? = null,
    val categories : List<CategoryDTO>? = null
)