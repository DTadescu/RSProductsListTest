package shdv.iotdev.rsproductstest.models.base

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import java.util.*

abstract class TaxModel (
    open var id: Int = 0,
    open var imageUrl: String = "",
    open var category: String = "",
    open var name: String = "",
    open var producer: String = "",
    open var price: ObservableDouble = ObservableDouble(999.9999),
    open var count: ObservableInt = ObservableInt(0)
)