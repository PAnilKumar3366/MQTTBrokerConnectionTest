package care.ai.apptest.api

import care.ai.apptest.util.MQTTClientOptions
import care.ai.apptest.util.MQTTClientOptions.PASS
import care.ai.apptest.util.MQTTClientOptions.USER
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient

/**
 * MQTTClient is Single Instance Class and this class creates only one Instance
 * */
class MQTTClient private constructor() {
    companion object {
        @Volatile
        private var instance: MQTTClient? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MQTTClient().also { instance = it }
            }
    }

    /**
     * To Get Mqtt Client
     * */
    fun getMQTTClient(): Mqtt3BlockingClient {
        val client = MqttClient.builder()
            .useMqttVersion3()
            .identifier(MQTTClientOptions.IDENTIFIER)
            .serverHost(MQTTClientOptions.HOST)
            .serverPort(8883)
            .simpleAuth()
            .username(USER)
            .password(Charsets.UTF_8.encode(PASS))
            .applySimpleAuth()
            .sslWithDefaultConfig()
            .buildBlocking()
        return client
    }


}