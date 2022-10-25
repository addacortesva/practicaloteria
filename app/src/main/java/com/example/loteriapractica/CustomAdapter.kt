package com.example.loteriapractica
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

//hacemos el adapter para las cartitas
class CustomAdapter(private val data:List<Cartas>) : RecyclerView.Adapter<CustomAdapter.BarajaViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BarajaViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_view, viewGroup, false)
        return BarajaViewHolder(v)
    }

    override fun onBindViewHolder(holder: BarajaViewHolder, i: Int) {
        val item = data[i]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class BarajaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage = itemView.findViewById<ImageView>(R.id.listaCartas)

        fun render(carta: Cartas) {
            itemImage.setImageResource(carta.imagen)
        }
    }
}

