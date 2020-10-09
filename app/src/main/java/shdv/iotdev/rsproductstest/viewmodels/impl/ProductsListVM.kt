package shdv.iotdev.rsproductstest.viewmodels.impl

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import shdv.iotdev.retrofit.models.ProductsListDTO
import shdv.iotdev.rsproductstest.models.impl.Counter
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductsCart
import shdv.iotdev.rsproductstest.service.GetProducts
import shdv.iotdev.rsproductstest.service.toModel
import shdv.iotdev.rsproductstest.viewmodels.base.*


object ProductsListVM: IProductsListVM<ProductTaxVM<ProductDetailModel>>, IFilterable, IErrorObservable {
    private const val errorMessage = "Ошибка получения данных по API."
    private const val LOGGER = "PROVIDER"

    private val productsList: MutableList<ProductTaxVM<ProductDetailModel>> = mutableListOf()
    private var currentProduct: ProductTaxVM<ProductDetailModel> = ProductTaxVM.DEFAULT

    private val cart = ProductsCart().also {it.setOnClearedListener(this::clearProductsCount)}

    private val productAddedListeners: MutableSet<AddedHandler> = mutableSetOf()
    private var clearListeners: MutableSet<ClearHandler> = mutableSetOf()
    private var detailListener: DetailHandler = {}
    private val errorListeners: MutableList<ErrorHandler> = mutableListOf()

    private val repository = GetProducts.provider
    private var queryId = (Math.random()*1000).toInt()

    private val detailAction: (id:Int, counter: Counter) -> Unit = this::setCurrentProduct

    var filterStatus: ObservableBoolean = ObservableBoolean(false)
        private set

    var searchEdit: ObservableField<String> = ObservableField("")
        private set

    private var isBusy = false

    override fun checkBusy() = ObservableBoolean(isBusy)

    /**
     *  Callback, that run receiving products, contains [s] in the title.
     *  It clears all previous products cards
     */
    override fun onSearchEditChanged(s: String){
        searchEdit.set(s)
        onFilterChanged()
        productsList.clear()
        getProducts()
    }

    /**
     *  Function, that run receiving products cards.
     *  It clears all previous products cards
     */
    override fun update(){
        onFilterChanged()
        productsList.clear()
        getProducts()
    }

    /**
     *  Function for asynchronous receiving products cards and giving it to activity callback.
     *  If previous query didn't come back before new query created, previous query's answer doesn't handle
     */
    override fun getProducts() {
        Log.d(LOGGER, "Start to get products")
        isBusy = true
        queryId = (Math.random()*1000).toInt()
        val idToCompare = queryId
        repository.getProductsListTitleSorted(productsList.size, 10, filter = searchEdit.get())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe (
                { result ->
                    if (queryId == idToCompare) {
                        val vmList = result.toViewModelList()
                        productsList.addAll(vmList)
                        onProductListAdded(vmList)
                        isBusy = false
                        Log.d(LOGGER, "End to get products")
                    }
                },
                {error ->
                    error.printStackTrace()
                    errorListeners.forEach {
                        it(errorMessage)
                    }
                    isBusy = false
                }
            )
    }

    override fun getCurrentProduct() = currentProduct

    /**
     *  Callback for asynchronously receiving detail information about product.
     *  It receives [id] of product and product's count in the cart [counter].
     *  After received it runs activity callback [detailListener] for new activity running.
     */
    private fun setCurrentProduct(id: Int, counter: Counter) {
        queryId = (Math.random()*1000).toInt()
        val idToCompare = queryId
        repository.getProductById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                    if (queryId == idToCompare) {
                        currentProduct = ProductTaxVM(
                            model = result.toModel(),
                            cart = cart,
                            detail = detailAction,
                            count = counter)
                        detailListener()
                    }
            },
                {error ->
                    error.printStackTrace()
                    errorListeners.forEach {
                        it(errorMessage)
                    }
                }
            )
    }

    /**
     * Function for set filter for search products by title-filter
     */
    override fun setFilterStatus(){
        if (!filterStatus.get()){
            filterStatus.set(true)
            onFilterChanged()
            productsList.clear()
        }
    }

    /**
     * Function for clear filter for search products by title-filter
     */
    override fun clearFilterStatus(){
        if (filterStatus.get()){
            filterStatus.set(false)
            searchEdit.set("")
            onFilterChanged()
            productsList.clear()
            getProducts()
        }
    }

    /**
     *  Function for get product viewmodel by [position].
     */
    override fun getVM(position: Int) = if (position < productsList.count())
                                        productsList[position]
                                    else
                                        ProductTaxVM.DEFAULT

    /**
     *  Function for register callbacks, watched for products viewmodels adding
     */
    override fun registerProductsAddedListener(listener: AddedHandler){
        productAddedListeners.add(listener)
    }

    /**
     *  Function for uregister callbacks, watched for products viewmodels adding
     */
    override fun unregisterProductsAddedListener(listener: AddedHandler){
        productAddedListeners.remove(listener)
    }

    /**
     *  Function for register callbacks, watched for errors, occurred in receiving time
     */
    override fun addErrorListener(listener: ErrorHandler){
        errorListeners.add(listener)
    }

    /**
     *  Function for unregister callbacks, watched for errors, occurred in receiving time
     */
    override fun removeErrorListener(listener: ErrorHandler){
        errorListeners.remove(listener)
    }

    /**
     * Function to set callback for running activity when detail information about product received
     */
    override fun setDetailListener(listener: DetailHandler){
        detailListener = listener
    }

    /**
     * Function to reset callback for running activity when detail information about product received
     */
    override fun resetDetailListener(listener: DetailHandler){
        if (detailListener == listener)
            detailListener = {}
    }

    /**
     * Event, raised when product taxes was received
     */
    private fun onProductListAdded(products: List<ProductTaxVM<ProductDetailModel>>){
        productAddedListeners.forEach {
            it(products)
        }
    }

    /**
     *  Function for register callbacks, watched for change filter to full view or contrary
     */
    override fun registerFilterChangedListener(listener: ClearHandler){
        clearListeners.add(listener)
    }

    /**
     *  Function for unregister callbacks, watched for change filter to full view or contrary
     */
    override fun unregisterFilterChangedListener(listener: ClearHandler){
        clearListeners.remove(listener)
    }

    /**
     * Function to get count of products viewmodels
     */
    override fun productsCount() = productsList.size

    /**
     * Event, raised when filter change to full view or view filtered by title-filter
     */
    private fun onFilterChanged(){
        clearListeners.forEach {
            it()
        }
    }

    /**
     * Test function for get stub viewmodels
     */
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

    /**
     * Function to clear list of products viewmodels
     */
    private fun clearProductsCount(){
        productsList.forEach {
            while (it.decQuantity()){}
        }
    }

    /**
     * Mapper to convert list of received pojo to list of viewmodels
     */
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