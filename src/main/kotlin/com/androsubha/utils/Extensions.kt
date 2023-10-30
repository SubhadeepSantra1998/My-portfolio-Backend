package com.androsubha.utils


import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.Document
import org.bson.conversions.Bson
import java.time.Instant


fun Long.toInstant(): Instant {
    return Instant.ofEpochMilli(this)
}

fun Instant.toLong(): Long {
    return this.toEpochMilli()
}

fun generateTimestamp(): Instant {
    return Instant.now()
}

fun MongoCollection<Document>.createDateRangeFilter(): Bson {
    val currentTimestamp = Instant.now().toEpochMilli()
    return Filters.and(
        Filters.lte("startDate", currentTimestamp),
        Filters.gte("endDate", currentTimestamp)
    )
}
