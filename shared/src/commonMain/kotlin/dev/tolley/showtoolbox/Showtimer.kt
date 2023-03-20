package dev.tolley.showtoolbox
import kotlinx.datetime.*

class Showtimer {
    private val platform: Platform = getPlatform()

    fun currentTimeText(): String {
        return "Current Time:" +
                "\n${Clock.System.now()}"
    }
}