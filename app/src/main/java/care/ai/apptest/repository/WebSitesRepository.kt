package care.ai.apptest.repository

import care.ai.apptest.api.MQTTClientHelper

class WebSitesRepository(private val helper: MQTTClientHelper) {

    fun getWebSites() = helper.getWebSitesList()
}