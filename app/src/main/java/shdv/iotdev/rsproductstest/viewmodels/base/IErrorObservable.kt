package shdv.iotdev.rsproductstest.viewmodels.base


typealias ErrorHandler = (message: String) -> Unit

interface IErrorObservable {

    fun removeErrorListener(listener: ErrorHandler)

    fun addErrorListener(listener: ErrorHandler)
}