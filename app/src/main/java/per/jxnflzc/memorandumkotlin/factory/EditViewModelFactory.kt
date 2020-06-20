package per.jxnflzc.memorandumkotlin.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.viewmodel.EditViewModel

class EditViewModelFactory(private val memorandum: Memorandum) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditViewModel(memorandum) as T
    }
}