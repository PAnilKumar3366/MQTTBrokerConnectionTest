package care.ai.apptest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import care.ai.apptest.api.MQTTClientHelper
import care.ai.apptest.repository.WebSitesRepository


class ViewModelFactory(private val apiHelper: MQTTClientHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(WebSitesRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}