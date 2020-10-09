package shdv.iotdev.rsproductstest.viewmodels.impl


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.ICart
import shdv.iotdev.rsproductstest.models.base.TaxModel
import shdv.iotdev.rsproductstest.models.impl.Counter
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductsCart
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM

/**
 *  Viewmodel class for product's card
 */
class ProductTaxVM<T: TaxModel> (
    val model: T,
    private val cart: ICart,
    var detail: ((id:Int, counter:Counter) -> Unit)? = null,
    private var count: Counter = Counter(0)
): IProductVM {

    var busy: ObservableBoolean = ObservableBoolean(false)
        private set


    companion object{
        val DEFAULT = ProductTaxVM(ProductDetailModel(), ProductsCart())
    }

    /**
     * Function to @return [count] of product in the [cart]
     */
    override fun getCount(): ObservableInt = count

    /**
     * Function for set [count] of product and add to the [cart]
     */
    override fun setCount(value: Int){
        if (value >= 0){
            count.set(value)
            cart.add(model.id, value)
        }
    }

    /**
     * Function for increase product's [count] in the [cart]
     */
    override fun incQuantity() = runWithBusy {
        count.set(count.get()+1)
        cart.incQuantity(model.id)
        return@runWithBusy true
    }

    /**
     * Function for decrease product's count in the cart
     * @return false if [count] = 0
     */
    override fun decQuantity() = runWithBusy {
        if(count.get() > 0) {
            count.set(count.get()-1)
            if (!cart.decQuantity(model.id))
                cart.remove(model.id)
            return@runWithBusy true
        }
        return@runWithBusy false
    }

    /**
     * Function to run callback, that presents detail information of a product
     */
    override fun showDetails() {
        detail?.invoke(model.id, count)
    }

    private inline fun runWithBusy (crossinline action: () -> Boolean):Boolean{
        busy.set(true)
        if (action()){
            busy.set(false)
            return true
        } else{
            busy.set(false)
            return false
        }
    }

}