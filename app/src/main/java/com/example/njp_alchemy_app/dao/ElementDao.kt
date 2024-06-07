package com.example.njp_alchemy_app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.njp_alchemy_app.entity.Element

@Dao
interface ElementDao {
    @Query("SELECT * FROM Element")
    fun getAll(): List<Element>

    @Query("SELECT * FROM Element WHERE name = :name")
    fun getOneByName(name: String): Element?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(element: Element)
}