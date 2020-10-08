package shdv.iotdev.rsproductstest.viewmodels.impl


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.ICart
import shdv.iotdev.rsproductstest.models.base.TaxModel
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductsCart
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM

class ProductTaxVM<T: TaxModel> (
    val model: T,
    private val cart: ICart,
    var detail: (id:Int) -> Unit = {}
): IProductVM {

    private var count: ObservableInt = ObservableInt(0)

    var busy: ObservableBoolean = ObservableBoolean(false)
        private set


    companion object{
        val DEFAULT = ProductTaxVM(ProductDetailModel(), ProductsCart())
    }

    override fun getCount(): ObservableInt = count

    fun setCount(value: Int){
        if (value >= 0){
            count.set(value)
            cart.add(model.id, value)
        }
    }

    override fun incQuantity() = runWithBusy {
        count.set(count.get()+1)
        cart.incQuantity(model.id)
        return@runWithBusy true
    }

    override fun decQuantity() = runWithBusy {
        if(count.get() > 0) {
            count.set(count.get()-1)
            if (!cart.decQuantity(model.id))
                cart.remove(model.id)
            return@runWithBusy true
        }
        return@runWithBusy false
    }

    override fun showDetails() {
        detail(model.id)
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