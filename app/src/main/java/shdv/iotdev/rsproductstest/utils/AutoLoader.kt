package shdv.iotdev.rsproductstest.utils

import android.graphics.Rect
import android.util.Log
import android.widget.ScrollView
import kotlinx.coroutines.delay
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM
import shdv.iotdev.rsproductstest.views.ProductTaxView

/**
 * Service, that run receiving product information if last product's card is on the screen
 */
class AutoLoader(
    var scrollView: ScrollView?,
    var fragment: ProductTaxView?,
    var provider: IProductsListVM<*>
) {
    private var isRunning = false
    private var scrollBounds = Rect()

    private val LOGGER = "LOADER"

    suspend fun start(){
        Log.d(LOGGER, "Start autoloading")
        isRunning = true
        while (isRunning){
            delay(1000)
            scrollView?.getHitRect(scrollBounds)
            if (!provider.checkBusy().get() && !scrollBounds.isEmpty){
                if (fragment?.checkViewOnScreen(scrollBounds) == true){
                    provider.getProducts()
                }
            }
        }
        Log.d(LOGGER, "Stop autoloading")
    }

    fun stop(){
        isRunning = false
        Log.d(LOGGER, "Set to stop autoloading")
    }

}