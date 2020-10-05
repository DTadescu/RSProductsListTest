package shdv.iotdev.rsproductstest.models.impl

import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.TaxModel
import java.util.*

data class ProductDetailModel(
    var id: Int = 0,
    var imageUrl: String = "https://picsum.photos/id/124/200/300",
    //override var imageUrl: String = "",
    var categories: List<String> = listOf("Category1", "Category2", "Category3"),
    var name: String = "Product",
    var producer: String = "Producer is not available",
    var description: String = "Sorry, description is not available. " +
            "I say, description is not available. Look at me, description is not available. " +
            "Fuck off, description is not available. ",
    var price: Double = 999.99,
    override var count: ObservableInt = ObservableInt(0)
):TaxModel()