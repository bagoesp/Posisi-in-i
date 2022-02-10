package com.bugs.posisi_in_1301.map

data class Cell(
    var x: Int = 0,
    var y: Int = 0,
    var top: Boolean = false,
    var right: Boolean = false,
    var bottom: Boolean = false,
    var left: Boolean = false
)
