package shdv.iotdev.rsproductstest.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import shdv.iotdev.rsproductstest.R
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductsListVM

class DetailActivity : AppCompatActivity() {

    private val vmProvider = ProductsListVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = vmProvider.getCurrentProduct().model.name
            supportFragmentManager.beginTransaction()
                .replace(R.id.contaner_detail,
                    ProductDetailView.Builder().apply {
                        setViewModel(vmProvider.getCurrentProduct()) }.build())
                .commit()



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->
                onBackPressed()
        }
        return true

    }

}