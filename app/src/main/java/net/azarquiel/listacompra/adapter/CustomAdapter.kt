package net.azarquiel.listacompra.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import net.azarquiel.listacompra.R
import net.azarquiel.listacompra.model.Producto

class CustomAdapter(val context: Context,
                        val layout: Int
    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        private var dataList: List<Producto> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val viewlayout = layoutInflater.inflate(layout, parent, false)
            return ViewHolder(viewlayout, context)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        internal fun setProducto(Dias: List<Producto>) {
            this.dataList = Dias
            notifyDataSetChanged()
        }



        class ViewHolder(viewlayout: View, val context: Context) : RecyclerView.ViewHolder(viewlayout) {
            fun bind(dataItem: Producto){
                val tvfecharow = itemView.findViewById(R.id.tvpoprow) as TextView
                val tvpoprow = itemView.findViewById(R.id.tvfecharow) as TextView
                val tvpronorow = itemView.findViewById(R.id.tvtemprow) as TextView


                tvfecharow.text =dataItem.nombre
                tvpoprow.text ="${dataItem.cantidad}"
                tvpronorow.text ="${dataItem.precio} €"



                itemView.tag = dataItem
                (itemView as CardView).setCardBackgroundColor(ContextCompat.getColor(context,R.color.compra))
                (itemView as CardView).getChildAt(0).tag = false

            }


        }
    }
