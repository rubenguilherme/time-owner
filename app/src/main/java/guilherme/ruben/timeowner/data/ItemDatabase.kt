package guilherme.ruben.timeowner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import guilherme.ruben.timeowner.data.events.Event
import guilherme.ruben.timeowner.data.events.EventDao
import guilherme.ruben.timeowner.data.items.Item
import guilherme.ruben.timeowner.data.items.ItemDao
import guilherme.ruben.timeowner.data.recurrents.RecurrentItem
import guilherme.ruben.timeowner.data.recurrents.RecurrentItemDao

@Database(entities = [Item::class, RecurrentItem::class, Event::class], version = 1, exportSchema = false)
abstract class ItemDatabase: RoomDatabase() {

    abstract fun itemDao(): ItemDao
    abstract fun recurrentItemDao(): RecurrentItemDao
    abstract fun eventDao(): EventDao

    companion object{
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: Context): ItemDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}