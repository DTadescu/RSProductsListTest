package shdv.iotdev.rsproductstest.utils

import android.graphics.Rect
import android.util.Log
import android.widget.ScrollView
import kotlinx.coroutines.delay
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM
import shdv.iotdev.rsproductstest.views.ProductTaxView

class AutoLoader(
    var scrollView: ScrollView?,
    var fragment: ProductTaxView?,
    var provider: IProductsListVM<*>
) {
    private var isRunning = false
    private var scrollBounds = Rect()

    suspend fun start(){
 //       Log.d("LOADER", "Start autoloading")
        isRunning = true
        while (isRunning){
//            Log.d("LOADER", "autoloading is running")
            delay(1000)
            scrollView?.getHitRect(scrollBounds)
            if (!scrollBounds.isEmpty){
//                Log.d("LOADER", "Bounds is not empty")
//                Log.d("LOADER", "${scrollBounds.left}, ${scrollBounds.top}, ${scrollBounds.right}, ${scrollBounds.bottom}")
                if (fragment?.checkViewOnScreen(scrollBounds) == true){
 //                   Log.d("LOADER", "View in bounds")
                    provider.getProducts()
                }
//                else if (fragment == null)
//                    Log.d("LOADER", "Fragment is null")
            }
        }
//        Log.d("LOADER", "Stop autoloading")
    }

    fun stop(){
        isRunning = false
    }

}