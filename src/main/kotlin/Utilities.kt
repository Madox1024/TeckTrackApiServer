
import org.w3c.dom.NodeList
import java.lang.NumberFormatException

data class Timestamp(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val millisecond: Int
)

// Attempts to parse given string and return com.techtrack.leaderboard.Timestamp class, returns null if Regex fails
fun parseTimestamp(string: String): Timestamp? {
    val timeStampGet = """(\d*?):?(\d{0,2}?):?(\d*)\.?(\d{0,3})$""".toRegex()
    return timeStampGet.matchEntire(string)
        ?.destructured
        ?.let { (hour, minute, second, millisecond) ->
            Timestamp(
                hour.safeToInt(),
                minute.safeToInt(),
                second.safeToInt(),
                fixMillisecond(millisecond)
            )
        }
        ?: return null
}
// Fixes dropped zeros e.g. ".12" = 120ms
fun fixMillisecond(milliseconds: String): Int {
    return if(milliseconds.length >= 3){
        milliseconds.safeToInt()
    }
    else {
        (milliseconds + "000".substring(0, 3-milliseconds.length)).safeToInt()
    }
}

// Returns 0 if String.toInt() fails
fun String.safeToInt(): Int {
    return try {
        this.toInt()
    } catch (ignored: NumberFormatException) {0}
}

// Converts a com.techtrack.leaderboard.Timestamp to milliseconds
// Int can contain enough milliseconds for for 24 Days
fun Timestamp?.toMilliseconds(): Int {
    return if (this == null){0} else {
        val millisecond = this.millisecond
        val seconds = this.second * 1000
        val minutes = this.minute * (60 * 1000)
        val hours = this.hour * ((60 * 1000) * 60)
        (millisecond + seconds + minutes + hours)
    }
}

fun logPrint(string: String) {
    println(string) // + TOD
    // generate and write to log file w/ TOD
}

fun NodeList.getAttributeValue(nodeIndex: Int, attributeKey: String): String { //xml parsing is wacky... look at this
    return this.item(nodeIndex).attributes.getNamedItem(attributeKey).toString().removePrefix("$attributeKey=\"").removeSuffix("\"")
}