package com.argent.ciudadesypaises

import android.app.Application

class PaisesCiudadesApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}