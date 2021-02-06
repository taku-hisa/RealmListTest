package com.example.realmlisttest.database

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Address:RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var address :String = ""
}