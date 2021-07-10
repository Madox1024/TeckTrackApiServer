package com.tecktrack.apiserver

import com.google.gson.Gson
import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.slf4j.LoggerFactory

class Server {

    private val jsonSubject = BehaviorSubject.createDefault("null")
    private val xmlSubject = PublishSubject.create<String>()
    private val log = LoggerFactory.getLogger(javaClass)

    // Spins up Orbits 4 server
    fun startOrbitsServer() {
        val gson = Gson() // Gson() has to be instanced

        val app = Javalin.create { config ->
            config.enableCorsForAllOrigins()
        }.start(7000)

        app.get("/") { ctx ->
            ctx.result(jsonSubject.value)
        }

        app.post("/upload") { ctx ->
            log.debug("xml POST Received")
            xmlSubject.onNext(ctx.body())
        }

        app.config.addStaticFiles("FrontEnd/", Location.EXTERNAL)

        xmlSubject
            .observeOn(Schedulers.computation())
            .map { xml ->
                log.debug("Subject Created")
                xmlHandler(xml)
            }.map { racePair ->
                log.debug("xml Parsing Successful")
                getJsonMasterClass(racePair)
            }.map { jsonMasterClass ->
                log.debug("Data Class Created")
                gson.toJson(jsonMasterClass)
            }.doOnError {
                it.printStackTrace()
            }.retry()
            .subscribe(jsonSubject)
        log.debug("json Created")
    }

    private fun xmlHandler(xmlString: String): Pair<RaceData, MutableMap<String, CarData>> {
        val xmlDoc = xmlStringToDoc(xmlString)
        return Pair(
            raceDataMapToClass(
                getRaceDataMap(
                    xmlDoc
                )
            ), getCarDataMap(xmlDoc)
        )
    }
}