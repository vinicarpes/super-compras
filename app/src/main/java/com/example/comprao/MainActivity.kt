package com.example.comprao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprao.ui.theme.CompraoTheme
import com.example.comprao.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompraoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Titulo(modifier = Modifier.padding(innerPadding))
//                    ImagemTopo(modifier = Modifier.padding(innerPadding))
//                    Icone(
//                        icone = Icons.Default.Edit,
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun ImagemTopo(modifier: Modifier = Modifier){
    Image(painter = painterResource(id= R.drawable.imagem_topo),
        contentDescription = null,
        modifier = modifier.size(160.dp))
}

@Preview
@Composable
fun ImagemTopoPreview() {
    CompraoTheme {
        ImagemTopo()
    }
}

@Composable
fun Icone(icone : ImageVector, modifier: Modifier = Modifier) {
    Icon(icone,
        contentDescription = "Editar",
        modifier)
}

@Preview
@Composable
fun IconePreview() {
    CompraoTheme {
        Icone(Icons.Default.Edit)
    }
}
@Composable
fun Titulo(modifier: Modifier){
    Text(text = "Lista de Compras", modifier = modifier, style = Typography.headlineLarge)
}

@Preview
@Composable
fun TituloPreview(){
    CompraoTheme {
        Titulo(modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompraoTheme {
        Greeting("Jorge")
    }
}