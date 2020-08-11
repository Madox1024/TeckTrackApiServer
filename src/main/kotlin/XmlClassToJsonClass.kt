// Converting the relevant data from XmlDataClasses to JsonDataClasses to prep for easy Gson conversion

fun getJsonMasterClass(racePair: Pair<RaceData, MutableMap<String, CarData>>): JsonMasterClass {
    return JsonMasterClass(
        eventName = racePair.first.eventName,
        laps = racePair.first.laps,
        flagStatus = racePair.first.flagStatus,
        carDataList = getCarList(racePair)
    )
}

// Generating list of cars for carDataList Val above
fun getCarList(racePair: Pair<RaceData, MutableMap<String, CarData>>): List<CarLeaderBoardData> {
    val racers = mutableListOf<CarLeaderBoardData>()
    for (carData in racePair.second.values) {
        racers.add(
            CarLeaderBoardData(
                regNum = carData.regNum,
                racer_name = carData.fullName,
                racer_number = carData.carNum,
                racer_class_name = carData.carClass,
                position_in_run = carData.position,
                current_lap = carData.lapCount,
                best_lap_number = carData.bestLap,
                best_lap_time = carData.bestLapTimeString,
                last_lap_time = carData.lastLapTimeString
            )
        )
    }
    return racers
}