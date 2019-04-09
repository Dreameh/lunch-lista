package moe.dreameh.assignment1.ui

import android.annotation.TargetApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import moe.dreameh.assignment1.Advice


class SharedViewModel : ViewModel() {


    val list : MutableList<Advice> = ArrayList()

    val adviceList: MutableLiveData<MutableList<Advice>> by lazy {
        MutableLiveData<MutableList<Advice>>().also { loadAdvices() }
    }

    fun getAdvices(): MutableList<Advice>? {
        return adviceList.value
    }

    fun setNewAdvice(advice: Advice) {
            adviceList.value?.add(advice)
    }

    fun filterAdvices(category: String): MutableList<Advice> {
        val filteredList:  MutableList<Advice> = ArrayList()
        for(advice in list) {
            if (advice.category == category) {
                filteredList.add(advice)
            }
        }
        return filteredList
    }

    fun loadAdvices() {
        list.add(Advice("Han Kolo", "Lifestyle", "I still get a funny feeling about that old man and the kid. I'm not sure what it is about them, but they're trouble"))
        list.add(Advice("Obi-wan Henobi", "Technology", "These aren't the droids you're looking for."))
        list.add(Advice("Darth Vaber", "Miscellaneous", "Impressive. Most impressive. Obi-Wan has taught you well. You have controlled your fear. Now, release your anger. Only your hatred can destroy me."))

        adviceList.value = list
    }
}
