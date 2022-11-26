package net.azarquiel.listacompra

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import net.azarquiel.listacompra.adapter.CustomAdapter
import net.azarquiel.listacompra.databinding.ActivityMainBinding
import net.azarquiel.listacompra.model.Producto

class MainActivity : AppCompatActivity() {

    private lateinit var contadorSH: SharedPreferences
    private lateinit var adapter: CustomAdapter
    private lateinit var carroAL: java.util.ArrayList<Producto>
    private lateinit var carroSH: SharedPreferences
    private lateinit var binding: ActivityMainBinding





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)
        carroSH = getSharedPreferences("carro", MODE_PRIVATE)
        contadorSH = getSharedPreferences("contador", MODE_PRIVATE)
        initcontador()
        initRV()
        getProductos()
        showProductos()




        binding.fab.setOnClickListener {
            newProducto()
        }
        binding.fab2.setOnClickListener{
           buy()
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val product = carroAL[pos]
                Delete(product)
                Snackbar.make(
                    binding.cm.rvcarro,
                    product.nombre,
                    Snackbar.LENGTH_LONG
                ).setAction("Undo") {
                carroAL.add(product)
                    adapter.setProducto(carroAL)

                }.show()
            }
        }).attachToRecyclerView( binding.cm.rvcarro)

    }

    private fun buy() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Comprar")
        builder.setMessage("El precio total de tu compra es ${getprecio()} €")
        builder.setPositiveButton("Pagar") { dialog, which ->
                pagar()
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->

        }
        builder.show()

    }

    private fun pagar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Credit Card")
        val ll = LinearLayout(this)
        ll.setPadding(30,30,30,30)
        ll.orientation = LinearLayout.VERTICAL

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0,50,0,50)

        val textInputLayoutNombre = TextInputLayout(this)
        textInputLayoutNombre.layoutParams = lp
        val textInputLayoutCantidad = TextInputLayout(this)
        textInputLayoutCantidad.layoutParams = lp
        val textInputLayoutPrecio = TextInputLayout(this)
        textInputLayoutPrecio.layoutParams = lp
        val textInputLayoutpin = TextInputLayout(this)
        textInputLayoutPrecio.layoutParams = lp

        val etnombre = EditText(this)
        etnombre.setPadding(0, 80, 0, 80)
        etnombre.textSize = 20.0F
        etnombre.hint = "Nombre"
        textInputLayoutNombre.addView(etnombre)

        val etcantidad = EditText(this)
        etcantidad.setPadding(0, 80, 0, 80)
        etcantidad.textSize = 20.0F
        etcantidad.hint = "Numero de Tarjeta"
        textInputLayoutCantidad.addView(etcantidad)

        val etprecio = EditText(this)
        etprecio.setPadding(0, 80, 0, 80)
        etprecio.textSize = 20.0F
        etprecio.hint = "Fecha de caducidad"
        textInputLayoutPrecio.addView(etprecio)
        val etpin = EditText(this)
        etpin.setPadding(0, 80, 0, 80)
        etpin.textSize = 20.0F
        etpin.hint = "CVV"
        textInputLayoutpin.addView(etpin)


        ll.addView(textInputLayoutNombre)
        ll.addView(textInputLayoutCantidad)
        ll.addView(textInputLayoutPrecio)
        ll.addView(textInputLayoutpin)

        builder.setView(ll)

        builder.setPositiveButton("Save") { dialog, which ->

        }

        builder.setNegativeButton("Cancel") { dialog, which ->
        }
        builder.show()

    }

    private fun getprecio(): Float {

        val total: Double =carroAL.sumOf { p -> (p.precio).toDouble() }
        return total.toFloat()
    }






    private fun Delete(producto: Producto) {
        val editor = carroSH.edit()
        editor.remove("${producto.id}")
        editor.commit()
        carroAL.remove(producto)
        adapter.setProducto(carroAL)

    }

    private fun initcontador() {
        val c = contadorSH.getInt("c",-1)
        if (c == -1){
            val editor = contadorSH.edit()
            editor.putInt("c",0)
            editor.commit()
        }
    }

    private fun showProductos() {


            adapter.setProducto(carroAL)

    }

    private fun initRV() {
        adapter = CustomAdapter(this, R.layout.row)
        binding.cm.rvcarro.adapter = adapter
        binding.cm.rvcarro.layoutManager = LinearLayoutManager(this)
    }


    private fun getProductos() {
        val carroall= carroSH.all
        carroAL=ArrayList<Producto>()
        for ((key,value) in carroall) {
            val productoJson = value.toString()
            val producto = Gson().fromJson(productoJson, Producto::class.java)
            carroAL.add(producto)
        }
    }

    private fun newProducto() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Product")
        val ll = LinearLayout(this)
        ll.setPadding(30,30,30,30)
        ll.orientation = LinearLayout.VERTICAL

        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0,50,0,50)

        val textInputLayoutNombre = TextInputLayout(this)
        textInputLayoutNombre.layoutParams = lp
        val textInputLayoutCantidad = TextInputLayout(this)
        textInputLayoutCantidad.layoutParams = lp
        val textInputLayoutPrecio = TextInputLayout(this)
        textInputLayoutPrecio.layoutParams = lp

        val etnombre = EditText(this)
        etnombre.setPadding(0, 80, 0, 80)
        etnombre.textSize = 20.0F
        etnombre.hint = "Nombre"
        textInputLayoutNombre.addView(etnombre)

        val etcantidad = EditText(this)
        etcantidad.setPadding(0, 80, 0, 80)
        etcantidad.textSize = 20.0F
        etcantidad.hint = "Cantidad"
        textInputLayoutCantidad.addView(etcantidad)

        val etprecio = EditText(this)
        etprecio.setPadding(0, 80, 0, 80)
        etprecio.textSize = 20.0F
        etprecio.hint = "Precio"

        textInputLayoutPrecio.addView(etprecio)


        ll.addView(textInputLayoutNombre)
        ll.addView(textInputLayoutCantidad)
        ll.addView(textInputLayoutPrecio)

        builder.setView(ll)

        builder.setPositiveButton("Save") { dialog, which ->
            save(Producto(getcontador() + 1,etnombre.text.toString(),  if(etcantidad.toString().isBlank()){"0"} else etcantidad.text.toString(),etprecio.text.toString().toFloat()))
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
        }

        builder.show()

    }

    private fun getcontador(): Int {
            val c = contadorSH.getInt("c",-1)
        incrementarcont(c)
        return c
    }

    private fun incrementarcont(c: Int) {
        val editor = contadorSH.edit()
        editor.putInt("c",c+1)
        editor.commit()

    }

    private fun save(producto: Producto) {
        //save en Share
        val editor = carroSH.edit()
        editor.putString("${producto.id}", Gson().toJson(producto))
        editor.commit()
        //add ArrayList
        carroAL.add(0,producto)
        adapter.setProducto(carroAL)
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    fun OnclickProducto(v:View){
       val pr = v.tag as Producto
       val cv = v as CardView
        pr.comprado = true
        updateProduct(pr)
        if (cv.getChildAt(0).tag as Boolean)
            cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        else
            cv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.compra))
        cv.getChildAt(0).tag = !(v.getChildAt(0).tag as Boolean)



    }
    fun updateProduct(product: Producto) {
        carroSH.edit(true) { putString((product.id.toString()), Gson().toJson(product)) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {

            else -> super.onOptionsItemSelected(item)
        }
    }


}