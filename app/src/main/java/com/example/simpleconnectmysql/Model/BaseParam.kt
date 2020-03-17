package com.example.simpleconnectmysql.Model

import org.apache.http.NameValuePair


class BaseParam() : NameValuePair {

    private var name:String = ""
    private var value:String = ""
    constructor( name:String = "", value:String=""):this(){
        this.name = name
        this.value = value
    }

    override fun getName(): String {
        return name
    }

    override fun getValue(): String {
        return value
    }
}