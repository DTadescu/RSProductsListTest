package shdv.iotdev.rsproductstest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductTaxModel
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM
import shdv.iotdev.rsproductstest.views.ProductDetailView
import shdv.iotdev.rsproductstest.views.ProductTaxView

//private lateinit var frTrans: FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        supportFragmentManager.beginTransaction()
                .add(R.id.container1, ProductTaxView(
                    ProductTaxVM(
                        model = ProductTaxModel()
                    ){
                        supportFragmentManager.beginTransaction()

                            .add(R.id.container2, ProductDetailView(ProductTaxVM(ProductDetailModel(
                                count = ObservableInt(it)
                            ))))
                            .commit()
                    }))
                .commit()
//        supportFragmentManager.beginTransaction()
//            .add(R.id.container, ProductDetailView(ProductTaxVM(ProductDetailModel())))
//            .commit()
    }

    companion object{
        var context:Context = MainActivity()
            private set
    }
}