package guilherme.ruben.timeowner.data.recurrents

import androidx.lifecycle.LiveData

class RecurrentItemRepository(private val recurrentItemDao: RecurrentItemDao) {

    val readAllData: LiveData<List<RecurrentItem>> = recurrentItemDao.readAllData()

    fun readSingleItem(id: Int): LiveData<RecurrentItem> = recurrentItemDao.readSingleItem(id)

    suspend fun addItem(item: RecurrentItem){
        recurrentItemDao.addItem(item)
    }

    suspend fun updateItem(item: RecurrentItem) {
        recurrentItemDao.updateItem(item)
    }

    suspend fun deleteItem(item: RecurrentItem) {
        recurrentItemDao.deleteItem(item)
    }

}