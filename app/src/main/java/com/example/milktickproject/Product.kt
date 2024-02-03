package com.example.milktickproject


class Product {
    val name: String? = null
    val price: Long? = null
    val imageResId : String? = null
    val discPrice: Long? = null
    val qty: MutableMap<String, Long>? = null
    val SUBSCRIBE: MutableMap<String, Boolean>? = null
    constructor()
    constructor(name: String, price: String, imageResId: String)
    constructor(name: String, price: String, imageResId: String, qty:Int)
    constructor(name: String, price: String, imageResId: String, discPrice: String?)
}
