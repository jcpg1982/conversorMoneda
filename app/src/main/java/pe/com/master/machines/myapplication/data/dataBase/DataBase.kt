package pe.com.master.machines.myapplication.data.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pe.com.master.machines.myapplication.data.dao.MoneyDao
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.helpers.Constants

@Database(
    entities = [Money::class], version = 1, exportSchema = false
)
abstract class DataBase : RoomDatabase() {

    abstract fun moneyDao(): MoneyDao

    companion object {

        @Volatile
        private var DATA_BASE: DataBase? = null

        fun getDataBase(context: Context, scope: CoroutineScope): DataBase {
            return DATA_BASE ?: synchronized(this) {
                val instance by lazy {
                    Room.databaseBuilder(
                        context,
                        DataBase::class.java,
                        Constants.NAME_DATA_BASE
                    )
                        .addCallback(DatabaseCallback(scope))
                        .build()
                }
                DATA_BASE = instance
                instance
            }
        }

        private class DatabaseCallback(val scope: CoroutineScope) : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                DATA_BASE?.let {
                    scope.launch {

                    }
                }

            }
        }

    }
}