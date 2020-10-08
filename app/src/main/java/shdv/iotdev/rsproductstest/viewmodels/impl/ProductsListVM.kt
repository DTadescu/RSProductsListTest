package shdv.iotdev.rsproductstest.viewmodels.impl

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import shdv.iotdev.retrofit.models.ProductsListDTO
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductsCart
import shdv.iotdev.rsproductstest.service.GetProducts
import shdv.iotdev.rsproductstest.service.toModel
import shdv.iotdev.rsproductstest.service.toModelList
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM

typealias AddedHandler = (list: List<ProductTaxVM<ProductDetailModel>>) -> Unit
typealias ClearHandler = () -> Unit

object ProductsListVM: IProductsListVM<ProductTaxVM<ProductDetailModel>> {
    private val productsList: MutableList<ProductTaxVM<ProductDetailModel>> = mutableListOf()
    private var currentProduct: ProductTaxVM<ProductDetailModel> = ProductTaxVM.DEFAULT

    private val cart = ProductsCart().also {it.setOnClearedListener(this::clearProductsCount)}

    private val productAddedListeners: MutableSet<AddedHandler> = mutableSetOf()
    private var clearListeners: MutableSet<ClearHandler> = mutableSetOf()

    private val repository = GetProducts.provider

    var detailAction: (id:Int) -> Unit = {}
    var filterStatus: ObservableBoolean = ObservableBoolean(false)
        private set

    var searchEdit: ObservableField<String> = ObservableField("")
        private set

    override fun onSearchEditChanged(s: CharSequence?, start: Int, before: Int, count: Int){
        searchEdit.set(s.toString())
    }

    override fun update(){
        onFilterChanged()
        productsList.clear()
        getProducts()
    }

    override fun getProducts() {
        repository.getProductsListTitleSorted(productsList.size, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { result ->
                val vmList = result.toViewModelList()
                productsList.addAll(vmList)
                onProductListAdded(vmList)
            }
        // getStubCases()
       // onProductListAdded()
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
            productsList.clear()
        }
    }

    fun clearFilterStatus(){
        if (filterStatus.get()){
            filterStatus.set(false)
            searchEdit.set("")
            onFilterChanged()
            productsList.clear()
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

    private fun onProductListAdded(products: List<ProductTaxVM<ProductDetailModel>>){
        productAddedListeners.forEach {
            it(products)
           // Log.d("VMADDED", "onProductListAdded")
        }
    }

    fun registerFilterChangedListener(listener: ClearHandler){
        clearListeners.add(listener)
    }

    fun unregisterFilterChangedListener(listener: ClearHandler){
        clearListeners.remove(listener)
    }

    fun productsCount() = productsList.size

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
            ), cart, detailAction).apply {
                setCount(cart.get(model.id)?:0)
            })
        }
    }

    private fun clearProductsCount(){
        productsList.forEach {
            while (it.decQuantity()){}
        }
    }

    private fun ProductsListDTO.toViewModelList(): List<ProductTaxVM<ProductDetailModel>>{
        val viewModels: MutableList<ProductTaxVM<ProductDetailModel>> = mutableListOf()
        data?.forEach {
            viewModels.add(ProductTaxVM(model = it.toModel(),
                                        detail = detailAction,
                                        cart = cart).apply {
                setCount(cart.get(model.id)?:0)
            })
        }
        return viewModels
    }

}