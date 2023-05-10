package code.with.cal.kotlincalculatorapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class MainActivity : AppCompatActivity()
{
    private var canAddDecimal = true
    var stack_array = ArrayDeque<String>()
    val round_list = listOf("#", "#.#", "#.##", "#.###", "#.####", "#.#####", "#.######", "#.#######")
    var round: String? = "4"
    val REQUEST_CODE = 10000

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun settingsAction(view: View){
        val i = Intent(this, SettingsActivity::class.java)
        startActivityForResult(i,REQUEST_CODE)
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra("rounding")) {
                    round = data.extras?.getString("rounding")
                }
                if (data.hasExtra("bgColour")) {
                    val cl: String? = data.extras?.getString("bgColour")
                    val stack: TextView = findViewById(R.id.stack)
                    val bg: ConstraintLayout = findViewById(R.id.bg)

                    if (cl == "white"){
                        stack.setBackgroundColor(Color.parseColor("#ffffff"))
                        bg.setBackgroundColor(Color.parseColor("#ffffff"))
                        workingsTV.setBackgroundColor(Color.parseColor("#ffffff"))
                        workingsTV.setTextColor(Color.parseColor("#FF222222"))
                        stack.setTextColor(Color.parseColor("#FF222222"))
                    }
                    else if (cl == "black") {
                        stack.setBackgroundColor(Color.parseColor("#FF222222"))
                        bg.setBackgroundColor(Color.parseColor("#FF222222"))
                        workingsTV.setBackgroundColor(Color.parseColor("#FF222222"))
                        workingsTV.setTextColor(Color.parseColor("#ffffff"))
                        stack.setTextColor(Color.parseColor("#ffffff"))
                    }
                }
            }
        }
    }

    fun numberAction(view: View)
    {
        if(view is Button)
        {
            if(view.text == ".")
            {
                if(canAddDecimal)
                    workingsTV.append(view.text)
                canAddDecimal = false
            }
            else
                workingsTV.append(view.text)
        }
    }

    fun operationAction(view: View)
    {
        if(view is Button && stack_array.size > 0)
        {
            calculateResults(view.text)
            stack.text = stack_array.joinToString(" | ")
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View)
    {
        stack_array.clear()
        workingsTV.text = ""
        stack.text = ""
    }

    fun backSpaceAction(view: View)
    {
        val length = workingsTV.length()
        if(length > 0)
            workingsTV.text = workingsTV.text.subSequence(0, length - 1)
    }

    fun enterAction(view: View)
    {
        var num = workingsTV.text
        if (num.isEmpty())
        {
            if (stack_array.isEmpty() == false) {
                val last_added = stack_array.first
                stack_array.push(last_added)
            }
        }
        else
            stack_array.push(num.toString().toFloat().toString())
        stack.text = stack_array.joinToString(" | ")
        workingsTV.text = ""
        canAddDecimal = true
    }

    fun SwapAction(view: View)
    {
        println("zamienione")
        if (stack_array.size > 1){
            val first = stack_array.pollFirst()
            val sec = stack_array.pollFirst()
            stack_array.push(first)
            stack_array.push(sec)
            stack.text = stack_array.joinToString(" | ")
        }
    }

    fun DropAction(view: View)
    {
        if (stack_array.size > 0){
            stack_array.pop()
            stack.text = stack_array.joinToString(" | ")
        }
    }

    private fun calculateResults(op: CharSequence): ArrayDeque<String>
    {
        var first_elem = stack_array.pollFirst()
        var result : Float = 0.0F
        var first : Float = first_elem.toString().toFloat()
        var df = DecimalFormat(round_list[round?.toInt()!!])
        df.roundingMode = RoundingMode.CEILING

        if (stack_array.size == 0){
            if (op == "+/-"){
                result = first * -1
                stack_array.push(result.toString())
            }
            else if (op == "√"){
                result = sqrt(first)
                stack_array.push(df.format(result).toString())
            }
            else
                stack_array.push(first_elem)
        }
        else{
            var sec_elem = stack_array.pollFirst()
            var sec : Float = sec_elem.toString().toFloat()

            if (op == "+/-"){
                result = first * -1
                stack_array.push(sec_elem)
                stack_array.push(result.toString())
            }
            else{
                when (op) {
                    "/" -> result = first / sec
                    "x" -> result = first * sec
                    "-" -> result = first - sec
                    "+" -> result = first + sec
                    "y^a" -> result = first.pow(sec)
                    "√" -> {
                        result = sqrt(first)
                        stack_array.push(sec_elem)
                    }
                }
                stack_array.push(df.format(result).toString())
            }
        }
        return stack_array
    }
}



















