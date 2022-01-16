package pe.com.master.machines.myapplication.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal
import pe.com.master.machines.myapplication.data.result.ResultData

class ViewModelLive(
    private val dataSourceLocal: DataSourceLocal,
) : ViewModel() {

    private val _guardarMoney: MutableStateFlow<ResultData<Unit>> =
        MutableStateFlow(ResultData.First)
    val guardarMoney: StateFlow<ResultData<Unit>> = _guardarMoney

    private val _getAllMoney: MutableStateFlow<ResultData<List<Money>>> =
        MutableStateFlow(ResultData.First)
    val getAllMoney: StateFlow<ResultData<List<Money>>> = _getAllMoney

    fun guardarMoney(listMoney: List<Money>) {
        viewModelScope.launch {
            dataSourceLocal.cleanAllMoney()
            dataSourceLocal.insertAllMoney(listMoney)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _guardarMoney.value = ResultData.Loading("Guardando monedas")
                }
                .catch { e ->
                    _guardarMoney.value = ResultData.Error(e)
                }
                .collect {
                    _guardarMoney.value = ResultData.Success(it)
                }
        }
    }

    fun getAllMoney() {
        viewModelScope.launch {
            dataSourceLocal.getAllMoney()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _getAllMoney.value = ResultData.Loading("Guardando monedas")
                }
                .catch { e ->
                    _getAllMoney.value = ResultData.Error(e)
                }
                .collect {
                    _getAllMoney.value = ResultData.Success(it)
                }
        }
    }

}