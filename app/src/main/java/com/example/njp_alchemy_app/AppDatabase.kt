package com.example.njp_alchemy_app

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.njp_alchemy_app.dao.CombinationDao
import com.example.njp_alchemy_app.dao.ElementDao
import com.example.njp_alchemy_app.entity.Combination
import com.example.njp_alchemy_app.entity.Element

@Database(entities = [Element::class, Combination::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun elementDao(): ElementDao
    abstract fun combinationDao(): CombinationDao
}
