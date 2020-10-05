package shdv.iotdev.rsproductstest.viewmodels.impl


import androidx.databinding.ObservableBoolean
import shdv.iotdev.rsproductstest.models.base.TaxModel
import shdv.iotdev.rsproductstest.models.impl.ProductTaxModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM

class ProductTaxVM<T: TaxModel> (
    val model: T,
    val detail: (count:Int) -> Unit = {}
): IProductVM {

    var busy: ObservableBoolean = ObservableBoolean(false)
        private set


    companion object{
        val DEFAULT = ProductTaxVM(ProductTaxModel())
    }


    override fun addToBasket() = runWithBusy {
       model.count.set(model.count.get()+1)

        //some logic to add to cart
    }

    override fun incQuantity() = runWithBusy {
        model.count.set(model.count.get()+1)

    }

    override fun decQuantity() = runWithBusy {
        if(model.count.get() > 0) model.count.set(model.count.get()-1)

    }

    override fun showDetails() {
        detail(model.count.get())
    }

    private inline fun runWithBusy (crossinline action: () -> Unit){
        busy.set(true)
        action()
        busy.set(false)
    }
}