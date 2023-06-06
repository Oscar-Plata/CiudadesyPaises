package com.argent.ciudadesypaises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.argent.ciudadesypaises.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DatosViewModel(private val repositorio:RepoTablas = Graph.repo): ViewModel() {
    private val _estado= MutableStateFlow(EstadoApp())
    val estado : StateFlow<EstadoApp> =_estado

    init {
        loadPuntos()
        loadCiudades()
        loadPaises()
        loadIdiomas()
        loadGPSs()
    }

    fun loadPuntos(){
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.puntos.collectLatest {
                _estado.update { x -> x.copy(listaPuntos = it, listaVisible = it) }
                //
            }
        }
    }
    fun loadPaises(){
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.paises.collectLatest {
                _estado.update { x -> x.copy(listaPaises = it) }
            }
        }
    }

    fun loadCiudades(){
        viewModelScope.launch(Dispatchers.IO){
            repositorio.ciudades.collectLatest{
                _estado.update { x -> x.copy(listaCiudades = it) }
            }
        }
    }
    fun loadIdiomas(){
        viewModelScope.launch(Dispatchers.IO){
            repositorio.idiomas.collectLatest {
                _estado.update { x -> x.copy(listaIdiomas = it) }
            }
        }
    }
    fun loadGPSs(){
        viewModelScope.launch(Dispatchers.IO){
            repositorio.gpss.collectLatest {
                _estado.update { x -> x.copy(listaGPS = it) }
            }
        }
    }

    fun agregarPais(p:Pais){
        viewModelScope.launch(Dispatchers.IO) { repositorio.agregarPais(p)

        }

    }

    fun agregarCiudad(c:Ciudad){
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.agregarCiudad(c)
        }
    }

    fun agregarPunto(p:PuntoTuristico){
        viewModelScope.launch(Dispatchers.IO){
            repositorio.agregarPunto(p)
        }

    }

    fun agregarIdioma(i:Idioma){
        viewModelScope.launch(Dispatchers.IO) { repositorio.agregarIdioma(i) }

    }
    fun agregarGps(g:Gps){
        viewModelScope.launch(Dispatchers.IO) { repositorio.agregarGPS(g) }

    }

    fun buscarPorCiudad(c: String){
        val div=c.split(":")
        val a= div[0].toInt()
        viewModelScope.launch {
            repositorio.puntosCiudad(a).flowOn(Dispatchers.IO).collect{
                _estado.update { x -> x.copy(listaVisible = it) }
            }
        }
    }

    fun buscarPorPais(p: String){
        val div=p.split(":")
        val a= div[0].toInt()
        val ciudades = _estado.value.listaCiudades
        val puntos=_estado.value.listaPuntos
        var auxCiudad= ciudades.filter {  it.codigoPais.equals(a)}
        var auxpuntos:MutableList<PuntoTuristico> = mutableListOf()
        puntos.forEach { p->
            auxCiudad.forEach {
                if(p.fkCiudad.equals(it.id)){
                    auxpuntos.add(p)
                }
            }
        }
        auxpuntos.toList()
        _estado.update { x -> x.copy(listaVisible = auxpuntos) }
//        viewModelScope.launch {
//            repositorio.puntosPais(a).flowOn(Dispatchers.IO).collect{
//                _estado.update { x -> x.copy(listaVisibl  e = it) }
//            }
//        }
    }

    fun buscarGratis(){
        viewModelScope.launch {
            repositorio.puntosGratis.collectLatest{
                _estado.update { x -> x.copy(listaVisible = it) }
            }
        }
    }

    fun buscarPago(){
        viewModelScope.launch {
            repositorio.puntosPago.collectLatest{
                _estado.update { x -> x.copy(listaVisible = it) }
            }
        }
    }


    fun resetVisibles(){
        _estado.update { x -> x.copy(listaVisible = x.listaPuntos)}
    }

    fun limpiarDB(){
        viewModelScope.launch(Dispatchers.IO) {
            repositorio.borrarPaises()
            repositorio.borrarCiudades()
            repositorio.borrarIdiomas()
            repositorio.borrarPuntos()
            repositorio.borrarGPSs()
        }
    }

    fun cambiarFragmento(f:Int){
        _estado.update { x -> x.copy(fragmentoVisible = f) }
    }

    fun cambiarModo(m:Int){
        _estado.update { x -> x.copy(modo = m) }
    }

    fun getNombreCiudad(x:Int): String{
        val lista = _estado.value.listaCiudades
        if(lista.isEmpty())
            return "Desconocido"
        val res=lista.find { it.id == x }
        if (res != null) {
            return res.nombre
        }
        return "Desconocido"
    }

    fun getNombrePais(x:Int): String{
        val lista = _estado.value.listaPaises
        if(lista.isEmpty())
            return "Desconocido"
        val res=lista.find { it.id == x }
        if (res != null) {
            return res.nombre
        }
        return "Desconocido"
    }

    fun listaCiudades(): Array<String> {
        val lista = _estado.value.listaCiudades
        if(lista.isEmpty())
            return arrayOf("Vacio")
        return Array(lista.size) { i -> "${lista[i].id}:${lista[i].nombre}"}
    }
    fun listaPaises(): Array<String> {
        val lista = _estado.value.listaPaises
        if(lista.isEmpty())
            return arrayOf("Vacio")
        return Array(lista.size) { i -> "${lista[i].id}:${lista[i].nombre}"}
    }
}

data class EstadoApp (
    val fragmentoVisible:Int=0,
    val modo:Int=0, //0-Puntos 1-Ciudades 2-Paises
    val listaPaises: List<Pais> = listOf(),
    val listaCiudades: List<Ciudad> = listOf(),
    val listaPuntos: List<PuntoTuristico> = listOf(),
    val listaIdiomas: List<Idioma> = listOf(),
    val listaGPS: List<Gps> = listOf(),
    val listaVisible: List<PuntoTuristico> = listOf(),
//    val paisActual:Pais?= Pais(0,"PaisDEF",0.0f,"America",1234,"Presidente"),
//    val ciudadActual:Ciudad?= Ciudad(0,"CiudadDEF","EstadoDef",1234,0),
//    val puntoActual:PuntoTuristico?= PuntoTuristico(0,"PuntoDEF","DESC",0f,0,0),
//    val gpsActual:Gps=Gps(0, 0.0,0.0,0.0)
)