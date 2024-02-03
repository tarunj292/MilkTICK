package com.example.milktickproject

class Offer {
    val name: String? = null
    val price: Long? = null
    val imageResId : String? = null
    val discPrice: Long? = null
    val qty: Int? = null
    val desc: String? = null
    constructor()
    constructor(name: String, price: String, imageResId: String)
    constructor(name: String, price: String, imageResId: String, qty:Int)
    constructor(name: String, price: String, imageResId: String, discPrice: String?)
}