package pe.com.master.machines.myapplication.data.repository.repositoryLocal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pe.com.master.machines.myapplication.data.dataBase.DataBase
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.data.repository.Repository
import javax.inject.Inject

class DataSourceLocal @Inject constructor(dataBase: DataBase) : Repository.DataSourceLocal {

    private val moneyDao = dataBase.moneyDao()

    override suspend fun cleanAllMoney(): Flow<Unit> {
        return flow {
            val result = moneyDao.deleteAll()
            emit(result)
        }
    }

    override suspend fun insertAllMoney(listMoney: List<Money>): Flow<Unit> {
        return flow {
            val result = moneyDao.insertAll(listMoney)
            emit(result)
        }
    }

    override suspend fun getAllMoney(): Flow<List<Money>> {
        return flow {
            val result = moneyDao.getAllMoney()
            emit(result)
        }
    }

}