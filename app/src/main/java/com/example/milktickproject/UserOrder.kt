package com.example.milktickproject

class UserOrder {
    var buffalomilk: Int? = null
    var cowmilk: Int? = null
    var desicowmilk: Int? = null
    var doubletonedmilk: Int? = null
    var slimmilk: Int? = null
    var tonedmilk: Int? = null
    constructor()
    constructor(buffalomilk:Int, cowmilk:Int, desicowmilk:Int, doubletonedmilk:Int, slimmilk:Int, tonedmilk:Int){
        this.buffalomilk = buffalomilk
        this.cowmilk = cowmilk
        this.desicowmilk = desicowmilk
        this.doubletonedmilk = doubletonedmilk
        this.slimmilk = slimmilk
        this.tonedmilk = tonedmilk
    }
}