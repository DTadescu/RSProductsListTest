package shdv.iotdev.rsproductstest

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import io.reactivex.Completable
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shdv.iotdev.rsproductstest.databinding.ActivityMainBinding
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.utils.AutoLoader
import shdv.iotdev.rsproductstest.utils.UploadSwipeListener
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductsListVM
import shdv.iotdev.rsproductstest.views.DetailActivity
import shdv.iotdev.rsproductstest.views.ProductTaxView
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vmProvider = ProductsListVM
    private var isInit = false
    private var lastFragment: ProductTaxView? = null
    private var loader: AutoLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AAAA", "###onCreate")
        super.onCreate(savedInstanceState)
        isInit = savedInstanceState?.getBoolean("Isinit")?:false
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewmodel = vmProvider
        Observable.create(ObservableOnSubscribe<String>{subscriber ->
            search_edit.doOnTextChanged { text, _, _, _ -> subscriber.onNext(text.toString())}
        }).map { text -> text.toLowerCase(Locale.ROOT) }
            .debounce(2000, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotBlank() }
            .subscribe{ text ->
                vmProvider.onSearchEditChanged(text)
            }

        vmProvider.registerFilterChangedListener(this::clearFragments)
        loader = AutoLoader(fragments_scroller, lastFragment, vmProvider)
        fragments_scroller.setOnTouchListener(UploadSwipeListener(this, vmProvider))
    }


    override fun onResume() {
        Log.d("AAAA", "###onResume")

        super.onResume()
        registerHandlers()
        GlobalScope.launch(Dispatchers.Main){
            loader?.start()
        }
        if(!isInit){
            vmProvider.getProducts()
            isInit = true
        }
//        else{
//            Log.d("ONRESUME", "${supportFragmentManager.fragments.size}")
//            for (i in supportFragmentManager.fragments.indices){
//                if (supportFragmentManager.fragments[i] is ProductTaxView)
//                    (supportFragmentManager.fragments[i] as ProductTaxView).setViewModel(ProductsListVM.getVM(i))
//            }
//        }


    }

    override fun onPause() {
        Log.d("AAAA", "###onPause")
        unregisterHandlers()
        super.onPause()

    }


    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("AAAA", "###onSaveInstanceState")
        loader?.stop()
        outState.putBoolean("Isinit", isInit)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("AAAA", "###onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        isInit = savedInstanceState.getBoolean("Isinit")

        Log.d("Restore", isInit.toString())
    }

    /**
     * Event, override for turn to view all products without filter when filter is set
     */
    override fun onBackPressed() {
        if (vmProvider.filterStatus.get())
            vmProvider.clearFilterStatus()
        else
        super.onBackPressed()
    }

    /**
     * Function to run activity for detail product view
     */
    private fun showDetails(){
            if (vmProvider.checkBusy().get()) return
            val detailActivity = Intent(this, DetailActivity::class.java)
            startActivity(detailActivity)
    }

    private fun showError(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Function to add fragments with product info to screen via [supportFragmentManager]
     * and set last added fragment to [loader]
     */
    private fun addViewToProductList(viewmodelList: List<ProductTaxVM<ProductDetailModel>>){

                supportFragmentManager.beginTransaction().apply {
                    viewmodelList.forEach{
                        add(R.id.container1, ProductTaxView().apply { setViewModel(it) })
                    }
                }.commit()


        GlobalScope.launch(Dispatchers.Main){
            while (supportFragmentManager.fragments.size < vmProvider.productsCount()){
                delay(100)
            }
            try {
                for (i in supportFragmentManager.fragments.size-1 downTo 0){
                    if (supportFragmentManager.fragments[i] is ProductTaxView){
                        lastFragment = supportFragmentManager.fragments[i] as ProductTaxView
                        loader?.fragment = lastFragment
                        return@launch
                    }
                }

            }
            catch (e: Exception){
                e.printStackTrace()
                Log.d("FAILURE", "Fail to get or cast last fragment.")
                Log.d("FAILURE", "${supportFragmentManager.fragments.lastIndex}")
            }
        }

    }

    /**
     * Function to clear all added fragments
     * @TODO: maybe need checking to matchable with product fragment
     */
    private fun clearFragments(){
        supportFragmentManager.beginTransaction().apply {
            for (fragment in supportFragmentManager.fragments)
                remove(fragment)
        }.commit()
       // vmProvider.registerProductsAddedListener(this::addViewToProductList)
        setKeyboardVisible(vmProvider.filterStatus.get())
    }

    /**
     *  Function to set/remove keyboard in/from the screen.
     *  [need] = true - set, false - remove
     */
    private fun setKeyboardVisible(need: Boolean) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!need)
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        else
            imm.showSoftInput(search_edit, 0)
    }

    /**
     * Function for register callbacs in viewmodel
     */
    private fun registerHandlers(){
        vmProvider.registerProductsAddedListener(this::addViewToProductList)
        vmProvider.registerFilterChangedListener(this::clearFragments)
        vmProvider.setDetailListener(this::showDetails)
        vmProvider.addErrorListener(this::showError)
    }

    /**
     * Function for remove callbacs from viewmodel
     */
    private fun unregisterHandlers(){
        vmProvider.unregisterProductsAddedListener(this::addViewToProductList)
        vmProvider.unregisterFilterChangedListener(this::clearFragments)
        vmProvider.resetDetailListener(this::showDetails)
        vmProvider.removeErrorListener(this::showError)
    }

}