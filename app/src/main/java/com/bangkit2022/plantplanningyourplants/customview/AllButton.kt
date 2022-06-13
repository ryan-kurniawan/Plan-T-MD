package com.bangkit2022.plantplanningyourplants.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.bangkit2022.plantplanningyourplants.R

class AllButton : AppCompatButton {
    private lateinit var enabledBackground : Drawable
    private lateinit var disabledBackground : Drawable

    private var txtColor : Int = 0
    private var isOutlined = false

    constructor(context : Context) : super(context) {
        init(null)
    }

    constructor(context : Context, attributeSet : AttributeSet) : super(context, attributeSet) {
        init(attributeSet)
    }

    constructor(context : Context, attributeSet : AttributeSet, defStyleAttribute : Int) : super(context, attributeSet, defStyleAttribute) {
        init(attributeSet, defStyleAttribute)
    }

    override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)

        background = if (isEnabled) {
            enabledBackground
        } else {
            disabledBackground
        }

        setTextColor(txtColor)

        text = resources.getString(if (isOutlined) {
            R.string.register
        } else {
            R.string.login
        })
    }

    private fun init(attributeSet : AttributeSet?, defStyleAttribute : Int = 0) {
        val attribute = context.obtainStyledAttributes(attributeSet, R.styleable.AllButton, defStyleAttribute, 0)

        isOutlined = attribute.getBoolean(R.styleable.AllEditText_email, false)

        txtColor = ContextCompat.getColor(context, if (isOutlined) {
            R.color.white
        } else {
            R.color.white
        })

        enabledBackground = ContextCompat.getDrawable(context, if (isOutlined) {
            R.drawable.button_color_greenout3
        } else {
            R.drawable.button_color_greenbg
        }) as Drawable

        disabledBackground = ContextCompat.getDrawable(context, R.drawable.button_color_green2) as Drawable

        attribute.recycle()
    }
}