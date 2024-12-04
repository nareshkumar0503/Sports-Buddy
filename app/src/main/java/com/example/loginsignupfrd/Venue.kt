package com.example.loginsignupfrd

data class Venue(
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val imageUrl: String = ""
) {
    constructor() : this("", "", "", "")
}
