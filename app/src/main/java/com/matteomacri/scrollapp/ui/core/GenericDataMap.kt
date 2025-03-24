package com.matteomacri.scrollapp.ui.core

import kotlinx.coroutines.flow.Flow

class GenericDataMap<T, U>(private val provider: (T) -> Flow<U>) {
    private val flowMap = mutableMapOf<T, Flow<U>>()
    operator fun get(id: T) = flowMap.getOrPut(id) { provider(id) }
}