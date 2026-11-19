package com.clase.listacompose.ui.miTema

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EsquemaColores = lightColorScheme(
    primary = AzulPrincipal,
    secondary = AzulClaro,
    background = Fondo,
    onBackground = Texto
)

@Composable
fun MiTema(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaColores,
        content = content
    )
}