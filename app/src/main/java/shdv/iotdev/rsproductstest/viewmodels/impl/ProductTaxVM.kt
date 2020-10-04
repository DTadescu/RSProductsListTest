package shdv.iotdev.rsproductstest.viewmodels.impl


import android.util.Log
import shdv.iotdev.rsproductstest.models.impl.ProductTaxModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM

class ProductTaxVM (
    override val model: ProductTaxModel
): IProductVM<ProductTaxModel> {

    init {
        model.price.set(
            (model.price.get()*100).toLong()/100.0
        )
    }

    companion object{
        val DEFAULT = ProductTaxVM(ProductTaxModel())
    }


    override fun addToBasket() {
        model.count.set(model.count.get()+1)
        Log.d("COUNT", "Count: ${model.count}")
    }

    override fun incQuantity() {
        model.count.set(model.count.get()+1)
    }

    override fun decQuantity() {
        if(model.count.get() > 0) model.count.set(model.count.get()-1)
    }
}