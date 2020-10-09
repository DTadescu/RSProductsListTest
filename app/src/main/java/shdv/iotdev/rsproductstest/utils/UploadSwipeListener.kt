package shdv.iotdev.rsproductstest.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import shdv.iotdev.rsproductstest.viewmodels.base.IProductsListVM
import kotlin.math.abs

/**
 * onTouchListener, that run data upload when "top-bottom" touching
 */
class UploadSwipeListener(
    activity: Activity,
    private val provider: IProductsListVM<*>
): View.OnTouchListener {
    private val dm = activity.resources.displayMetrics
    private val DISTANCE = (200*dm.densityDpi/160.0 + 0.5).toInt()
    private var downY: Float? = null
    private var downViewY: Float? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v?.onTouchEvent(event)
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
                downViewY = if (v != null && v is ScrollView){
                    v.scrollY.toFloat()
                }
                else
                     v?.y
                return false
            }
            MotionEvent.ACTION_UP -> {
                val upViewY = if (v != null && v is ScrollView){
                    v.scrollY.toFloat()
                } else v?.y

                val upY = event.y

                val deltaViewY = upViewY?.let { up ->
                    downViewY?.let {
                        up - it
                    }
                }?: 100.0F

                val deltaY = upY - (downY?:upY)

                if (abs(deltaViewY) < 10 && abs(deltaY) > DISTANCE && deltaY > 0)
                    onTopToBottomSwipe()

                return false
            }
            else -> return false

        }
    }

    private fun onTopToBottomSwipe(){
        provider.update()
    }
}