package com.example.njp_alchemy_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Element(
    @PrimaryKey val IdRes: Int,
    @ColumnInfo(name = "name") val name: String
)