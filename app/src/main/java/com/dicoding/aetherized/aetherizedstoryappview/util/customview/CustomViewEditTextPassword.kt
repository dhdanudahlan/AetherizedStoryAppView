package com.dicoding.aetherized.aetherizedstoryappview.util.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.aetherized.aetherizedstoryappview.R

class CustomViewEditTextPassword : AppCompatEditText, View.OnTouchListener, TextWatcher {

    private lateinit var toggleButtonImage: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        toggleButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_show_password) as Drawable
        setOnTouchListener(this)
        setButtonDrawables(endOfTheText = toggleButtonImage)
        addTextChangedListener(this)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            var isToggleButtonClicked = false

            val toggleButtonStart: Float = (width - paddingEnd - toggleButtonImage.intrinsicWidth).toFloat()
            if (event!!.x > toggleButtonStart) {
                isToggleButtonClicked = true
            }

            return if (isToggleButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        togglePasswordVisibility()
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        togglePasswordVisibility()
                        true
                    }

                    else -> false
                }
            } else false
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_hide_password) as Drawable
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_show_password) as Drawable
        }
        setButtonDrawables(endOfTheText = toggleButtonImage)
        setSelection(text?.length ?: 0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable?) {
        error = if (s != null && s.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }
}