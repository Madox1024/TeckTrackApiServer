package com.tecktrack.apiserver

import com.google.gson.annotations.SerializedName

data class JsonMasterClass(
   @SerializedName("name") val eventName: String,
   @SerializedName("current_lap") val laps: Int,
   @SerializedName("current_flag") val flagStatus: String,
   @SerializedName("racers") val carDataList: List<CarLeaderBoardData>
)
// See XmlDataClasses.kt for full list of available data
data class CarLeaderBoardData(
   val regNum: String,
   val racer_name: String,
   val racer_number: String,
   val racer_class_name: String,
   val position_in_run: Int,
   val current_lap: Int,
   val best_lap_number: Int,
   val best_lap_time: String,
   val last_lap_time: String
)