package shdv.iotdev.rsproductstest.viewmodels.base

typealias ClearHandler = () -> Unit

interface IFilterable {

    fun setFilterStatus()

    fun clearFilterStatus()

    fun unregisterFilterChangedListener(listener: ClearHandler)

    fun registerFilterChangedListener(listener: ClearHandler)

    fun onSearchEditChanged(s: String)

}