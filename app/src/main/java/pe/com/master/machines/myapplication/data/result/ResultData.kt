package pe.com.master.machines.myapplication.data.result

sealed class ResultData<out T> {

    object First : ResultData<Nothing>()

    data class Loading(val message: String) : ResultData<Nothing>()

    object Empty : ResultData<Nothing>()

    data class Error(val exception: Throwable) : ResultData<Nothing>()

    data class Success<out R>(val data: R) : ResultData<R>()

}
