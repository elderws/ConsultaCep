package net.overclock.consultacep

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.overclock.consultacep.Greeting
import net.overclock.consultacep.ui.CepViewModel
import net.overclock.consultacep.ui.CepViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CepViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "Hello from shared module: ${Greeting().greet()}")

        val editCep = findViewById<EditText>(R.id.editCep)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val txtCep = findViewById<TextView>(R.id.txtCep)
        val txtLogradouro = findViewById<TextView>(R.id.txtLogradouro)
        val txtBairro = findViewById<TextView>(R.id.txtBairro)
        val txtLocalidade = findViewById<TextView>(R.id.txtLocalidade)
        val txtUf = findViewById<TextView>(R.id.txtUf)

        viewModel = ViewModelProvider(this, CepViewModelFactory())[CepViewModel::class.java]

        viewModel.formState.observe(this@MainActivity, Observer {
            val formState = it ?: return@Observer

            btnBuscar.isEnabled = formState.isDataValid
            btnBuscar.visibility = if (formState.isLoading) View.GONE else View.VISIBLE
            progress.visibility = if (formState.isLoading) View.VISIBLE else View.GONE

            if (formState.isLoading) {
                editCep.isEnabled = false
                btnBuscar.isEnabled = false
                txtCep.text = ""
                txtLogradouro.text = ""
                txtBairro.text = ""
                txtLocalidade.text = ""
                txtUf.text = ""
            } else {
                editCep.isEnabled = true
                btnBuscar.isEnabled = true
                txtCep.text = getString(R.string.cep, formState.endereco?.cep ?: "")
                txtLogradouro.text = getString(R.string.logradouro, formState.endereco?.logradouro ?: "")
                txtBairro.text = getString(R.string.bairro, formState.endereco?.bairro ?: "")
                txtLocalidade.text = getString(R.string.localidade, formState.endereco?.localidade ?: "")
                txtUf.text = getString(R.string.uf, formState.endereco?.uf ?: "")
            }
        })

        editCep.apply {
            addTextChangedListener(object : TextWatcher {
                private var isUpdating = false

                override fun afterTextChanged(editable: Editable?) {
                    if (isUpdating || editable == null) return

                    isUpdating = true
                    val filteredValue = editable.toString().replace("\\D".toRegex(), "")
                    var formattedValue = ""

                    filteredValue.mapIndexed { index, char ->
                        if (index == 5) {
                            formattedValue += "-"
                        }
                        if (index <= 7) {
                            formattedValue += char
                        }
                    }

                    editCep.setText(formattedValue)
                    editCep.setSelection(formattedValue.length)
                    isUpdating = false

                    viewModel.onCepChanged(formattedValue)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.buscarCep(editCep.text.toString())
                }
                false
            }
        }

        btnBuscar.setOnClickListener {
            val cep = editCep.text.toString()
            viewModel.buscarCep(cep)
            editCep.setText("")
        }
    }
}