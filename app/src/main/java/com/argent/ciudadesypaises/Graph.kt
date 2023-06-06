package com.argent.ciudadesypaises

import android.content.Context
import com.argent.ciudadesypaises.room.AppDB
import com.argent.ciudadesypaises.room.RepoTablas

object Graph {
    lateinit var db: AppDB
        private set

    val repo by lazy{
        RepoTablas(dao= db.appDao())
    }

    fun provide(context:Context){
        db = AppDB.getDB(context)
    }
}