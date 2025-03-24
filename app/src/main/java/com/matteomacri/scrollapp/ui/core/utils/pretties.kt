package com.matteomacri.scrollapp.ui.core.utils

import androidx.compose.runtime.Composable

@Composable
fun String.toPrettyDescription(): String {
    return this.substringBefore("\n")
}