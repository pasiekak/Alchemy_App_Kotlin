package com.example.njp_alchemy_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.njp_alchemy_app.entity.Element

@Dao
interface ElementDao {
    @Query("SELECT * FROM Element")
    fun getAll(): List<Element>

    @Query("SELECT * FROM Element WHERE icon_name = :icon_name")
    fun getOneByName(icon_name: String): Element?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(element: Element)

    @Query("UPDATE Element SET IdRes = :idRes WHERE icon_name = :iconName")
    fun updateIdRes(iconName: String, idRes: Int)
}