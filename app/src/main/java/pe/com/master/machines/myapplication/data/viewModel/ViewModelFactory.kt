package pe.com.master.machines.myapplication.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal

class ViewModelFactory(
    private val dataSourceLocal: DataSourceLocal,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelLive::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelLive(dataSourceLocal) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}