package guilherme.ruben.timeowner.data.events

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val eventName: String,
    val eventDesc: String,
    val date: Long,
    var done: Boolean,
    val color: Int
): Parcelable