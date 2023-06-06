package com.argent.ciudadesypaises.room

class RepoTablas(private val dao:DaoTablas) {
    val paises=dao.allPaises()
    val ciudades=dao.allCiudades()
    val puntos=dao.allPuntos()
    val idiomas=dao.allIdiomas()
    val gpss=dao.allGps()

    val puntosGratis=dao.getPuntosGratis()
    val puntosPago=dao.getPuntosCosto()
    fun puntosCiudad(c:Int)=dao.getPuntosByCiudad(c)
    fun puntosPais(p:Int)=dao.getPuntosByPais(p)


    fun unPais(x:Int)=dao.getPais(x)
    fun unaCiudad(x:Int)=dao.getCiudad(x)
    fun unPunto(x:Int)=dao.getPunto(x)
    fun unGPS(x:Int)=dao.getGPS(x)
    fun unIdioma(x:Int)=dao.getIdioma(x)

    suspend fun agregarPais(p:Pais)=dao.addPais(p)
    suspend fun agregarCiudad(c:Ciudad)=dao.addCiudad(c)
    suspend fun agregarPunto(p:PuntoTuristico)=dao.addPunto(p)
    suspend fun agregarIdioma(i:Idioma)=dao.addIdioma(i)
    suspend fun agregarGPS(g:Gps)=dao.addGps(g)

    suspend fun borrarPais(p:Pais)=dao.delPais(p)
    suspend fun borrarCiudad(c:Ciudad)=dao.delCiudad(c)
    suspend fun borrarPunto(p:PuntoTuristico)=dao.delPunto(p)
    suspend fun borrarIdioma(i:Idioma)=dao.delIdioma(i)
    suspend fun borrarGPS(g:Gps)=dao.delGps(g)

    suspend fun borrarPaises()=dao.delPaises()
    suspend fun borrarCiudades()=dao.delCiudades()
    suspend fun borrarPuntos()=dao.delPuntos()
    suspend fun borrarIdiomas()=dao.delIdiomas()
    suspend fun borrarGPSs()=dao.delGPSs()

}