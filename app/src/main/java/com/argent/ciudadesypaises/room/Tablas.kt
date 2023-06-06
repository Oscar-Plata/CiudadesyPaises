package com.argent.ciudadesypaises.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pais")
data class Pais(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="codigoPais")
    var id:Int,
    var nombre:String,
    var extension:Float,
    var contienete:String,
    var habitates:Long,
    var gobernador:String
)

@Entity(tableName="ciudad")
data class Ciudad(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="idCiudad")
    var id:Int,
    var nombre:String,
    var estado:String,
    var habitates: Long,
    @ColumnInfo(name="paisFK")
    var codigoPais: Int,
)

@Entity(tableName="puntoTuristico")
data class PuntoTuristico(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="idPunto")
    var id:Int,
    var nombre:String,
    var desc:String,
    @ColumnInfo(name="tarifa")
    var tarifa: Float=0f,
    @ColumnInfo(name="idGPSFK")
    var fkGPS: Int,
    @ColumnInfo(name = "idCiudadFK")
    var fkCiudad:Int
)

@Entity(tableName = "gps")
data class Gps(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="idGPS")
    var id:Int,
    var latitud:Double,
    var longitu:Double,
    var altitud:Double,
)

@Entity(tableName = "idioma")
data class Idioma(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="idIdioma")
    var id:Int,
    var nombre:String,
    var oficial:Boolean,
    @ColumnInfo(name="codigoPaisFK")
    var paisFK:Int
)

