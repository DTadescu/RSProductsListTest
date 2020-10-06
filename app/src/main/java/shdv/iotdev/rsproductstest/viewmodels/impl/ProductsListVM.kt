package shdv.iotdev.rsproductstest.viewmodels.impl

import androidx.databinding.ObservableBoolean
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM

object ProductsListVM: IProductsListVM<ProductTaxVM<ProductDetailModel>> {
    override val productsList: MutableList<ProductTaxVM<ProductDetailModel>> = mutableListOf()
    private var currentProduct: ProductTaxVM<ProductDetailModel> = ProductTaxVM.DEFAULT
    var filterStatus: ObservableBoolean = ObservableBoolean(false)
        private set

    override fun getProducts() {
        TODO("Not yet implemented")
    }

    override fun getProductsByFilter(filter: String) {
        TODO("Not yet implemented")
    }

    override fun getCurrentProduct() = currentProduct

    override fun setCurrentProduct(id: Int): Boolean {
        return productsList.find { it.model.id == id }?.let {
            currentProduct = it
            true
        }?:false
    }

    fun setFilterStatus(){
        filterStatus.set(true)
    }

    fun clearFilterStatus(){
        filterStatus.set(false)
    }

}