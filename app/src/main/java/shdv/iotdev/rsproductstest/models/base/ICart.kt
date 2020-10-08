package shdv.iotdev.rsproductstest.models.base

typealias SimpleListener = () -> Unit
interface ICart {


    fun incQuantity(id: Int)

    fun decQuantity(id: Int): Boolean

    fun add(id: Int, count: Int)

    fun get(id: Int): Int?

    fun remove(id: Int): Int?

    fun clear()

    fun setOnClearedListener(listener: SimpleListener)

    fun removeOnClearedListener(listener: SimpleListener)
}