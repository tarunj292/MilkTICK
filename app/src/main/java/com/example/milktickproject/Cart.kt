package com.example.milktickproject

class Cart {
    var name: String? = null
    var price: String? = null
    var qty: Int? = null
    var imageResId: String? = null

    constructor()
    constructor(name: String, price: String, qty: Int, imageResId: String)
}