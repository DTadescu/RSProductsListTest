package shdv.iotdev.rsproductstest.models.impl

import shdv.iotdev.rsproductstest.models.base.ICart
import shdv.iotdev.rsproductstest.models.base.SimpleListener

class ProductsCart: ICart {

    private val cartItems: MutableMap<Int, Int> = mutableMapOf()
    private val onClearedListeners: MutableList<SimpleListener> = mutableListOf({})

    override fun incQuantity(id: Int) {
        for (item in cartItems){
            if (item.key == id){
                cartItems[id] = item.value+1
                return
            }
        }
        cartItems[id] = 1
    }

    override fun decQuantity(id: Int): Boolean {
        for (item in cartItems){
            if (item.key == id && item.value > 0){
                cartItems[id] = item.value-1
                return true
            }
        }
        return false
    }

    override fun add(id: Int, count: Int) {
        if (count > 0)
            cartItems[id] = count
    }

    override fun get(id: Int): Int? = cartItems[id]

    override fun remove(id: Int) = cartItems.remove(id)

    override fun clear() {
        onCleared()
        cartItems.clear()
    }

    override fun setOnClearedListener(listener: SimpleListener) {
        onClearedListeners += listener
    }

    override fun removeOnClearedListener(listener: SimpleListener) {
        onClearedListeners -= listener
    }

    private fun onCleared(){
        onClearedListeners.forEach {
            it()
        }
    }

}