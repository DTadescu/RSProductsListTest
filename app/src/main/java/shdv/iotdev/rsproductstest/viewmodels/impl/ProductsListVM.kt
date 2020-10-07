package shdv.iotdev.rsproductstest.viewmodels.impl

import android.util.Log
import androidx.databinding.ObservableBoolean
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM

typealias AddedHandler = (list: List<ProductTaxVM<ProductDetailModel>>) -> Unit
typealias ClearHandler = () -> Unit

object ProductsListVM: IProductsListVM<ProductTaxVM<ProductDetailModel>> {
    private val productsList: MutableList<ProductTaxVM<ProductDetailModel>> = mutableListOf()
    private var currentProduct: ProductTaxVM<ProductDetailModel> = ProductTaxVM.DEFAULT
    private val productAddedListeners: MutableSet<AddedHandler> = mutableSetOf()
    private var clearListeners: MutableSet<ClearHandler> = mutableSetOf()
    var detailAction: (id:Int) -> Unit = {}
    var filterStatus: ObservableBoolean = ObservableBoolean(false)
        private set

    override fun getProducts() {
        getStubCases()
        onProductListAdded()
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
        if (!filterStatus.get()){
            filterStatus.set(true)
            onFilterChanged()
        }
    }

    fun clearFilterStatus(){
        if (filterStatus.get()){
            filterStatus.set(false)
            onFilterChanged()
            getProducts()
        }
    }

    fun getVM(position: Int) = if (position < productsList.count())
                                        productsList[position]
                                    else
                                        ProductTaxVM.DEFAULT

    fun registerProductsAddedListener(listener: AddedHandler){
        productAddedListeners.add(listener)
    }

    fun unregisterProductsAddedListener(listener: AddedHandler){
        productAddedListeners.remove(listener)
    }

    private fun onProductListAdded(){
        productAddedListeners.forEach {
            it(productsList)
            Log.d("VMADDED", "onProductListAdded")
        }
    }

    fun registerFilterChangedListener(listener: ClearHandler){
        clearListeners.add(listener)
    }

    fun unregisterFilterChangedListener(listener: ClearHandler){
        clearListeners.remove(listener)
    }

    private fun onFilterChanged(){
        clearListeners.forEach {
            it()
        }
    }

    private fun getStubCases(){
        productsList.clear()
        for (i in 0..20){
            productsList.add(ProductTaxVM(ProductDetailModel(
                id = i,
                name = "Product ${i+1}"
            ), detailAction))
        }
    }

}