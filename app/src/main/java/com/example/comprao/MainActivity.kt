package com.example.comprao

import android.graphics.PathDashPathEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprao.ui.theme.CompraoTheme
import com.example.comprao.ui.theme.Coral
import com.example.comprao.ui.theme.Marinho
import com.example.comprao.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    val viewModel: CompraoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompraoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier, viewModel: CompraoViewModel) {
    val listaDeItens by viewModel.listaDeItens.collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {

            ImagemTopo()
            AdicionarItem(aoSalvarItem = { item ->
                viewModel.adicionarItem(item)
            })
            Spacer(Modifier.height(48.dp))
            Titulo(
                texto = "Lista de Compras"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (listaDeItens.isEmpty()){
            item {
                Text(
                    text = "Sua lista está vazia. Adicione itens a ela para não esquecer nada na próxima compra!",
                    style= Typography.bodyLarge
                )
            }
        }

        ListaDeItens(
            lista = listaDeItens.filter { !it.comprado },
            aoMudarStatus = { itemSelecionado ->
                viewModel.mudarStatus(itemSelecionado)
            },
            aoEditarItem = { itemEditado, novoTexto ->
                viewModel.editarItem(itemEditado, novoTexto)
            },
            aoRemoverItem = { itemRemovido ->
                viewModel.removerItem(itemRemovido)
            }
        )

        if (listaDeItens.any { it.comprado }) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Titulo(texto = "Comprado")
                Spacer(modifier = Modifier.height(24.dp))
            }
            ListaDeItens(
                lista = listaDeItens.filter { it.comprado },
                aoMudarStatus = { itemSelecionado ->
                    viewModel.mudarStatus(itemSelecionado)
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    viewModel.editarItem(itemEditado, novoTexto)
                },
                aoRemoverItem = { itemRemovido ->
                    viewModel.removerItem(itemRemovido)
                }
            )
        }
    }
}

fun LazyListScope.ListaDeItens(
    lista: List<ItemCompra>,
    aoMudarStatus: (ItemCompra) -> Unit,
    aoEditarItem: (ItemCompra, novoTexto: String) -> Unit = { _, _ -> },
    aoRemoverItem: (ItemCompra) -> Unit
) {
    items(lista.size) { index ->
        ItemDaLista(
            item = lista[index],
            aoMudarStatus = aoMudarStatus,
            aoEditarItem = aoEditarItem,
            aoRemoverItem = aoRemoverItem
        )
    }

}


@Composable
fun ItemDaLista(
    item: ItemCompra,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var textoEditado by rememberSaveable { mutableStateOf(item.texto) }
            var edicao by rememberSaveable { mutableStateOf(false) }

            Checkbox(
                checked = item.comprado,
                onCheckedChange = { aoMudarStatus(item) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )

            if (edicao) {
                OutlinedTextField(
                    value = textoEditado,
                    onValueChange = { textoEditado = it },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        aoEditarItem(item, textoEditado)
                        edicao = false
                    }
                ) {
                    Icone(
                        Icons.Default.Done,
                        Modifier.size(16.dp)
                    )
                }

            } else {
                Text(
                    text = item.texto,
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(
                onClick = { aoRemoverItem(item) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            ) {
                Icone(
                    Icons.Default.Delete
                )
            }

            IconButton(
                onClick = {
                    edicao = true
                },
                modifier = Modifier.size(16.dp)
            ) {
                Icone(Icons.Default.Edit)
            }
        }
        Text(
            item.dataHora,
            Modifier.padding(top = 8.dp),
            style = Typography.labelSmall
        )
    }
}

@Composable
fun AdicionarItem(aoSalvarItem: (item: ItemCompra) -> Unit, modifier: Modifier = Modifier) {
    var texto by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        placeholder = {
            Text(
                text = "Digite o item que deseja adicionar",
                color = Color.Gray,
                style = Typography.bodyMedium
            )
        },
        value = texto,
        onValueChange = { texto = it },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = Coral,
            focusedIndicatorColor = Coral,
            cursorColor = Coral
        )
    )
    Button(
        shape = RoundedCornerShape(24.dp),
        onClick = {
            aoSalvarItem(ItemCompra(texto, false, getDataHora()))
            texto = ""
        },
        colors = ButtonDefaults.buttonColors(Coral),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 12.dp)
    ) {
        Text(
            text = "Salvar Item",
            color = Color.White,
            style = Typography.bodyLarge,
        )
    }
}

fun getDataHora(): String {
    val dataHoraAtual = System.currentTimeMillis()
    val dataHoraFormatada = SimpleDateFormat("EEEE (dd/MM/yyyy) 'às' HH:mm", Locale("pt", "BR"))
    return dataHoraFormatada.format(dataHoraAtual)
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
        icone, contentDescription = "Editar", modifier, tint = Marinho
    )
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Left,
        text = texto,
        style = Typography.headlineLarge,
        modifier = modifier.padding(bottom = 8.dp).fillMaxWidth())
    LinhaPontilhada(modifier = modifier)
}

@Composable
fun LinhaPontilhada(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 2.5f)
    Canvas(modifier=modifier.fillMaxWidth()){
        drawLine(
            color = Coral,
            pathEffect = pathEffect,
            start = Offset(0f,0f),
            end = Offset(size.width, 0f),
            strokeWidth = 4f
        )
    }
}

@Preview
@Composable
private fun ItemDaListaPreview() {
    CompraoTheme {
        ItemDaLista(item = ItemCompra(texto = "teste", false, getDataHora()))
    }
}

@Preview
@Composable
private fun AdicionarItemPreview() {
    CompraoTheme { AdicionarItem(aoSalvarItem = {}) }
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


data class ItemCompra(
    var texto: String,
    var comprado: Boolean = false,
    val dataHora: String
)
