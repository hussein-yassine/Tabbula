package com.thecodefather.tabbulahost

import com.thecodefather.tabbula.TabFragmentType


sealed class MyTabFragmentType : TabFragmentType {
    data object Dashboard : MyTabFragmentType()
    data object Profile : MyTabFragmentType()
    data object Default : MyTabFragmentType()
}

