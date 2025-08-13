package com.example.comprao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprao.ui.theme.CompraoTheme
import com.example.comprao.ui.theme.Coral
import com.example.comprao.ui.theme.Marinho
import com.example.comprao.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompraoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun ListaDeCompras(modifier: Modifier = Modifier) {
        var listaDeItens by rememberSaveable { mutableStateOf(listOf<ItemCompra>()) }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            ImagemTopo()
            AdicionarItem(aoSalvarItem = { textoNovo ->
                listaDeItens = listaDeItens + ItemCompra(textoNovo)
            })
            Spacer(Modifier.height(48.dp))
            Titulo(
                texto = "Lista de Compras"
            )
            ListaDeItens(
                lista = listaDeItens.filter { ! it.comprado },
                aoMudarStatus = { itemSelecionado ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemSelecionado == itemMap) {
                            itemSelecionado.copy(comprado = !itemSelecionado.comprado)
                        } else {
                            itemMap
                        }
                    }
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemMap == itemEditado) {
                            itemMap.copy(texto = novoTexto)
                        } else {
                            itemMap
                        }
                    }
                },
                aoRemoverItem = { itemRemovido ->
                    listaDeItens = listaDeItens - itemRemovido
                }
            )
            Titulo(texto = "Comprado")

            if (listaDeItens.any { it.comprado }){
                ListaDeItens(
                    lista = listaDeItens.filter { it.comprado },
                    aoMudarStatus = { itemSelecionado ->
                        listaDeItens = listaDeItens.map { itemMap ->
                            if (itemSelecionado == itemMap) {
                                itemSelecionado.copy(comprado = !itemSelecionado.comprado)
                            } else {
                                itemMap
                            }
                        }
                    },
                    aoEditarItem = { itemEditado, novoTexto ->
                        listaDeItens = listaDeItens.map { itemMap ->
                            if (itemMap == itemEditado) {
                                itemMap.copy(texto = novoTexto)
                            } else {
                                itemMap
                            }
                        }
                    },
                    aoRemoverItem = { itemRemovido ->
                        listaDeItens = listaDeItens - itemRemovido
                    }
                )
            }
        }
    }

    @Composable
    fun ListaDeItens(
        lista: List<ItemCompra>,
        aoMudarStatus: (ItemCompra) -> Unit,
        aoEditarItem: (ItemCompra, novoTexto: String) -> Unit = {_,_ ->},
        aoRemoverItem: (ItemCompra) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column {
            lista.forEach { item: ItemCompra ->
                ItemDaLista(
                    item = item,
                    aoMudarStatus = aoMudarStatus,
                    aoEditarItem = aoEditarItem,
                    aoRemoverItem = aoRemoverItem
                )
            }
        }
    }


    @Composable
    fun ItemDaLista(
        item: ItemCompra,
        aoMudarStatus: (item: ItemCompra) -> Unit = {},
        aoRemoverItem: (item: ItemCompra) -> Unit = {},
        aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = {_, _ ->},
        modifier: Modifier = Modifier
    ) {
        Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                var textoEditado  by rememberSaveable { mutableStateOf(item.texto) }
                var edicao by rememberSaveable {mutableStateOf(false)}

                Checkbox(
                    checked = item.comprado,
                    onCheckedChange = { aoMudarStatus(item) },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .requiredSize(24.dp)
                )

                if(edicao){
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
                            edicao = false }
                    ) {
                        Icone(
                            Icons.Default.Done,
                            Modifier.size(16.dp)
                        )
                    }

                } else{
                    Text(
                        text = item.texto,
                        style = Typography.bodyMedium,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
                IconButton(
                    onClick = { aoRemoverItem(item) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icone(
                        Icons.Default.Delete,
                        Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        edicao = true
                    }) { }
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
    fun AdicionarItem(aoSalvarItem: (texto: String) -> Unit, modifier: Modifier = Modifier) {
        var texto = rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            placeholder = {
                Text(
                    text = "Digite o item que deseja adicionar",
                    color = Color.Gray,
                    style = Typography.bodyMedium
                )
            },
            value = texto.value,
            onValueChange = { texto.value = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            shape = RoundedCornerShape(24.dp)
        )
        Button(
            shape = RoundedCornerShape(24.dp),
            onClick = {
                aoSalvarItem(texto.value)
                texto.value = ""
            },
            colors = ButtonDefaults.buttonColors(Coral),
            modifier = modifier
        ) {
            Text(
                text = "Salvar Item",
                color = Color.White,
                style = Typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
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
        Text(text = texto, style = Typography.headlineLarge, modifier = modifier)
    }

    @Preview
    @Composable
    private fun ItemDaListaPreview() {
        CompraoTheme {
            ItemDaLista(item = ItemCompra(texto = "teste"))
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

    @Preview
    @Composable
    private fun ListaComprapreview() {
        CompraoTheme {
            ListaDeCompras()
        }
    }
}

data class ItemCompra(
    var texto: String,
    var comprado: Boolean = false
)
