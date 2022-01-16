package pe.com.master.machines.myapplication.data.repository

import kotlinx.coroutines.flow.Flow
import pe.com.master.machines.myapplication.data.entity.Money

interface Repository {

    interface DataSourceLocal {

        suspend fun cleanAllMoney(): Flow<Unit>

        suspend fun insertAllMoney(listMoney: List<Money>): Flow<Unit>

        suspend fun getAllMoney(): Flow<List<Money>>

    }

    interface DataSourceRemote {

    }
}