package guilherme.ruben.timeowner.data.recurrents

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecurrentItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItem(recurrentItem: RecurrentItem)

    @Update
    suspend fun updateItem(item: RecurrentItem)

    @Delete
    suspend fun deleteItem(item: RecurrentItem)

    @Query("SELECT * FROM recurrent_item_table ORDER BY itemName ASC")
    fun readAllData(): LiveData<List<RecurrentItem>>

    @Query("SELECT * FROM recurrent_item_table WHERE id =:id")
    fun readSingleItem(id: Int): LiveData<RecurrentItem>
}