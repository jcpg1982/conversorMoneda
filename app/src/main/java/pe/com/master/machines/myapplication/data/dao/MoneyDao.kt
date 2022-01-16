package pe.com.master.machines.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.jetbrains.annotations.NotNull
import pe.com.master.machines.myapplication.data.entity.Money

@Dao
interface MoneyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(@NotNull entities: List<Money>)

    @Query("DELETE  FROM Money")
    fun deleteAll()

    @Query("SELECT *  FROM Money")
    fun getAllMoney(): List<Money>
}