package com.example.loginsignupfrd

import java.io.Serializable

data class Event(
    var id: String = "",
    var eventName: String = "",
    var phoneNumber: String = "",
    var sportsName: String = "",
    var sportCategory: String = "",
    var time: String = "",
    var date: String = "",
    var location: String = "",
    var email: String = "",
    var age: Int = 0,
    var gender: String = ""
) : Serializable {
    // Add a no-argument constructor
    constructor() : this("", "", "", "", "", "", "", "", "", 0, "")
}
