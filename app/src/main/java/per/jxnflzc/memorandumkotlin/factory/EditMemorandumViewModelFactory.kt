package per.jxnflzc.memorandumkotlin.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.viewmodel.edit.EditMemorandumViewModel

class EditMemorandumViewModelFactory(private val memorandum: Memorandum) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditMemorandumViewModel(memorandum) as T
    }
}