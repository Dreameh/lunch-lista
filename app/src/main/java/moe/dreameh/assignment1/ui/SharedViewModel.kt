package moe.dreameh.assignment1.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import moe.dreameh.assignment1.Advice
import moe.dreameh.assignment1.room.AdviceDatabase
import moe.dreameh.assignment1.room.AdviceRepository
import kotlin.coroutines.CoroutineContext

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)


    private val repository: AdviceRepository
    val bigList: MutableLiveData<MutableList<Advice>>

    init {
        val adviceDao = AdviceDatabase.getDatabase(application).adviceDao()
        repository = AdviceRepository(adviceDao)
        bigList = repository.allAdvices
    }

    private val list = mutableListOf(
            Advice(
                    1,
                    "Han Kolo",
                    "Lifestyle",
                    "I still get a funny feeling about that old man and the kid. I'm not sure what it is about them, but they're trouble"
            ),
            Advice(
                    2,
                    "Obi-wan Henobi",
                    "Technology",
                    "These aren't the droids you're looking for."
            ),
            Advice(
                    3,
                    "Darth Vaber",
                    "Miscellaneous",
                    "Impressive. Most impressive. Obi-Wan has taught you well. You have controlled your fear. Now, release your anger. Only your hatred can destroy me."
            )
    )

    private val adviceList by lazy {
        MutableLiveData<MutableList<Advice>>().apply { postValue(list) }
    }

    fun getAdvices(): MutableLiveData<MutableList<Advice>> {
        return adviceList
    }

    fun addNewAdvice(advice: Advice) {
        list.add(advice)
        adviceList.value = list
    }

    fun filterAdvice(category: String) = list.filter { it.category == category }
}
