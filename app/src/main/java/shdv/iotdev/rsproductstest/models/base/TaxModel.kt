package shdv.iotdev.rsproductstest.models.base

import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableInt
import java.util.*

abstract class TaxModel (

    open var count: ObservableInt = ObservableInt(0)
)