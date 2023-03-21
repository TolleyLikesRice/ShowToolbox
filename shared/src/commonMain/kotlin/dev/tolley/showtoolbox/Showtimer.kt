package dev.tolley.showtoolbox
import kotlinx.datetime.*

class Showtimer {
    private val platform: Platform = getPlatform()

    fun currentTimeText(): String {
        return "${Clock.System.now().toString().substring(11, 19)}"
    }
}