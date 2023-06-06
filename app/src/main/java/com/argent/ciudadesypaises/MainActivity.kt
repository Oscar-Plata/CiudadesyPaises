

package com.argent.ciudadesypaises

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.argent.ciudadesypaises.room.Ciudad
import com.argent.ciudadesypaises.room.Pais
import com.argent.ciudadesypaises.room.PuntoTuristico
import com.argent.ciudadesypaises.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CiudadesYPaisesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ContenedorPrincipal()
                }
            }
        }
    }
}

@Composable
fun ContenedorPrincipal() {
    val viewModel = viewModel(modelClass = DatosViewModel::class.java)
    val estado = viewModel.estado.collectAsState().value

    when(estado.fragmentoVisible){
        0 -> FragmentoSplash(viewModel,estado)
        1 -> FragmetoLista(viewModel,estado, onRegresar = {viewModel.cambiarFragmento(0)})
        2 -> FragmetoFormulario(viewModel,estado, onRegresar = {viewModel.cambiarFragmento(1)})
        else -> FragmentoSplash(viewModel,estado)
    }
}

@Composable
fun FragmentoSplash(dvm:DatosViewModel,est:EstadoApp) {
    Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally){

        Image(painter = painterResource(id = R.drawable.appiconn), contentDescription = null, modifier = Modifier.padding(top=60.dp).size(230.dp))
        Text(text = "Ciudades Y Paises", style = MaterialTheme.typography.body2,color= orange, fontSize = 40.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.weight(0.75f))
        Button(onClick = {dvm.cambiarFragmento(1)},
            colors = ButtonDefaults.buttonColors(backgroundColor = orange),
            modifier= Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(150.dp)
                .clip(CircleShape)) {
            Text(text = "INICIO", style = MaterialTheme.typography.body2,color= Color.White, fontSize = 40.sp)
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

}

@Composable
fun FragmetoLista(dvm:DatosViewModel,est:EstadoApp,onRegresar:()->Unit) {
    val ctx = LocalContext.current
    val titulo = when(est.modo){
        0 -> "Puntos Turisticos";
        1 -> "Ciudades";
        2 -> "Paises";
        else -> "Otro";
    }

    BackHandler {
        onRegresar()
    }
    Scaffold(
        topBar = {TopAppBar(title ={Text(text = titulo,style = MaterialTheme.typography.body2)}, backgroundColor = orange) },
        bottomBar ={ BottomAppBar(backgroundColor = orange) {
            Spacer(modifier = Modifier.weight(0.8f))
            Button(onClick = {dvm.cambiarModo(0)}, colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                painter = painterResource(R.drawable.point),
                contentDescription = "Punto"
            )}
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {dvm.cambiarModo(1)},colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                painter = painterResource(R.drawable.city),
                contentDescription = "Ciudad"
            )}
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {dvm.cambiarModo(2)},colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                painter = painterResource(R.drawable.world),
                contentDescription = "Pais"
            )}
            Spacer(modifier = Modifier.weight(0.8f))
        }} ,
        floatingActionButton = {
            FloatingActionButton(onClick = { dvm.cambiarFragmento(2) }, backgroundColor = green, contentColor = Color.White,) {
                Icon(Icons.Filled.Add, contentDescription = "null")
            }
        })

    { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            if(est.modo==0) {
                val scrollState = rememberScrollState()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .height(80.dp)
                        .background(color = yellow),
                    verticalAlignment = Alignment.CenterVertically){
                    //Filtros
                    Button(onClick = {dvm.resetVisibles()}, modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(CircleShape)) { Text(text = "Todos",style = MaterialTheme.typography.body2)}
                    Button(onClick = {dvm.buscarGratis()}, modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(CircleShape)) { Text(text = "Gratis",style = MaterialTheme.typography.body2)}
                    Button(onClick = {dvm.buscarPago()}, modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(CircleShape)) { Text(text = "De Pago",style = MaterialTheme.typography.body2)}
                    ComboBox(dvm.listaCiudades(), onItem = {dvm.buscarPorCiudad(it)},120)
                    ComboBox(dvm.listaPaises(), onItem = {dvm.buscarPorPais(it)},120)
                    Button(onClick = {dvm.limpiarDB();Toast.makeText(ctx,"La BD ha sido Borrada",Toast.LENGTH_LONG).show()},colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red), modifier = Modifier
                        .height(60.dp)
                        .padding(5.dp)
                        .clip(CircleShape)) { Text(text = "BORRAR TODO",style = MaterialTheme.typography.body2)}
                }
            }
            LazyColumn {
                when(est.modo){
                    0 -> items(est.listaVisible) { PuntoChip(p = it,dvm) }
                    1 -> items(est.listaCiudades){ CiudadChip(c = it,dvm)}
                    2 -> items(est.listaPaises){ PaisChip(p = it,dvm) }
                }
            }
        }
    }
}

@Composable
fun FragmetoFormulario(dvm:DatosViewModel,est:EstadoApp,onRegresar:()->Unit) {
    BackHandler {
        onRegresar()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Agregar Item",style = MaterialTheme.typography.body2)}, backgroundColor = orange) },
            bottomBar ={ BottomAppBar(backgroundColor = orange) {
                Spacer(modifier = Modifier.weight(0.8f))
                Button(onClick = {dvm.cambiarModo(0)}, colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                    painter = painterResource(R.drawable.point),
                    contentDescription = "Punto"
                )}
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {dvm.cambiarModo(1)},colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                    painter = painterResource(R.drawable.city),
                    contentDescription = "Ciudad"
                )}
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {dvm.cambiarModo(2)},colors = ButtonDefaults.buttonColors(backgroundColor = orange)) { Image(
                    painter = painterResource(R.drawable.world),
                    contentDescription = "Pais"
                )}
                Spacer(modifier = Modifier.weight(0.8f))
            }}
        ) {padding ->
            Column(Modifier.padding(padding)) {
                when(est.modo){
                    0 -> FormularioPunto(dvm,onRegresar)
                    1 -> FormularioCiudad(dvm,onRegresar)
                    2 -> FormularioPais(dvm,onRegresar)
                }
            }
        }
    }
}

@Composable
fun FormularioPais(dvm:DatosViewModel,onRegresar:()->Unit) {
    val scrollState = rememberScrollState()
    var id by remember { mutableStateOf("0") }
    var nombre by remember{ mutableStateOf("Pais") }
    var extension by remember{ mutableStateOf("0.0") }
    var continente by remember{ mutableStateOf("Contienete") }
    var habitantes by remember{ mutableStateOf("1234") }
    var gobernador by remember{ mutableStateOf("Gobernador") }

    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
        Text(text = "Pais",style = MaterialTheme.typography.body2)
        OutlinedTextField(value = id, onValueChange = {id = it}, label = {Text("IdPais")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = {nombre = it}, label = {Text("Nombre")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = extension, onValueChange = {extension = it}, label = {Text("Extension")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = continente, onValueChange = {continente = it}, label = {Text("Contienente")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = habitantes, onValueChange = {habitantes = it}, label = {Text("Habitantes")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = gobernador, onValueChange = {gobernador = it}, label = {Text("Gobernador")}, modifier = Modifier.fillMaxWidth())
        Button(onClick = {dvm.agregarPais(Pais(id.toInt(),nombre,extension.toFloat(),continente,habitantes.toLong(),gobernador)); onRegresar() }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = green)) {
            Text(text = "GUARDAR",style = MaterialTheme.typography.body2, modifier = Modifier.fillMaxWidth())
        }
    }

}

@Composable
fun FormularioCiudad(dvm:DatosViewModel,onRegresar:()->Unit) {
    val scrollState = rememberScrollState()
    var id by remember { mutableStateOf("0") }
    var nombre by remember{ mutableStateOf("Ciudad") }
    var estado by remember{ mutableStateOf("Estado") }
    var habitantes by remember{ mutableStateOf("1234") }
    var idPais by remember{ mutableStateOf("0") }

    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
        Text(text = "Ciudad",style = MaterialTheme.typography.body2)
        OutlinedTextField(value = id, onValueChange = {id = it}, label = {Text("IdCiudad")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = {nombre = it}, label = {Text("Nombre")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = estado, onValueChange = {estado = it}, label = {Text("Estado")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = habitantes, onValueChange = {habitantes = it}, label = {Text("Habitantes")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = idPais, onValueChange = {idPais = it}, label = {Text("IdPais")}, modifier = Modifier.fillMaxWidth())

        Button(onClick = {dvm.agregarCiudad(Ciudad(id.toInt(),nombre,estado,habitantes.toLong(),idPais.toInt())); onRegresar()}, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = green)) {
            Text(text = "GUARDAR",style = MaterialTheme.typography.body2, modifier = Modifier.fillMaxWidth())
        }
    }

}

@Composable
fun FormularioPunto(dvm:DatosViewModel,onRegresar:()->Unit) {
    val scrollState = rememberScrollState()
    var id by remember { mutableStateOf("0") }
    var nombre by remember{ mutableStateOf("Punto Turistico") }
    var descripcion by remember{ mutableStateOf("Descripcion") }
    var tarifa by remember{ mutableStateOf("0.0") }
    var idCiudad by remember{ mutableStateOf("0") }
    var idGPS by remember{ mutableStateOf("0") }

    Column(
        Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
        Text(text = "Punto Turistico",style = MaterialTheme.typography.body2)
        OutlinedTextField(value = id, onValueChange = {id=it}, label = {Text("IdPunto")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = {nombre = it}, label = {Text("Nombre")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = descripcion, onValueChange = {descripcion = it}, label = {Text("Descripcion")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = tarifa, onValueChange = {tarifa = it}, label = {Text("Tarifa")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = idCiudad, onValueChange = {idCiudad = it}, label = {Text("IdCiudad")}, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = idGPS, onValueChange = {idGPS = it}, label = {Text("IdGPS")}, modifier = Modifier.fillMaxWidth())

        Button(onClick = {dvm.agregarPunto(PuntoTuristico(id.toInt(),nombre,descripcion,tarifa.toFloat(),idGPS.toInt(),idCiudad.toInt())); onRegresar()}, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = green)) {
            Text(text = "GUARDAR",style = MaterialTheme.typography.body2, modifier = Modifier.fillMaxWidth())
        }
    }
}


@Composable
fun PuntoChip(p:PuntoTuristico,dvm: DatosViewModel){
    var touch by remember { mutableStateOf(false) }
    val ctx= LocalContext.current
    Column(modifier = Modifier
        .padding(horizontal = 30.dp, vertical = 5.dp)
        .clip(shape = Shapes.medium)
        .background(greenL)
        .fillMaxWidth()
        .heightIn(30.dp, 350.dp)
        .clickable { touch = !touch }) {
        Row(modifier=Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = p.id.toString(),
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(.3f),style = MaterialTheme.typography.body2
            )
            Text(
                text = p.nombre,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),style = MaterialTheme.typography.body2
            )
            }
        if (touch) {
            Text(text = "Descripcion:\n ${p.desc}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp)
                .heightIn(20.dp, 250.dp))
            Text(text = "Costo: ${p.tarifa}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier.padding(horizontal = 10.dp))
            Text(text = "Ciudad: ${dvm.getNombreCiudad(p.fkCiudad)} [${p.fkCiudad}]", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier.padding(horizontal = 10.dp))
        }
    }
}
@Composable
fun PaisChip(p: Pais,dvm: DatosViewModel){
    var touch by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .padding(horizontal = 30.dp, vertical = 5.dp)
        .clip(shape = Shapes.medium)
        .background(blueL)
        .fillMaxWidth()
        .heightIn(30.dp, 200.dp)
        .clickable { touch = !touch }) {
        Row(modifier=Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
        Text(text = p.id.toString(),fontSize = 30.sp, color = Color.White,fontWeight = FontWeight.ExtraBold, modifier = Modifier
            .padding(horizontal = 10.dp)
            .weight(.3f),style = MaterialTheme.typography.body2)
        Text(text = p.nombre, fontSize = 25.sp, fontWeight = FontWeight.Bold,color = Color.Black,modifier = Modifier
            .padding(horizontal = 5.dp)
            .weight(1f),style = MaterialTheme.typography.body2)
        }
        if (touch) {
            Text(text = "Gobernador:\n ${p.gobernador}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp)
                .heightIn(20.dp, 250.dp))
            Text(text = "Extension: ${p.extension}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text = "Continente: ${p.contienete}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text = "Habitantes: ${p.habitates}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp))

        }
    }
}
@Composable
fun CiudadChip(c:Ciudad,dvm: DatosViewModel){
    var touch by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .padding(horizontal = 30.dp, vertical = 5.dp)
        .clip(shape = Shapes.medium)
        .background(redL)
        .fillMaxWidth()
        .heightIn(30.dp, 200.dp)
        .clickable { touch = !touch }) {
        Row(modifier=Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
        Text(text = c.id.toString(),fontSize = 30.sp, color = Color.White,fontWeight = FontWeight.ExtraBold, modifier = Modifier
            .padding(horizontal = 10.dp)
            .weight(.3f),style = MaterialTheme.typography.body2)
        Text(text = c.nombre, fontSize = 25.sp, fontWeight = FontWeight.Bold,color = Color.Black,modifier = Modifier
            .padding(horizontal = 5.dp)
            .weight(1f),style = MaterialTheme.typography.body2)
        }
        if (touch) {
            Text(text = "Estado:\n ${c.estado}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp)
                .heightIn(20.dp, 250.dp))
            Text(text = "Habitantes: ${c.habitates}", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp))
            Text(text = "Pais: ${dvm.getNombrePais(c.codigoPais)} [${c.codigoPais}]", fontSize = 15.sp,color = Color.DarkGray, fontWeight = FontWeight.Thin, modifier = Modifier
                .padding(horizontal = 10.dp))
        }

    }
}


//Funcion de elemento UI para crear ComboBox
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComboBox(items:Array<String>,onItem:(String) -> Unit,tam:Int) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items[0]) }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .clip(CircleShape)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(fontFamily = FredokaOne, color = Color.Black),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.widthIn(tam.dp,((tam)*1.5f).dp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = orange)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        content= { Text(text = item, style = MaterialTheme.typography.body2, color = Color.White)},
                        onClick = {
                            selectedText = item
                            onItem(item)
                            expanded = false
//                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}