package shdv.iotdev.rsproductstest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM
import shdv.iotdev.rsproductstest.views.ProductTaxView

//private lateinit var frTrans: FragmentTransaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        supportFragmentManager.beginTransaction()
                .add(R.id.container, ProductTaxView(ProductTaxVM.DEFAULT))
                .commit()
    }

    companion object{
        var context:Context = MainActivity()
            private set
    }
}