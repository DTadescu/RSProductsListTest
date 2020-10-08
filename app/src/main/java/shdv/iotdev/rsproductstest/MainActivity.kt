package shdv.iotdev.rsproductstest

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
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

        binding.viewmodel = vmProvider.apply { detailAction = this@MainActivity::showDetails }
        //binding.searchEdit.doOnTextChanged{ text, start, before, count -> vmProvider.onSearchEditChanged(text, start, before, count) }
        vmProvider.registerFilterChangedListener(this::clearFragments)
        loader = AutoLoader(fragments_scroller, lastFragment, vmProvider)
        //fragments_scroller.setOnTouchListener(UploadSwipeListener(this, vmProvider))
    }


    override fun onResume() {
        Log.d("AAAA", "###onResume")

        super.onResume()
        GlobalScope.launch(Dispatchers.Main){
            loader?.start()
        }
        if(!isInit){
            vmProvider.registerProductsAddedListener(this::addViewToProductList)

            vmProvider.getProducts()
            isInit = true
        }
        else{
            Log.d("ONRESUME", "${supportFragmentManager.fragments.size}")
            for (i in supportFragmentManager.fragments.indices){
                if (supportFragmentManager.fragments[i] is ProductTaxView)
                    (supportFragmentManager.fragments[i] as ProductTaxView).setViewModel(ProductsListVM.getVM(i))
            }
        }


    }

    override fun onPause() {
        Log.d("AAAA", "###onPause")
        loader?.stop()
        super.onPause()

    }

    override fun onStart() {

        Log.d("AAAA", "###onStart")
        super.onStart()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("AAAA", "###onSaveInstanceState")
        outState.putBoolean("Isinit", isInit)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d("AAAA", "###onRestoreInstanceState")
        super.onRestoreInstanceState(savedInstanceState)
        isInit = savedInstanceState.getBoolean("Isinit")

        Log.d("Restore", isInit.toString())
    }

    override fun onDestroy() {
        Log.d("AAAA", "###onDestroy")
        super.onDestroy()
        vmProvider.unregisterProductsAddedListener(this::addViewToProductList)
        vmProvider.unregisterFilterChangedListener(this::clearFragments)

    }


    private fun showDetails(productId: Int){
        if(vmProvider.setCurrentProduct(productId)){
            val detailActivity = Intent(this, DetailActivity::class.java)
            startActivity(detailActivity)
        }

    }

    private fun addViewToProductList(viewmodelList: List<ProductTaxVM<ProductDetailModel>>){

                supportFragmentManager.beginTransaction().apply {
                    viewmodelList.forEach{
                        add(R.id.container1, ProductTaxView().apply { setViewModel(it) })
                    }
                }.commit()

        GlobalScope.launch(Dispatchers.Main){
            while (supportFragmentManager.fragments.size < vmProvider.productsCount()){
                delay(100)
                Log.d("FRAGMENT",supportFragmentManager.fragments.size.toString())
                Log.d("FRAGMENT", vmProvider.productsCount().toString())
            }
            try {
                for (i in supportFragmentManager.fragments.size-1 downTo 0){
                    if (supportFragmentManager.fragments[i] is ProductTaxView){
                        lastFragment = supportFragmentManager.fragments[i] as ProductTaxView
                        loader?.fragment = lastFragment
                        Log.d("FRAGMENT", lastFragment.toString())
                        return@launch
                    }
                }

                Log.d("FRAGMENT", supportFragmentManager.fragments.last().toString())
            }
            catch (e: Exception){
                e.printStackTrace()
                Log.d("FAILURE", "Fail to get or cast last fragment.")
                Log.d("FAILURE", "${supportFragmentManager.fragments.lastIndex}")
            }
        }

    }

    private fun clearFragments(){
        Log.d("CLEAR", "${supportFragmentManager.fragments.lastIndex}")
        supportFragmentManager.beginTransaction().apply {
            for (fragment in supportFragmentManager.fragments)
                remove(fragment)
        }.commit()
        vmProvider.registerProductsAddedListener(this::addViewToProductList)
    }

}