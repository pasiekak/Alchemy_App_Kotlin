package com.example.njp_alchemy_app.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Element(
    @PrimaryKey @ColumnInfo(name = "icon_name") val icon_name: String,
    @ColumnInfo(name = "IdRes") val IdRes: Int?,
    @ColumnInfo(name = "polish_name") val polish_name: String,
)