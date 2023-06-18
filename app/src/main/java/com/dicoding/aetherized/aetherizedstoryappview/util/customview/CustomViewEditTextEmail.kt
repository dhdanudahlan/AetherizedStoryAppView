package com.dicoding.aetherized.aetherizedstoryappview.util.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.aetherized.aetherizedstoryappview.R

class CustomViewEditTextEmail : AppCompatEditText, View.OnTouchListener, TextWatcher {

    private lateinit var clearButtonImage: Drawable

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton() else hideClearButton()
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
        setButtonDrawables()
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
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event!!.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when {
                    event!!.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked) {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_close) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideClearButton()

                        return true
                    }
                    else -> return false
                }
            }else return false
        }
        return false
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        val emailPattern = ("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$").toRegex(RegexOption.IGNORE_CASE)
        return emailPattern.matches(email)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString().isNotEmpty()) {
            if (isValidEmail(s)) {
                showClearButton()
            } else {
                error = "Email format is incorrect"
            }
        } else {
            hideClearButton()
        }
    }

    override fun afterTextChanged(s: Editable?) {
        error = if (s != null && s.length < 8) {
            "Password must be at least 8 characters"
        } else {
            null
        }
    }
}