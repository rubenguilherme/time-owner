package guilherme.ruben.timeowner.data.recurrents

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "recurrent_item_table")
data class RecurrentItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val itemName: String,
    val itemDesc: String,
    // String of type: "0110010" where 1 = day to add and 0 = day without item
    // Order is "SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY"
    val days: String,
    val priority: Int
): Parcelable