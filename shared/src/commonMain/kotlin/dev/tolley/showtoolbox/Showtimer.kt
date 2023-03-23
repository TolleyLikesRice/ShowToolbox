package dev.tolley.showtoolbox

import kotlinx.datetime.*

class Showtimer {
    private var timings = mutableMapOf<String, Instant>()

    fun currentTimeText(): String {
        return Clock.System.now().toString().substring(11, 19)
    }

    fun record(timing: String): String {

        // Don't question it, if you get rid of this, Time Elapsed and Current Time are out of sync
        // Basically it just removes the millisecond precision
        var time: Instant = Clock.System.now();
        var str = time.toEpochMilliseconds().toString()
        str = "${str.substring(0, str.length - 3)}000"
        time = Instant.fromEpochMilliseconds(str.toLong())

        timings[timing] = time
        return timings[timing].toString().substring(11, 19)
    }

    fun deleteTime(timing: String) {
        timings.remove(timing)
    }

    fun timeSince(timing: String): String {
        return if (timings.containsKey(timing)) {
            Instant.fromEpochMilliseconds(timings[timing]!!.until(Clock.System.now(), DateTimeUnit.MILLISECOND))
                .toString().substring(11, 19)
        } else "??:??:??"
    }
}