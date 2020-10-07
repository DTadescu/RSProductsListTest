package shdv.iotdev.rsproductstest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import shdv.iotdev.rsproductstest.databinding.ActivityMainBinding
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductsListVM
import shdv.iotdev.rsproductstest.views.DetailActivity
import shdv.iotdev.rsproductstest.views.ProductTaxView



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vmProvider = ProductsListVM
    private var viewList: MutableList<ProductTaxView> = mutableListOf()
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AAAA", "###onCreate")
        super.onCreate(savedInstanceState)
        isInit = savedInstanceState?.getBoolean("Isinit")?:false
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewmodel = vmProvider.apply { detailAction = this@MainActivity::showDetails }
        vmProvider.registerFilterChangedListener(this::clearFragments)
    }

    override fun onResume() {
        Log.d("AAAA", "###onResume")

        super.onResume()
        if(!isInit){
            vmProvider.registerProductsAddedListener(this::addViewToProductList)

            vmProvider.getProducts()
            isInit = true
        }
        else
            for (i in supportFragmentManager.fragments.indices){
                if (supportFragmentManager.fragments[i] is ProductTaxView)
                    (supportFragmentManager.fragments[i] as ProductTaxView).setViewModel(ProductsListVM.getVM(i))
            }

    }

    override fun onPause() {
        Log.d("AAAA", "###onPause")
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
    }

    private fun clearFragments(){
        supportFragmentManager.beginTransaction().apply {
            for (fragment in supportFragmentManager.fragments)
                remove(fragment)
        }.commit()
        vmProvider.registerProductsAddedListener(this::addViewToProductList)
    }
}