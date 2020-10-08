package shdv.iotdev.rsproductstest.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM
import kotlin.math.abs

class UploadSwipeListener(
    private val activity: Activity,
    private val provider: IProductsListVM<*>
): View.OnTouchListener {
    private val dm = activity.resources.displayMetrics
    private val DISTANCE = (150*dm.densityDpi/160.0 + 0.5).toInt()
    private var downY: Float? = null
    private var downViewY: Float? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
                downViewY = v?.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val upViewY = v?.y
                val upY = event.y

                val deltaY = upY - (downY?:upY)
                val deltaViewY = upViewY?.let { up ->
                    downViewY?.let {
                        up - it
                    }
                }?: 100.0F

                if (abs(deltaViewY) < 10 && abs(deltaY) > DISTANCE && deltaY > 0)
                    onTopToBottomSwipe()

                return true
            }
            else -> return true

        }
    }

    private fun onTopToBottomSwipe(){
        provider.update()
    }
}