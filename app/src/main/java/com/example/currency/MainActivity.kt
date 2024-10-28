package com.example.currency

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var edt1: EditText
    private lateinit var edt2: EditText
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private var lastFocusedEditText: EditText? = null // Biến lưu lại EditText được nhập cuối cùng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt1 = findViewById(R.id.edt1)
        edt2 = findViewById(R.id.edt2)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)

        val arrayList = arrayListOf("VietNam - Dong", "China - Yuan", "Europe - Euro", "Japan - Yen", "Korea - Won")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner2.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Sử dụng TextWatcher cho edt1
        edt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateConversionFromEdt1()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Sử dụng TextWatcher cho edt2
        edt2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateConversionFromEdt2()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Lắng nghe sự kiện focus của EditText
        edt1.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedEditText = edt1
        }

        edt2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) lastFocusedEditText = edt2
        }
    }

    // Cập nhật giá trị cho edt1 hoặc edt2 dựa trên EditText được focus cuối cùng
    private fun updateConversion() {
        if (lastFocusedEditText == edt1) {
            updateConversionFromEdt1()
        } else if (lastFocusedEditText == edt2) {
            updateConversionFromEdt2()
        }
    }

    // Cập nhật giá trị cho edt2 dựa trên edt1
    private fun updateConversionFromEdt1() {
        val inputValue = edt1.text.toString().toDoubleOrNull()
        if (inputValue != null) {
            val coinFrom = spinner1.selectedItem.toString()
            val coinTo = spinner2.selectedItem.toString()
            val convertedValue = convertCurrency(inputValue, coinFrom, coinTo)
            edt2.setText(String.format("%.2f", convertedValue))
        }
    }

    // Cập nhật giá trị cho edt1 dựa trên edt2
    private fun updateConversionFromEdt2() {
        val inputValue = edt2.text.toString().toDoubleOrNull()
        if (inputValue != null) {
            val coinFrom = spinner2.selectedItem.toString()
            val coinTo = spinner1.selectedItem.toString()
            val convertedValue = convertCurrency(inputValue, coinFrom, coinTo)
            edt1.setText(String.format("%.2f", convertedValue))
        }
    }

    // Hàm chuyển đổi tiền tệ
    private fun convertCurrency(value: Double, from: String, to: String): Double {
        return when {
            from == "VietNam - Dong" && to == "VietNam - Dong" -> value
            from == "VietNam - Dong" && to == "China - Yuan" -> value * 0.0002805
            from == "VietNam - Dong" && to == "Europe - Euro" -> value * 0.00003652
            from == "VietNam - Dong" && to == "Japan - Yen" -> value * 0.006011
            from == "VietNam - Dong" && to == "Korea - Won" -> value * 0.05435
            from == "China - Yuan" && to == "VietNam - Dong" -> value / 0.0002805
            from == "China - Yuan" && to == "China - Yuan" -> value
            from == "China - Yuan" && to == "Europe - Euro" -> value * 0.1302
            from == "China - Yuan" && to == "Japan - Yen" -> value * 21.4282
            from == "China - Yuan" && to == "Korea - Won" -> value * 193.7461
            from == "Europe - Euro" && to == "VietNam - Dong" -> value * 27382.4925
            from == "Europe - Euro" && to == "China - Yuan" -> value * 7.6813
            from == "Europe - Euro" && to == "Europe - Euro" -> value
            from == "Europe - Euro" && to == "Japan - Yen" -> value * 164.5968
            from == "Europe - Euro" && to == "Korea - Won" -> value * 1488.2277
            from == "Japan - Yen" && to == "VietNam - Dong" -> value / 0.006011
            from == "Japan - Yen" && to == "China - Yuan" -> value / 21.4282
            from == "Japan - Yen" && to == "Europe - Euro" -> value / 164.5968
            from == "Japan - Yen" && to == "Japan - Yen" -> value
            from == "Japan - Yen" && to == "Korea - Won" -> value * 9.0417
            from == "Korea - Won" && to == "VietNam - Dong" -> value / 0.05435
            from == "Korea - Won" && to == "China - Yuan" -> value / 193.7461
            from == "Korea - Won" && to == "Europe - Euro" -> value / 1488.2277
            from == "Korea - Won" && to == "Japan - Yen" -> value / 9.0417
            from == "Korea - Won" && to == "Korea - Won" -> value
            else -> 0.0
        }
    }
}