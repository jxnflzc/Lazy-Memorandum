package per.jxnflzc.memorandumkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import per.jxnflzc.memorandumkotlin.model.Memorandum

class EditViewModel () : ViewModel() {
    var memorandum = MutableLiveData<Memorandum>()
}