package com.teamnull.taskcity.csvclient



class AddressData (val city:String, val street:String, val number:String, val rowData:String, var x:Float, var y:Float ){

    fun GetAddress (): String{
        return "${city} ${street} ${number}"
    }
    fun GetRow ():String{
        return "${rowData};${x};${y}"
    }
    fun SetCoordinate(xx:Float,  yy:Float){
        x = xx
        y = yy
    }

}
