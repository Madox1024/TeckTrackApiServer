// File Contains Data Classes for the xml data

data class CarData( // Contains car specific data, one obj/Car
    val regNum: String, // this is the Car's Unique ID

    // Other Car IDs, subject to change/corrections
    var carNum: String,
    var carClass: String,
    var firstName: String,
    var lastName: String,
    var fullName: String,

    // Data for Processing
    var lastCrossingRTime: Int,
    var lapCount: Int,
    var lastPitLap: Int,
    var lastTOD: String,
    var pitStopCount: Int,
    var lastTimeLine: String,
    var bestLapTime: Int,
    var bestLapTimeString: String,
    var lastLapTime: Int, // this will be 0 b/c xml can return "IN PIT" keep in mind when converting to display
    var lastLapTimeString: String,
    var lapsSincePit: Int,

    // Data for display (no processing)
    var averageSpeed: String,
    var averageLapTime: String,
    var bestLap: Int,
    var bestSpeed: Int,
    var differenceToFirst: Int,
    var gapToNextPos: Int,
    var lastSpeed: String,
    var position: Int,
    var classPosition: Int,
    var transponder: Int
)

data class RaceData( // Contains track & race information
    val eventName: String,
    val groupName: String,
    val runName: String,
    val trackName: String,
    val runType: String,
    val bestLapBy: String,
    val bestOverallLapTime: String,
    val flagStatus: String,
    val leader: String,
    val trackLength: Int,
    val leaderDiff: Int, // Number of laps leader is ahead of 2nd place
    val laps: Int,
    val raceTime: Int,
    val timeOfDay: Int
)
