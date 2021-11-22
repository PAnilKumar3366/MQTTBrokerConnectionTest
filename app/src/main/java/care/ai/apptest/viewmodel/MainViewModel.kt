package care.ai.apptest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import care.ai.apptest.model.WebSite
import care.ai.apptest.repository.WebSitesRepository
import care.ai.apptest.util.Resource

class MainViewModel(private val repository: WebSitesRepository) : ViewModel() {


    var websitesLiveData: MutableLiveData<Resource<MutableList<WebSite>>>? = null

    fun getWebSiteList(): MutableLiveData<Resource<MutableList<WebSite>>> {
        websitesLiveData = repository.getWebSites()
        return websitesLiveData as MutableLiveData<Resource<MutableList<WebSite>>>
    }
}