package moe.dreameh.assignment1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import moe.dreameh.assignment1.Advice

class SharedViewModel : ViewModel() {

    private val list = mutableListOf(
            Advice(
                    "Han Kolo",
                    "Lifestyle",
                    "I still get a funny feeling about that old man and the kid. I'm not sure what it is about them, but they're trouble"
            ),
            Advice(
                    "Obi-wan Henobi",
                    "Technology",
                    "These aren't the droids you're looking for."
            ),
            Advice(
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
