package com.matteomacri.scrollapp.domain.net

import androidx.room.ProvidedTypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Named

@ProvidedTypeConverter
@Named("typeAdapters")
class TypeAdapters {

    private val apiDateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US)
    private val apiDurationFormatter = SimpleDateFormat("HH:mm:ss", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    @ToJson
    fun toDurationString(value: Duration?): String? {
        return try {
            apiDurationFormatter.format(Date(value!!.toMillis()))
        } catch (e: Exception) {
            null
        }
    }

    @FromJson
    fun fromDurationString(value: String?): Duration? {
        return try {
            Duration.ofMillis(apiDurationFormatter.parse(value!!)!!.time)
        } catch (e: Exception) {
            null
        }
    }
}