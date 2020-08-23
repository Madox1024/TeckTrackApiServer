package com.tecktrack.apiserver
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants.NODESET
import javax.xml.xpath.XPathFactory


// File contains:
// functions to get, parse and convert data from xml and csv


// returns orbits xml document object
fun xmlStringToDoc(xmlString: String): Document {
    val dbFactory = DocumentBuilderFactory.newInstance()
    val dBuilder = dbFactory.newDocumentBuilder()
    val xmlInput = InputSource(StringReader(xmlString)) // xmlFile.readText() returns a string
    return dBuilder.parse(xmlInput)
}

// converts above document to a map of race data, IllegalArgumentException if cannot find type attribute or NOMATCH
fun getRaceDataMap(doc: Document): MutableMap<String, String> {
    val xpFactory = XPathFactory.newInstance()
    val xPath = xpFactory.newXPath()

    val valueXPath = "/resultspage/label"
    val labelNodeList = xPath.evaluate(valueXPath, doc, NODESET) as NodeList
    val xmlMap: MutableMap<String, String> = mutableMapOf()

    for (i in 0 until labelNodeList.length) { // Node lists aren't iterable (thanks .js)
        val type = labelNodeList.item(i).attributes.getNamedItem("type") ?: throw IllegalArgumentException(
            "Critical Error! Bad xml; no type attribute found")
        // i think this will still throw if XPath returns No Match?
        val text = labelNodeList.item(i).textContent
        xmlMap[type.toString().removePrefix("type=\"").removeSuffix("\"")] = text
    }
    return xmlMap
}

// Takes MutableMap from getRaceData and returns a com.tecktrack.leaderboard.com.tecktrack.apiserver.RaceData Obj, IllegalArgumentException if null values given
fun raceDataMapToClass(xmlMap: MutableMap<String, String>): RaceData {
    try {
        return RaceData(
            raceTime = parseTimestamp(xmlMap["racetime"] as String)
                .toMilliseconds(),
            timeOfDay = parseTimestamp(xmlMap["timeofday"] as String)
                .toMilliseconds(),

            trackLength = xmlMap["tracklength"]!!.safeToInt(),
            leaderDiff = xmlMap["leadermargin"]!!.safeToInt(),
            laps = xmlMap["laps"]!!.safeToInt(),

            eventName = xmlMap["eventname"]!!,
            groupName = xmlMap["groupname"]!!,
            runName = xmlMap["runname"]!!,
            trackName = xmlMap["trackname"]!!,
            runType = xmlMap["runtype"]!!,
            bestLapBy = xmlMap["bestlapby"]!!,
            bestOverallLapTime = xmlMap["bestlaptime"]!!,
            flagStatus = xmlMap["flag"]!!,
            leader = xmlMap["leader"]!!
        )
    } catch(ignore: NullPointerException) { // this shouldn't be thrown unless something is very wrong with the xml
        throw IllegalArgumentException("Critical Error! Bad xml; missing Race Data or type key(s)")}
}

//  Takes xml doc and returns a mutable map of [regnum : com.tecktrack.leaderboard.com.tecktrack.apiserver.CarData] (this fun feels like a monster D:)
fun getCarDataMap(doc: Document): MutableMap<String, CarData> {
    val xpFactory = XPathFactory.newInstance()
    val xPath = xpFactory.newXPath()

    val elementXPath = "/resultspage/results/result"
    val labelNodeList = xPath.evaluate(elementXPath, doc, NODESET) as NodeList
    val carMap: MutableMap <String, CarData> = mutableMapOf()

    for (i in 0 until labelNodeList.length) { // Node lists aren't iterable (thanks .js)
        try {
            val carData = CarData(
                regNum = labelNodeList.getAttributeValue(i, "regnumber"),

                carNum = labelNodeList.getAttributeValue(i, "no"),
                carClass = labelNodeList.getAttributeValue(i, "class"),
                firstName = labelNodeList.getAttributeValue(i, "firstname"),
                lastName = labelNodeList.getAttributeValue(i, "lastname"),
                fullName = labelNodeList.getAttributeValue(i, "fullname"),

                lastCrossingRTime = parseTimestamp(
                    labelNodeList.getAttributeValue(
                        i,
                        "totaltime"
                    )
                ).toMilliseconds(),
                lapCount = labelNodeList.getAttributeValue(i, "laps").safeToInt(),
                lastPitLap = labelNodeList.getAttributeValue(i, "lastpitstop").safeToInt(),
                lastTOD = labelNodeList.getAttributeValue(i, "lasttimeofday"),
                pitStopCount = labelNodeList.getAttributeValue(i, "nopitstops").safeToInt(),
                lastTimeLine = labelNodeList.getAttributeValue(i, "lasttimeline"),
                lapsSincePit = labelNodeList.getAttributeValue(i, "sincepit").safeToInt(),
                bestLapTime = parseTimestamp(
                    labelNodeList.getAttributeValue(i, "besttime")
                ).toMilliseconds(),
                lastLapTime = parseTimestamp(
                    labelNodeList.getAttributeValue(i, "lasttime")
                ).toMilliseconds(),

                averageSpeed = labelNodeList.getAttributeValue(i, "averagespeed"),
                averageLapTime = labelNodeList.getAttributeValue(i, "averagetime"),
                bestLap = labelNodeList.getAttributeValue(i, "bestinlap").safeToInt(),
                bestSpeed = labelNodeList.getAttributeValue(i, "bestspeed").safeToInt(),
                differenceToFirst = labelNodeList.getAttributeValue(i, "difference").safeToInt(),
                gapToNextPos = labelNodeList.getAttributeValue(i, "gap").safeToInt(),
                lastSpeed = labelNodeList.getAttributeValue(i, "lastspeed"),
                position = labelNodeList.getAttributeValue(i, "position").safeToInt(),
                classPosition = labelNodeList.getAttributeValue(i, "positioninclass").safeToInt(),
                transponder = labelNodeList.getAttributeValue(i, "transponder").safeToInt(),
                bestLapTimeString = labelNodeList.getAttributeValue(i, "besttime"),
                lastLapTimeString = labelNodeList.getAttributeValue(i, "lasttime")
            )
            carMap[carData.regNum] = carData
        } catch (ignore: NullPointerException) { // this shouldn't be thrown unless something is very wrong with the xml
            throw IllegalArgumentException("Critical Error! Bad xml; missing car data or keys(s)")}
        // i think this will still throw if XPath returns No Match?
    }
    return carMap
}
