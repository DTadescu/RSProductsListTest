package shdv.iotdev.rsproductstest.models.impl

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.TaxModel

data class ProductTaxModel(
    override var id: Int = 0,
    override var imageUrl: String = "https://picsum.photos/id/124/200/300",
    //override var imageUrl: String = "",
    override var category: String = "Category",
    override var name: String = "Product",
    override var producer: String = "Producer",
    override var price: ObservableDouble = ObservableDouble(999.9999),
    override var count: ObservableInt = ObservableInt(0)
): TaxModel()