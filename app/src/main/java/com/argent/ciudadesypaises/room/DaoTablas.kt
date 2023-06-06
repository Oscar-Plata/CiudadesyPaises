package com.argent.ciudadesypaises.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoTablas {

    //PUNTOS TURISTICOS
    @Query("SELECT * from puntoTuristico")
    fun allPuntos(): Flow<List<PuntoTuristico>>

    @Query("SELECT * FROM puntoTuristico WHERE idPunto =:idGet")
    fun getPunto(idGet:Int):Flow<PuntoTuristico>

    @Query("SELECT * FROM puntoTuristico WHERE idCiudadFK=:idFK")
    fun getPuntosByCiudad(idFK:Int):Flow<List<PuntoTuristico>>

    @Query("SELECT * FROM puntoTuristico WHERE idCiudadFK=(SELECT idCiudad from ciudad where paisFK=:idFK)")
    fun getPuntosByPais(idFK:Int):Flow<List<PuntoTuristico>>

    @Query("SELECT * FROM puntoTuristico WHERE tarifa=0")
    fun getPuntosGratis():Flow<List<PuntoTuristico>>

    @Query("SELECT * FROM puntoTuristico WHERE tarifa>0")
    fun getPuntosCosto():Flow<List<PuntoTuristico>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPunto(punto:PuntoTuristico)

    @Delete
    suspend fun delPunto(punto:PuntoTuristico)

    @Query("DELETE FROM puntoTuristico")
    suspend fun delPuntos()

    //PAISES
    @Query("SELECT * from pais")
    fun allPaises(): Flow<List<Pais>>

    @Query("SELECT * FROM pais WHERE codigoPais =:idGet")
    fun getPais(idGet:Int):Flow<Pais>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPais(pais:Pais)

    @Delete
    suspend fun delPais(pais:Pais)

    @Query("DELETE FROM pais")
    suspend fun delPaises()

    //CIUDADES
    @Query("SELECT * from ciudad")
    fun allCiudades(): Flow<List<Ciudad>>

    @Query("SELECT * FROM ciudad WHERE idCiudad =:idGet")
    fun getCiudad(idGet:Int):Flow<Ciudad>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCiudad(ciudad: Ciudad)

    @Delete
    suspend fun delCiudad(ciudad: Ciudad)

    @Query("DELETE FROM ciudad")
    suspend fun delCiudades()

    //GPS
    @Query("SELECT * from gps")
    fun allGps(): Flow<List<Gps>>

    @Query("SELECT * FROM gps WHERE idGPS =:idGet")
    fun getGPS(idGet:Int):Flow<Gps>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGps(gps: Gps)

    @Delete
    suspend fun delGps(gps: Gps)

    @Query("DELETE FROM gps")
    suspend fun delGPSs()

    //IDIOMAS
    @Query("SELECT * from idioma")
    fun allIdiomas(): Flow<List<Idioma>>

    @Query("SELECT * FROM idioma WHERE idIdioma =:idGet")
    fun getIdioma(idGet:Int):Flow<Idioma>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIdioma(idioma: Idioma)

    @Delete
    suspend fun delIdioma(idioma: Idioma)

    @Query("DELETE FROM idioma")
    suspend fun delIdiomas()
}