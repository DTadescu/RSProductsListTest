package shdv.iotdev.rsproductstest.utils

import android.widget.ListView

fun ListView.setHeightByContent(){
    if(adapter != null && !adapter.isEmpty){
        layoutParams.apply {
            var totalHeight = 0
            for (i in 0 until adapter.count){
                val listItem = adapter.getView(i, null, this@setHeightByContent)
                listItem.measure(0, 0)
                totalHeight += listItem.measuredHeight
            }
            height = totalHeight + dividerHeight*(adapter.count - 1)
        }
    }
}