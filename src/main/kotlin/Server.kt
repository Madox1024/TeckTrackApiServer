import com.google.gson.Gson
import io.javalin.Javalin
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class Server {

    private val jsonSubject = BehaviorSubject.createDefault("null")
    private val xmlSubject = PublishSubject.create<String>()

    // Spins up web server
    fun startJavalinServer() {
        val gson = Gson() // Gson() has to be instanced

        val app = Javalin.create { config ->
            config.enableCorsForAllOrigins()
        }.start(7000)

        app.get("/") { ctx ->
            ctx.result(jsonSubject.value)
        }
        app.post("/upload") { ctx ->
            xmlSubject.onNext(ctx.body())
        }
        xmlSubject
            .observeOn(Schedulers.computation())
            .map { xml ->
                xmlHandler(xml) // create subject for xml handler and wrap in
            }.map { racePair ->
                getJsonMasterClass(racePair)
            }.map { jsonMasterClass ->
                gson.toJson(jsonMasterClass)
            }.doOnError {
                it.printStackTrace()
            }.retry()
            .subscribe(jsonSubject)
    }

    private fun xmlHandler(xmlString: String): Pair<RaceData, MutableMap<String, CarData>> {
        val xmlDoc = xmlStringToDoc(xmlString)
//        sleep(100) // simulated latency
        return Pair(raceDataMapToClass(getRaceDataMap(xmlDoc)), getCarDataMap(xmlDoc))
    }
}