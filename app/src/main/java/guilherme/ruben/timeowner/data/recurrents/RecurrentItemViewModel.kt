package guilherme.ruben.timeowner.data.recurrents

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import guilherme.ruben.timeowner.data.ItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecurrentItemViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<RecurrentItem>>
    private val repository: RecurrentItemRepository

    init {
        val recurrentItemDao = ItemDatabase.getDatabase(application).recurrentItemDao()
        repository = RecurrentItemRepository(recurrentItemDao)
        readAllData = repository.readAllData

    }

    fun readSingleItem(id : Int) : LiveData<RecurrentItem> = repository.readSingleItem(id)

    fun addItem(item: RecurrentItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(item)
        }
    }

    fun updateItem(item: RecurrentItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: RecurrentItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item)
        }
    }
}