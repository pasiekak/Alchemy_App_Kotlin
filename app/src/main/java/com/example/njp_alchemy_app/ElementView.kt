package com.example.njp_alchemy_app

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import com.example.njp_alchemy_app.entity.Element

class ElementView(
    context: Context,
    val element: Element,
    private val alchemy: Alchemy
) : androidx.appcompat.widget.AppCompatImageView(context) {

    private var initialX = 0f
    private var initialY = 0f
    private var offsetX = 0f
    private var offsetY = 0f


    init {
        element.IdRes?.let { setImageResource(it) }

        // Przypisz listener obsługujący zdarzenia dotykowe
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (parent is FrameLayout) {
                        initialX = view.x
                        initialY = view.y
                        offsetX = event.rawX - view.x
                        offsetY = event.rawY - view.y
                    }
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (parent is FrameLayout) {
                        val frameLayoutParent:FrameLayout = parent as FrameLayout
                        val newX = event.rawX - offsetX
                        val newY = event.rawY - offsetY
                        val maxX = frameLayoutParent.width - view.width
                        val maxY = frameLayoutParent.height - view.height
                        view.x = newX.coerceIn(0f, maxX.toFloat())
                        view.y = newY.coerceIn(0f, maxY.toFloat())
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()

                    if (this.parent is TableRow || this.parent is TableLayout || this.parent is LinearLayout) {
                        alchemy.createNewElementInFrameLayout(element)
                    }
                    if (parent is FrameLayout) {
                        val frameLayoutParent = parent as FrameLayout
                        val hitRect = Rect()
                        view.getHitRect(hitRect)

                        for (i in 0 until frameLayoutParent.childCount) {
                            val child = frameLayoutParent.getChildAt(i)
                            if (view != child && child != null) {
                                val secondRect = Rect()
                                child.getHitRect(secondRect)
                                if (hitRect.intersect(secondRect)) {
                                    val v1 = view as ElementView
                                    val v2 = child as ElementView
                                    alchemy.handleCollision(v1, v2)
                                }
                            }
                        }
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
