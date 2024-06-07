package com.example.njp_alchemy_app.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Combination(
    @ColumnInfo(name = "ingredient_1") val ingredient_1: String,
    @ColumnInfo(name = "ingredient_2") val ingredient_2: String,
    @PrimaryKey @ColumnInfo(name = "result") val result: String,
)