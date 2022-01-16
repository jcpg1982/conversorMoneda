package pe.com.master.machines.myapplication.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import pe.com.master.machines.myapplication.data.dataBase.DataBase
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal
import javax.inject.Singleton

@Module
class SqliteModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context): DataBase {
        val scope = CoroutineScope(Dispatchers.IO)
        return DataBase.getDataBase(context, scope)
    }

    @Provides
    @Singleton
    fun provideDataSourceLocal(dataBase: DataBase): DataSourceLocal = DataSourceLocal(dataBase)

}