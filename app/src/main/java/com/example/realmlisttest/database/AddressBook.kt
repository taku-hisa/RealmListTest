package com.example.realmlisttest.database

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class AddressBook : RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var bookName : String = ""
    var list:RealmList<Address> = RealmList()
}