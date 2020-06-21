package per.jxnflzc.memorandumkotlin.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import per.jxnflzc.memorandumkotlin.model.Catalog
import per.jxnflzc.memorandumkotlin.model.Memorandum
import per.jxnflzc.memorandumkotlin.viewmodel.edit.EditCatalogViewModel
import per.jxnflzc.memorandumkotlin.viewmodel.edit.EditMemorandumViewModel

class EditCatalogViewModelFactory(private val catalog: Catalog) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditCatalogViewModel(catalog) as T
    }
}