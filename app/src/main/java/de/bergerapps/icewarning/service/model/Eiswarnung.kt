package de.bergerapps.icewarning.service.model


class Eiswarnung {
    var success: Boolean = false
    lateinit var message: String
    var code: Int = 0
    var callsLeft: Int = 0
    var callsDailyLimit: Int = 0
    var callsResetInSeconds: Int = 0
    lateinit var result: EiswarnungResult

    inner class EiswarnungResult {
        lateinit var requestDate: String
        var forecastId = 0
        lateinit var forecastText: String
        lateinit var forecastCity: String
        lateinit var forecastDate: String
    }

}