package com.thecodefather.tabbula

data class TabbulaFragmentModel<T : TabFragmentType>(
    val fragmentType: T,
    val tabTitle: String
)

enum class TabsLoadingMethod {
    LOAD_ALL_AT_ONCE, LOAD_ONE_BY_ONE
}

interface TabFragmentType
