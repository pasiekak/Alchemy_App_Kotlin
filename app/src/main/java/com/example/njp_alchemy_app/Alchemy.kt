package com.example.njp_alchemy_app

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import com.example.njp_alchemy_app.entity.Combination
import com.example.njp_alchemy_app.entity.Element
import kotlin.math.ceil
import kotlin.random.Random

class Alchemy(
    private val context: Context,
    private val database: AppDatabase,
    private val tableLayout: TableLayout,
    private val frameLayout: FrameLayout,
    private val scoreTextView: TextView,
    private val allElements: List<Element>,
    private var knownElements: MutableList<Element>,
    private val combinations: List<Combination>) {

    private val numberOfElements:Int = allElements.count()
    private var discoveredElements:Int = 4;

    fun refreshGame() {
        knownElements = mutableListOf(
            Element(R.drawable.fire_icon, "Ogień"),
            Element(R.drawable.water_icon, "Woda"),
            Element(R.drawable.air_icon, "Powietrze"),
            Element(R.drawable.dirt_icon, "Ziemia")
        )
        discoveredElements = 4
        refreshTable()
        clearCraftSpace()

    }
    fun refreshTable() {
        tableLayout.removeAllViews()

        val numberOfRows = ceil(knownElements.size / 4.0).toInt()
        scoreTextView.text = "Odkryto: $discoveredElements/$numberOfElements"

        for (i in 0 until numberOfRows) {
            val tableRow = TableRow(context)
            tableRow.setPadding(10, 10, 10, 10)

            for (j in 0 until 4) {
                val index = i * 4 + j
                if (index < knownElements.size) {
                    val element = knownElements[index]

                    // Tworzenie LinearLayout pionowego
                    val linearLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(10, 10, 10, 10)
                        layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                    // Tworzenie ElementView (ImageView) i dodawanie do LinearLayout
                    val elementView = ElementView(context, element, this).apply {
                        layoutParams = LinearLayout.LayoutParams(128, 128).apply {
                            gravity = Gravity.CENTER
                        }
                    }
                    linearLayout.addView(elementView)

                    // Tworzenie TextView i dodawanie do LinearLayout
                    val textView = TextView(context).apply {
                        text = element.name

                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER
                        }
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        typeface = ResourcesCompat.getFont(context, R.font.baloo)
                    }
                    linearLayout.addView(textView)

                    // Dodawanie LinearLayout do TableRow
                    tableRow.addView(linearLayout)
                }
            }

            tableLayout.addView(tableRow)
        }
    }


    fun clearCraftSpace() {
        frameLayout.removeAllViews()
    }
    fun createNewElementInFrameLayout(name: String, IdRes: Int, x: Float? = null, y: Float? = null) {
        val element = ElementView(context, Element(IdRes, name), this)
        val maxX = frameLayout.width - 128
        val maxY = frameLayout.height - 128
        val randomX = x ?: Random.nextInt(0, maxX).toFloat()
        val randomY = y ?: Random.nextInt(0, maxY).toFloat()

        val layoutParams = FrameLayout.LayoutParams(128, 128)
        element.layoutParams = layoutParams

        frameLayout.addView(element)
        element.x = randomX
        element.y = randomY
    }
    fun handleCollision(v1: ElementView, v2: ElementView) {
        val result = combineElements(v1.element,v2.element)
        if (result != null) {
            val element = database.elementDao().getOneByName(result)
            if(element != null) {
                if(!knownElements.any { it.name == result }) {
                    discoveredElements += 1
                    knownElements.add(element)
                    refreshTable()
                    if (discoveredElements == numberOfElements) {
                        showEndgameDialog()
                    } else {
                        showNewElementDialog(element)
                    }
                }

                createNewElementInFrameLayout(element.name, element.IdRes, v1.x, v1.y)
                frameLayout.removeView(v1)
                frameLayout.removeView(v2)
            }
        }
    }
    fun combineElements(e1: Element, e2: Element): String? {
        return database.combinationDao().getCombination(e1.name, e2.name)
    }
    fun showNewElementDialog(newElement: Element) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Odkryto nowy element!!!")
        builder.setMessage("Odkryłeś element: ${newElement.name}")
        builder.setIcon(newElement.IdRes)
        builder.setPositiveButton("OK") {
            dialog, _ -> dialog.dismiss()
        }
        builder.show()
    }
    fun showEndgameDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Gratulacje!!!")
        builder.setMessage("Udało Ci się odkryć wszystkie elementy!")
        builder.setPositiveButton("OK") {
                dialog, _ -> dialog.dismiss()
        }
        builder.show()
    }
}
