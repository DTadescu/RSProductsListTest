package shdv.iotdev.rsproductstest.models.impl

import androidx.databinding.ObservableInt

class Counter(
    value: Int = 0
): ObservableInt(value) {
    override fun set(value: Int) {
        if (value >= 0)
            super.set(value)
    }
}