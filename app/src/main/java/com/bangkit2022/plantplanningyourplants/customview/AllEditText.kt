package com.bangkit2022.plantplanningyourplants.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit2022.plantplanningyourplants.R
import com.bangkit2022.plantplanningyourplants.emailValid

class AllEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var usernameImage : Drawable
    private lateinit var emailImage : Drawable
    private lateinit var passwordImage : Drawable
    private lateinit var buttonClearImage : Drawable
    private lateinit var enabledBackground : Drawable

    private var isUsername : Boolean = false
    private var isEmail : Boolean = false
    private var isPassword : Boolean = false

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

        setPadding(32, 32, 32, 32)

        background = enabledBackground
        gravity = Gravity.CENTER_VERTICAL
        compoundDrawablePadding = 16
    }

    private fun init(attributeSet : AttributeSet?, defStyleAttribute : Int = 0) {
        val attribute = context.obtainStyledAttributes(attributeSet, R.styleable.AllEditText, defStyleAttribute, 0)

        isEmail = attribute.getBoolean(R.styleable.AllEditText_email, false)
        isPassword = attribute.getBoolean(R.styleable.AllEditText_password, false)

        usernameImage = ContextCompat.getDrawable(context, R.drawable.icon_person) as Drawable
        emailImage = ContextCompat.getDrawable(context, R.drawable.icon_mail) as Drawable
        passwordImage = ContextCompat.getDrawable(context, R.drawable.icon_locked) as Drawable
        buttonClearImage = ContextCompat.getDrawable(context, R.drawable.icon_clear) as Drawable
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.et_color_greenout) as Drawable

        if (isUsername) {
            setButtonDrawables(startOfTheText = usernameImage)
        } else if (isEmail) {
            setButtonDrawables(startOfTheText = emailImage)
        } else if (isPassword) {
            setButtonDrawables(startOfTheText = passwordImage)
        }

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence : CharSequence, start : Int, count : Int, after : Int) {
                // Do Nothing.
            }

            override fun onTextChanged(charSequence : CharSequence, start : Int, count : Int, before : Int) {
                val input = charSequence.toString()
                val emailError = resources.getString(R.string.error_wrong_or_invalid_input_email_address)
                val passwordError = resources.getString(R.string.error_wrong_or_invalid_input_password)

                if (charSequence.toString().isNotEmpty()) {
                    showClearButton()
                } else {
                    hideClearButton()
                }

                error = if (isPassword  &&  input.length < 6  &&  input.isNotEmpty()) {
                    passwordError
                } else if (isEmail  &&  !input.emailValid()) {
                    emailError
                } else {
                    null
                }
            }

            override fun afterTextChanged(editable : Editable) {
                // Do Nothing.
            }
        })

        attribute.recycle()
    }

    private fun showClearButton() {
        when {
            isUsername -> setButtonDrawables(startOfTheText = usernameImage, endOfTheText = buttonClearImage)
            isEmail -> setButtonDrawables(startOfTheText = emailImage, endOfTheText = buttonClearImage)
            isPassword -> setButtonDrawables(startOfTheText = passwordImage, endOfTheText = buttonClearImage)

            else -> setButtonDrawables(endOfTheText = buttonClearImage)
        }
    }

    private fun hideClearButton() {
        when {
            isUsername -> setButtonDrawables(startOfTheText = usernameImage)
            isEmail -> setButtonDrawables(startOfTheText = emailImage)
            isPassword -> setButtonDrawables(startOfTheText = passwordImage)

            else -> setButtonDrawables()
        }
    }

    private fun setButtonDrawables(startOfTheText : Drawable? = null, endOfTheText : Drawable? = null, topOfTheText : Drawable? = null, bottomOfTheText : Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, endOfTheText, topOfTheText, bottomOfTheText)
    }

    override fun onTouch(view : View?, motionEvent : MotionEvent) : Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart : Float
            val clearButtonEnd : Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd  =  (buttonClearImage.intrinsicWidth + paddingStart).toFloat()

                when {
                    motionEvent.x < clearButtonEnd  ->  isClearButtonClicked = true
                }
            } else {
                clearButtonStart  =  (width - paddingEnd - buttonClearImage.intrinsicWidth).toFloat()

                when {
                    motionEvent.x > clearButtonStart  ->  isClearButtonClicked = true
                }
            }

            if (isClearButtonClicked) {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        buttonClearImage  =  ContextCompat.getDrawable(context, R.drawable.icon_clear) as Drawable

                        showClearButton()

                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        buttonClearImage = ContextCompat.getDrawable(context, R.drawable.icon_clear) as Drawable

                        when {
                            text != null  ->  text?.clear()
                        }

                        hideClearButton()

                        return true
                    }

                    else -> return false
                }
            } else {
                return false
            }
        }

        return false
    }
}