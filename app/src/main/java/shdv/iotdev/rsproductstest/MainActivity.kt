package shdv.iotdev.rsproductstest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var view: ProductTaxView
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewmodel = vmProvider


            val viewModel = ProductTaxVM(
                model = ProductDetailModel(),
                detail = this::showDetails
            )
             if (vmProvider.productsList.isEmpty()) {
                vmProvider.productsList.add(viewModel)
            }
            view = ProductTaxView.Builder().apply { setViewModel(vmProvider.productsList[0]) }.build()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, view)
                .commit()


    }


    private fun showDetails(productId: Int){
        if(vmProvider.setCurrentProduct(productId)){
            val detailActivity = Intent(this, DetailActivity::class.java)
            startActivity(detailActivity)
        }

    }
}