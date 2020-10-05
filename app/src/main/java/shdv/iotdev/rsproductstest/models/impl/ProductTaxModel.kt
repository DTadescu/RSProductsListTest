package shdv.iotdev.rsproductstest.models.impl

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.TaxModel

data class ProductTaxModel(
    var id: Int = 0,
    var imageUrl: String = "https://picsum.photos/id/124/200/300",
    //override var imageUrl: String = "",
    var category: String = "Category",
    var name: String = "Product",
    var producer: String = "Producer",
    var price: Double = 999.99,
    override var count: ObservableInt = ObservableInt(0)
): TaxModel()