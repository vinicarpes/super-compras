package com.example.comprao

import android.content.ClipData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompraoViewModel : ViewModel() {
    private val _listaDeItens: MutableStateFlow<List<ItemCompra>> = MutableStateFlow(emptyList())
    val listaDeItens: StateFlow<List<ItemCompra>> = _listaDeItens

    fun adicionarItem(item: ItemCompra) {
        viewModelScope.launch {
            _listaDeItens.update { lista ->
                lista + item
            }
        }
    }

    fun removerItem(item: ItemCompra) {
        viewModelScope.launch {

            _listaDeItens.update { lista ->
                lista - item
            }
        }
    }

    fun editarItem(itemEditado: ItemCompra, novoTexto: String) {
        viewModelScope.launch {
            _listaDeItens.update { lista ->
                lista.map { itemAtual ->
                    if (itemAtual == itemEditado) {
                        itemAtual.copy(texto = novoTexto)
                    } else {
                        itemAtual
                    }
                }
            }
        }
    }

    fun mudarStatus(itemSelecionado: ItemCompra) {
        viewModelScope.launch {
            _listaDeItens.update { lista ->
                lista.map { itemMap ->
                    if (itemSelecionado == itemMap) {
                        itemSelecionado.copy(comprado = !itemSelecionado.comprado)
                    } else {
                        itemMap
                    }
                }
            }
        }
    }
}
