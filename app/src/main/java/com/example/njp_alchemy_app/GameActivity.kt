package com.example.njp_alchemy_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.njp_alchemy_app.entity.Combination
import com.example.njp_alchemy_app.entity.Element
import java.lang.reflect.Field


class GameActivity : AppCompatActivity() {
    private lateinit var alchemy: Alchemy
    private lateinit var tableLayout: TableLayout
    private lateinit var frameLayout: FrameLayout
    private lateinit var scoreView: TextView
    private lateinit var appDatabase: AppDatabase
    private lateinit var allElements: List<Element>
    private lateinit var combinations: List<Combination>
    private lateinit var knownElements: MutableList<Element>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.game_activity)

        tableLayout = findViewById(R.id.elements_table)
        frameLayout = findViewById(R.id.craft_space)
        scoreView = findViewById(R.id.score_text)
        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database.db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .createFromAsset("database.db")
            .build()


        syncDatabaseWithIcons()

        allElements = appDatabase.elementDao().getAll()
        println("ELEMENTS $allElements")
        combinations = appDatabase.combinationDao().getAll()
        knownElements = mutableListOf(
            Element("fire_icon", R.drawable.fire_icon, "Ogień"),
            Element("water_icon",R.drawable.water_icon, "Woda"),
            Element("air_icon",R.drawable.air_icon, "Powietrze"),
            Element("dirt_icon",R.drawable.dirt_icon, "Ziemia")
        )

        alchemy = Alchemy(this@GameActivity, appDatabase, tableLayout, frameLayout, scoreView, allElements, knownElements, combinations)
        alchemy.refreshTable()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Code here executes on main thread after user presses button
            alchemy.refreshGame()
        }
        val clearCraftButton = findViewById<Button>(R.id.clear_craft_button);
        clearCraftButton.setOnClickListener {
            alchemy.clearCraftSpace()
        }
    }



    private fun syncDatabaseWithIcons() {
        val drawableClass: Class<*> = R.drawable::class.java
        val fields: Array<Field> = drawableClass.declaredFields
        for (field in fields) {
            try {
                val icon_name: String = field.getName()
                if(icon_name.contains("icon")) {
                    val IdRes: Int = field.getInt(null)
                    appDatabase.elementDao().updateIdRes(icon_name, IdRes)
                    println("DRAWABLE: $icon_name, $IdRes")
                }
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}
