package care.ai.apptest.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import care.ai.apptest.MainActivity
import care.ai.apptest.model.WebSite
import care.ai.apptest.util.MQTTClientOptions
import care.ai.apptest.util.MQTTClientOptions.CAREAI_REQUEST_TOPIC
import care.ai.apptest.util.Resource
import care.ai.apptest.util.Status
import com.google.gson.Gson
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient
import org.json.JSONObject
import java.nio.charset.StandardCharsets.UTF_8

/**
 * This is Helper class for MQTTClient. This helper class used to connect the MQTTClient and Subscribe the topic.This class contains Connection method,Subscribe method and Publish Method. Currently this class is subscribe to get_websites topic.
 * */
class MQTTClientHelper(private val topic: String) {
    private final val TAG: String = "MQTTClientHelper"
    var client: Mqtt3BlockingClient
    var websitesLiveData: MutableLiveData<Resource<MutableList<WebSite>>>
    var count: Int

    init {
        client = MQTTClient.getInstance().getMQTTClient();
        websitesLiveData = MutableLiveData<Resource<MutableList<WebSite>>>()
        count = 0;
    }

    fun getWebSitesList(): MutableLiveData<Resource<MutableList<WebSite>>> {
        connectMQTTClient()
        return websitesLiveData
    }

    fun connectMQTTClient() {
        websitesLiveData.value = Resource(Status.LOADING, null, "")
        client.toAsync().connectWith()
            .simpleAuth()
            .username(MQTTClientOptions.USER)
            .password(Charsets.UTF_8.encode(MQTTClientOptions.PASS))
            .applySimpleAuth()
            .send()
            .whenComplete { connAck, throwable ->
                if (throwable != null) {
                    Log.e(
                        MainActivity::class.java.simpleName,
                        "Failed to connect to MQTT broker.",
                        throwable
                    )
                    websitesLiveData.value = Resource(Status.ERROR, null, throwable.message)
                    return@whenComplete
                } else {
                    if (!connAck.returnCode.isError) {
                        Log.d(
                            TAG,
                            "connectMQTTClient: Connection Success Code" + connAck.returnCode.code
                        )
                        subscribe()
                        publish()
                    }

                }

            }
    }

    fun subscribe() {
        client.subscribeWith()
            .topicFilter(MQTTClientOptions.CAREAI_RESPONSE_TOPIC)
            .send()

        client.subscribeWith()
            .topicFilter(MQTTClientOptions.CAREAI_ERROR_TOPIC)
            .send()
    }

    fun publish(): MutableLiveData<Resource<MutableList<WebSite>>> {


        client.toAsync().publishes(MqttGlobalPublishFilter.SUBSCRIBED) { publish ->
            try {
                val topic = publish.topic
                val payload = Charsets.UTF_8.decode(publish.payload.get())

                Log.d(
                    MainActivity::class.java.simpleName,
                    "Topic: ${topic}\n" +
                            "Payload: $payload"
                )
                val result = JSONObject(payload.toString()).getJSONArray("websites")
                val list: MutableList<WebSite> =
                    Gson().fromJson<MutableList<WebSite>>(result.toString())
                Log.d(TAG, " List Size" + list.size);
                if (count == 0) {
                    Log.d(TAG, "onCreate: " + Thread.currentThread())
                    websitesLiveData.postValue(Resource(Status.SUCCESS, data = list, "Success"))
                    // mainAdapter.setWebsites(list)
                    // mBinding.recyclerview.adapter=mainAdapter
                }
                count++
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: ", e)
                websitesLiveData.postValue(Resource(Status.ERROR, null, e.message))
            }
        }

        //publish messages to the topic "careai/mobile/app/request"
        val message = JSONObject().apply {
            put("command", "get_websites")
        }.toString()
        client.publishWith()
            .topic(CAREAI_REQUEST_TOPIC)
            .payload(UTF_8.encode(message))
            .send()


        return websitesLiveData
    }
}