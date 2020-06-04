package com.example.peruapptest.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Place")
class PlaceEntity {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    @ColumnInfo(name = "idfirebase")
    var idFirebase: String? = null
    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null
    @ColumnInfo(name = "descripcionLugar")
    var descripcionLugar: String? = null
    @ColumnInfo(name = "imageURL")
    var imageURL: String? = null


}