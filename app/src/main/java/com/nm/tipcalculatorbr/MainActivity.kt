package com.nm.tipcalculatorbr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.nm.tipcalculatorbr.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    // Instância de objeto de ligação com acesso às exibições no layout activity_main.xml
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o objeto binding que para acessar as Views no layout activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Define a exibição de conteúdo da atividade para ser a exibição raiz do layout
        setContentView(binding.root)

        // Configura um ouvinte de clique no botão calcular para calcular a gorjeta
        binding.calculateButton.setOnClickListener { calculateTip() }

        // Configura uma chave ouvinte no campo EditText para ouvir os pressionamentos do botão "Enter"
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    /**
     * Calcula a gorjeta com base na entrada do usuário
     */
    private fun calculateTip() {
        // Obtém o valor decimal do campo EditText do custo do serviço
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        // Se o custo for nulo ou 0, exibe 0 gorjeta e sai dessa função
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // Obtém a porcentagem de gorjeta com base no botão de opção selecionado
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // Calcula a gorjeta
        var tip = tipPercentage * cost

        // Se a opção para arredondar a gorjeta estiver ativada (isChecked for true), arredonda a gorjeta. Caso contrário, não altera o valor da gorjeta
        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            // Pega o teto da gorjeta atual, que arredonda para o próximo inteiro, e armazena o novo valor na variável gorjeta
            tip = kotlin.math.ceil(tip)
        }

        // Exibe na tela o valor da gorjeta
        displayTip(tip)
    }

    /**
     * Formata o valor da gorjeta de acordo com a moeda local e exibe na tela
     */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    /**
     * Ouve as teclas para ocultar o teclado quando o botão "Enter" é tocado
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Oculta o teclado
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}