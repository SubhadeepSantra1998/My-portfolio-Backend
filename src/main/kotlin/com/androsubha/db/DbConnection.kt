package com.androsubha.db

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object DbConnection {

    fun connect(): CoroutineDatabase {
        val dbName = "my-portfolio"
        val mongoPassword = "ZrJPu1FKxjaPzjVt"

        return KMongo.createClient(
            connectionString = "mongodb+srv://subhadeep:$mongoPassword@cluster0.wctjsrc.mongodb.net/?retryWrites=true&w=majority"
        ).coroutine.getDatabase(dbName)
    }
}