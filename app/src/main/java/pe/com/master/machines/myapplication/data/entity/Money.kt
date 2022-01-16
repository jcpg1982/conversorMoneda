package pe.com.master.machines.myapplication.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Money(
    @PrimaryKey
    var idMoney: Int,
    var nameMoney: String,
    var codMoney: String,
    var country: String,
    var buy: Double,
    var sell: Double,
    var flag: String
) : Parcelable