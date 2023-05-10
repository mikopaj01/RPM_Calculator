package code.with.cal.kotlincalculatorapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    var bgcolour = "black"
    var round = "4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val round_picker: NumberPicker = findViewById(R.id.roundPicker)

        val round_list = listOf("1", "0.1", "0.01", "0.001", "0.0001", "0.00001", "0.000001", "0.0000001")

        round_picker.minValue = 0
        round_picker.maxValue = 7
        round_picker.value = 4
        round_picker.displayedValues = round_list.toTypedArray()
        round_picker.wrapSelectorWheel = true;
    }

    fun setAction (view: View) {
        val r: NumberPicker = findViewById(R.id.roundPicker)
        round = r.value.toString()
    }

    fun setBackgroundAction (view: View) {
        if (view is Button){
            if (view.text == "White"){
                bgcolour = "white"
            }
            if (view.text == "Black"){
                bgcolour = "black"
            }
        }
    }

    override fun finish() {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("rounding", round)
        i.putExtra("bgColour", bgcolour)
        setResult(Activity.RESULT_OK, i)
        super.finish()
    }
}