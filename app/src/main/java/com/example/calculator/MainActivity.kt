package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var inputValue1 :Double? = 0.0
    private var inputValue2 :Double? = null
    private var currentOperator :Operator? = null
    private var result :Double? = null
    private var equation :StringBuilder = StringBuilder().append(ZERO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners(){
        for (button in getNumericButton()){
            button.setOnClickListener{onNumberClicked(button.text.toString())}
        }
        with(binding){
            zeroBtn.setOnClickListener { onZeroClicked() }
            doubleZeroBtn.setOnClickListener { onDoubleZeroClicked() }
            dotBtn.setOnClickListener { onDecimalPointClicked() }
            plusBtn.setOnClickListener { onOperatorClicked(Operator.ADDITION) }
            minusBtn.setOnClickListener { onOperatorClicked(Operator.SUBTRACTION) }
            multiplyBtn.setOnClickListener { onOperatorClicked(Operator.MULTIPLICATION) }
            divideBtn.setOnClickListener { onOperatorClicked(Operator.DIVISION) }
            acButton.setOnClickListener { onAllClearClicked() }
            equalBtn.setOnClickListener { onEqualClicked() }
            plusMinusBtn.setOnClickListener { onPlusMinusClicked() }
            percentageBtn.setOnClickListener { onPercentageCLicked() }
        }
    }

    private fun onPercentageCLicked(){
        if (inputValue2 ==null ){
            val percentage = getInputValue1() / 100
            inputValue1 = percentage
             equation.clear().append(percentage)
            updateInputOnDisplay()
        }else{
            val percentageOfValue1 = (getInputValue1() * getInputValue2() ) / 100
            val percentageOfValue2 = getInputValue2()/100
            result = when(requireNotNull(currentOperator)){
                Operator.ADDITION -> getInputValue1() + percentageOfValue1
                Operator.SUBTRACTION -> getInputValue1() - percentageOfValue1
                Operator.MULTIPLICATION -> getInputValue1() * percentageOfValue2
                Operator.DIVISION -> getInputValue1() / percentageOfValue2
            }
            equation.clear().append(ZERO)
            updateResultOnDisplay(isPercentage = true )
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        }
    }

    private fun onPlusMinusClicked(){
        if (equation.startsWith(MINUS)){
            equation.deleteCharAt(0)
        }else{
            equation.insert(0, MINUS)
        }
        setInput()
        updateInputOnDisplay()
    }

    private fun onAllClearClicked(){
       inputValue1  = 0.0
        inputValue2  = null
        currentOperator = null
        result =null
        equation.clear().append(ZERO)
        clearDisplay()
    }
    private fun onOperatorClicked(operator:Operator){
        onEqualClicked()
        currentOperator = operator
    }

    private fun onEqualClicked(){
        if (inputValue2 != null){
            result = calculate()
            equation.clear().append(ZERO)
            updateResultOnDisplay()
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        }else{
            equation.clear().append(ZERO)

        }
    }

    private fun calculate():Double{
        return when(requireNotNull(currentOperator)){
            Operator.ADDITION -> getInputValue1() + getInputValue2()
            Operator.SUBTRACTION -> getInputValue1() - getInputValue2()
            Operator.MULTIPLICATION -> getInputValue1() * getInputValue2()
            Operator.DIVISION -> getInputValue1() / getInputValue2()
        }
    }

    private fun onZeroClicked(){
        if (equation.startsWith(ZERO))return
        onNumberClicked(ZERO)
    }
    private fun onDecimalPointClicked(){
        if (equation.contains(DECIMAL_POINT))return
        equation.append(DECIMAL_POINT)
        setInput()
        updateInputOnDisplay()
    }
    private fun onDoubleZeroClicked(){
        if (equation.startsWith(ZERO))return
        onNumberClicked(DOUBLE_ZERO)
    }

    private fun getNumericButton() = with(binding){
        arrayOf(
            oneBtn,
            twoBtn,
            threeBtn,
            fourBtn,
            fiveBtn,
            sixBtn,
            sevenBtn,
            eightButton,
            nineBtn
        )
    }

    private fun onNumberClicked(numberText : String){
        if (equation.startsWith(ZERO)){
            equation.deleteCharAt(0)
        }else if (equation.startsWith("$MINUS$ZERO")){
          equation.deleteCharAt(1)
        }
        equation.append(numberText)
        setInput()
        updateInputOnDisplay()
    }

    private fun setInput(){
        if (currentOperator == null){
            inputValue1 = equation.toString().toDouble()
        }else{
            inputValue2 = equation.toString().toDouble()
        }
    }

    private fun clearDisplay(){
        with(binding){
            tvInput.text = getFormattedDisplayValue(value = getInputValue1())
            tvResult.text = null
        }
    }

    private fun updateResultOnDisplay (isPercentage: Boolean = false){
        binding.tvInput.text = getFormattedDisplayValue(value = result)
        var input2Text = getFormattedDisplayValue(value = getInputValue2())
        if (isPercentage) input2Text = "$input2Text${getString(R.string.percentage)}"
        binding.tvResult.text =String.format(
            "%s %s %s",
            getFormattedDisplayValue(value = getInputValue1()),
            getOperatorSymbol(),
            input2Text

        )
    }

    private fun updateInputOnDisplay(){
        if (result == null){
            binding.tvResult.text = null
        }
        binding.tvInput.text = equation
    }

    private fun getInputValue1() = inputValue1 ?: 0.0
    private fun getInputValue2() = inputValue2 ?: 0.0

    private fun getOperatorSymbol():String{
        return when(requireNotNull(currentOperator)){
            Operator.ADDITION -> getString(R.string.addition)
            Operator.SUBTRACTION -> getString(R.string.subtraction)
            Operator.MULTIPLICATION -> getString(R.string.multiplication)
            Operator.DIVISION -> getString(R.string.division)
        }
    }

    private fun getFormattedDisplayValue(value: Double?):String?{
        val originalValue = value ?: return null
        return if (originalValue % 1 == 0.0){
            originalValue.toInt().toString()
        }else{
            originalValue.toString()
        }
    }
    enum class Operator{
        ADDITION,SUBTRACTION,MULTIPLICATION,DIVISION
    }
private companion object{
    const val DECIMAL_POINT = "."
    const val ZERO = "0"
    const val DOUBLE_ZERO = "00"
    const val MINUS = "-"
}
}