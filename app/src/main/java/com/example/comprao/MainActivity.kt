package com.example.comprao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprao.ui.theme.CompraoTheme
import com.example.comprao.ui.theme.Marinho
import com.example.comprao.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompraoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(verticalArrangement = Arrangement.Top) {
                        ImagemTopo(modifier = Modifier.padding(innerPadding))
                        Titulo(
                            texto = "Lista de Compras",
                            modifier = Modifier.padding(innerPadding)
                        )
                        ItemDaLista(modifier = Modifier.padding(innerPadding))
                        Titulo(texto = "Comprado", modifier = Modifier.padding(innerPadding))
                    }

                }
            }
        }
    }
}


@Composable
fun ItemDaLista(modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )
            Text(
                text = "suco",
                style = Typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Icone(
                Icons.Default.Delete, Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            )
            Icone(Icons.Default.Edit, Modifier.size(16.dp))
        }
        Text(
            "Segunda-Feiras (30/11/2024) às 8:30",
            Modifier.padding(top = 8.dp),
            style = Typography.labelSmall
        )
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
fun ImagemTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.imagem_topo),
        contentDescription = null,
        modifier = modifier.size(160.dp)
    )
}

@Composable
fun Icone(icone: ImageVector, modifier: Modifier = Modifier) {
    Icon(
        icone,
        contentDescription = "Editar",
        modifier,
        tint = Marinho
    )
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto, style = Typography.headlineLarge, modifier = modifier)
}

@Preview
@Composable
private fun ItemDaListaPreview() {
    CompraoTheme {
        ItemDaLista()
    }
}

@Preview
@Composable
fun ImagemTopoPreview() {
    CompraoTheme {
        ImagemTopo()
    }
}

@Preview
@Composable
fun IconePreview() {
    CompraoTheme {
        Icone(Icons.Default.Edit)
    }
}

@Preview
@Composable
fun TituloPreview() {
    CompraoTheme {
        Titulo(texto = "Lista de Compras")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompraoTheme {
        Greeting("Jorge")
    }
}