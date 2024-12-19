package com.example.agenda

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var textName: EditText
    private lateinit var textSurname: EditText
    private lateinit var textNumber: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular vistas
        textName = findViewById(R.id.text_nombre)
        textSurname = findViewById(R.id.text_apellido)
        textNumber = findViewById(R.id.number)
        addButton = findViewById(R.id.añadir_contacto)

        // Configurar acción del botón
        addButton.setOnClickListener {
            guardarDatos()
        }
    }

    private fun guardarDatos() {
        val nombres = textName.text.toString().trim()
        val apellidos = textSurname.text.toString().trim()
        val numeros = textNumber.text.toString().trim()

        // Validar campos vacíos
        if (nombres.isEmpty() || apellidos.isEmpty() || numeros.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.100.70/agenda/insertar.php"

        // Crear una solicitud POST
        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // Verificar respuesta del servidor
                if (response.contains("success", ignoreCase = true)) {
                    Toast.makeText(this, "Contacto registrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al registrar: $response", Toast.LENGTH_SHORT).show()
                }

                // Limpiar los campos después de insertar
                textName.setText("")
                textSurname.setText("")
                textNumber.setText("")
            },
            Response.ErrorListener { error ->
                val message = error.message ?: "Error desconocido"
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                // Enviar parámetros al servidor
                val params = HashMap<String, String>()
                params["nombres"] = nombres
                params["apellidos"] = apellidos
                params["numeros"] = numeros
                return params
            }
        }

        // Agregar la solicitud a la cola de Volley
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}



