import com.google.gson.Gson
import io.javalin.Javalin

fun main() {
    startJavalinServer()
}

// Spins up web server
fun startJavalinServer() {
    val gson = Gson() // Gson() has to be instanced
    
    val app = Javalin.create { config ->
        config.enableCorsForAllOrigins()
    }.start(7000)

    var data:JsonMasterClass? = null

    app.get("/") { ctx ->
        val jsonData = gson.toJson(data) // converting SampleData Class into a json
        ctx.result(jsonData) // posting jsonData as the result for ctx, this is what you get when you visit http://localhost:7000/
    }
    app.post("/upload") { ctx -> // declaring http://localhost:7000/upload for posting xml
        val racePair = xmlHandler(ctx.body())
        data = getJsonMasterClass(racePair)
    }

}

fun xmlHandler(xmlString: String): Pair<RaceData, MutableMap<String, CarData>> {
    val xmlDoc = xmlStringToDoc(xmlString)
    return Pair(raceDataMapToClass(getRaceDataMap(xmlDoc)), getCarDataMap(xmlDoc))
}
