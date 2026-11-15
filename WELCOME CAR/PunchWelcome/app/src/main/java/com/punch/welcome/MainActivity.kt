package com.punch.welcome

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var nameInput: EditText
    private lateinit var delayInput: EditText
    private lateinit var langSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (24 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }

        root.addView(TextView(this).apply {
            text = "Punch Welcome"
            textSize = 26f
            setTypeface(null, Typeface.BOLD)
        })
        root.addView(label("Your name (shown in greeting)"))
        nameInput = EditText(this).apply {
            setText(Prefs.name(this@MainActivity))
            setSingleLine()
        }
        root.addView(nameInput)

        root.addView(label("Language"))
        langSpinner = Spinner(this)
        langSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            listOf("Hinglish", "English", "Hindi")
        )
        langSpinner.setSelection(Prefs.lang(this))
        root.addView(langSpinner)

        root.addView(label("Delay after car starts (seconds)"))
        delayInput = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(Prefs.delay(this@MainActivity).toString())
        }
        root.addView(delayInput)

        val save = Button(this).apply { text = "Save" }
        val test = Button(this).apply { text = "Test welcome message now" }
        root.addView(save)
        root.addView(test)

        root.addView(TextView(this).apply {
            gravity = Gravity.START
            textSize = 13f
            setPadding(0, pad, 0, 0)
            text = "Important: in your head unit settings, allow this app to Auto-start " +
                "and turn off battery optimization for it. Otherwise it will not run at boot."
        })

        save.setOnClickListener { saveNow(); Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show() }
        test.setOnClickListener {
            saveNow()
            val i = Intent(this, GreetingService::class.java).putExtra("test", true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(i) else startService(i)
        }

        setContentView(android.widget.ScrollView(this).apply { addView(root) })
    }

    private fun label(t: String) = TextView(this).apply {
        text = t
        setPadding(0, 28, 0, 4)
    }

    private fun saveNow() {
        val name = nameInput.text.toString().trim().ifEmpty { "Boss" }
        val delay = delayInput.text.toString().toIntOrNull()?.coerceIn(0, 60) ?: 8
        Prefs.save(this, name, langSpinner.selectedItemPosition, delay)
    }
}
