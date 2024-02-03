package com.example.milktickproject

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var balance: String? = null
    constructor()
    constructor(name:String, email:String, uid:String, balance: String){
        this.name = name
        this.email = email
        this.uid = uid
        this.balance = balance
    }
}