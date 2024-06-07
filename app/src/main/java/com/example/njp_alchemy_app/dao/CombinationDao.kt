package com.example.njp_alchemy_app.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.njp_alchemy_app.entity.Combination

@Dao
interface CombinationDao {
    @Query("SELECT * FROM Combination")
    fun getAll(): List<Combination>

    @Query("SELECT result FROM Combination WHERE (ingredient_1 = :ingredient_1_name AND ingredient_2 = :ingredient_2_name) OR (ingredient_1 = :ingredient_2_name AND ingredient_2 = :ingredient_1_name) LIMIT 1")
    fun getCombination(ingredient_1_name: String, ingredient_2_name: String): String
}