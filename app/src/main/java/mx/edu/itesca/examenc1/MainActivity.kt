package mx.edu.itesca.examenc1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var edtCantidad: EditText
    private lateinit var edtProducto: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var btnCompilar: Button
    private lateinit var tableLayout: TableLayout

    private var productos = mutableListOf<Triple<Int, String, Double>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtCantidad = findViewById(R.id.edtCantidad)
        edtProducto = findViewById(R.id.edtProducto)
        edtPrecio = findViewById(R.id.edtPrecio)
        btnCompilar = findViewById(R.id.btnCompilar)
        tableLayout = findViewById(R.id.tableLayout)

        btnCompilar.setOnClickListener {
            agregarProducto()
        }
    }

    private fun agregarProducto() {
        if (productos.size >= 3) {
            Toast.makeText(this, "Máximo 3 productos", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = edtCantidad.text.toString().toIntOrNull()
        val producto = edtProducto.text.toString()
        val precio = edtPrecio.text.toString().toDoubleOrNull()

        if (cantidad == null || precio == null || producto.isEmpty()) {
            Toast.makeText(this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show()
            return
        }

        val totalProducto = cantidad * precio
        productos.add(Triple(cantidad, producto, totalProducto))

        actualizarTabla()
        limpiarCampos()
    }

    private fun actualizarTabla() {
        tableLayout.removeViews(1, tableLayout.childCount - 1) // Elimina filas anteriores

        var subtotal = 0.0

        for (i in 0 until 3) { // Siempre mostramos 3 filas
            val fila = TableRow(this)
            if (i < productos.size) {
                val producto = productos[i]
                fila.addView(crearCelda((i + 1).toString()))
                fila.addView(crearCelda(producto.second))
                fila.addView(crearCelda(String.format("%.2f", producto.third)))
                subtotal += producto.third
            } else {
                // Fila vacía
                fila.addView(crearCelda(""))
                fila.addView(crearCelda(""))
                fila.addView(crearCelda(""))
            }
            tableLayout.addView(fila)
        }

        val iva = subtotal * 0.16
        val total = subtotal + iva

        // Agregar fila de Subtotal
        val filaSubtotal = TableRow(this)
        filaSubtotal.addView(crearCelda(""))
        filaSubtotal.addView(crearCelda("Sub-total"))
        filaSubtotal.addView(crearCelda(String.format("%.2f", subtotal)))
        tableLayout.addView(filaSubtotal)

        // Agregar fila de IVA
        val filaIVA = TableRow(this)
        filaIVA.addView(crearCelda(""))
        filaIVA.addView(crearCelda("IVA (16%)"))
        filaIVA.addView(crearCelda(String.format("%.2f", iva)))
        tableLayout.addView(filaIVA)

        // Agregar fila de Total
        val filaTotal = TableRow(this)
        filaTotal.addView(crearCelda(""))
        filaTotal.addView(crearCelda("Total"))
        filaTotal.addView(crearCelda(String.format("%.2f", total)))
        tableLayout.addView(filaTotal)
    }

    private fun crearCelda(texto: String): TextView {
        return TextView(this).apply {
            this.text = texto
            textSize = 16f
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
    }

    private fun limpiarCampos() {
        edtCantidad.text.clear()
        edtProducto.text.clear()
        edtPrecio.text.clear()
    }
}