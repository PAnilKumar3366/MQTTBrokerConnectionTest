package care.ai.apptest.util

object MQTTClientOptions {

    const val IDENTIFIER = "client-android"
    const val HOST = "0f892689e6f9436490ee016d481d6f0e.s1.eu.hivemq.cloud"
    const val USER = "care.ai"
    const val PASS = "Mobile@ppT3st"

    const val CAREAI_REQUEST_TOPIC = "careai/mobile/app/request"
    const val CAREAI_RESPONSE_TOPIC = "careai/mobile/app/response"
    const val CAREAI_ERROR_TOPIC = "careai/mobile/app/error"
}