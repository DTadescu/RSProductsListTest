package shdv.iotdev.rsproductstest.viewmodels.impl


import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import shdv.iotdev.rsproductstest.models.base.TaxModel
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM

class ProductTaxVM<T: TaxModel> (
    val model: T,
    var detail: (id:Int) -> Unit = {}
): IProductVM {

    private var count: ObservableInt = ObservableInt(0)

    var busy: ObservableBoolean = ObservableBoolean(false)
        private set


    companion object{
        val DEFAULT = ProductTaxVM(ProductDetailModel())
    }

    override fun getCount(): ObservableInt = count


    override fun addToBasket() = runWithBusy {
        count.set(count.get()+1)
        //some logic to add to cart
    }

    override fun incQuantity() = runWithBusy {
        count.set(count.get()+1)
    }

    override fun decQuantity() = runWithBusy {
        if(count.get() > 0) {
            count.set(count.get()-1)
        }

    }

    override fun showDetails() {
        detail(model.id)
    }

    private inline fun runWithBusy (crossinline action: () -> Unit){
        busy.set(true)
        action()
        busy.set(false)
    }

}