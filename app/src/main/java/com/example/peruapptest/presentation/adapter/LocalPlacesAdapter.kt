package com.example.peruapptest.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peruapptest.R
import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.util.cargarImagen
import kotlinx.android.synthetic.main.places_local_items.view.*

class LocalPlacesAdapter(private val context: Context, private val items: List<PlaceEntity>)
    : RecyclerView.Adapter<LocalPlacesAdapter.VHLocalPlace>(){

    var listener: eventoCompartir? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalPlacesAdapter.VHLocalPlace {
        return VHLocalPlace(
            LayoutInflater
                .from(context)
                .inflate(R.layout.places_local_items, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LocalPlacesAdapter.VHLocalPlace, position: Int) {
        var item = items[position]
        item.imageURL?.let { holder.ivImagen.cargarImagen(it, R.drawable.ic_upload_image, R.drawable.ic_upload_image) }
        holder.tvDescripcionPlace.text = "Descripción: " + item.descripcionLugar
        holder.tvFecha.text = item.createdAt
        holder.tvFoto.text = item.imageURL
        holder.tvFoto.visibility = View.GONE
        holder.btCompartir.setOnClickListener {
            listener?.compartir(item)
        }
        holder.btEliminar.setOnClickListener {
            listener?.eliminar(item)
        }

    }

    class VHLocalPlace(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImagen: ImageView = itemView.iv_foto
        var tvDescripcionPlace: TextView = itemView.tv_descripcion
        var tvFecha: TextView = itemView.tv_fecha
        var tvFoto: TextView = itemView.tv_url_foto
        var btCompartir: Button = itemView.bt_compartir
        var btEliminar: Button = itemView.bt_eliminar
    }

    interface eventoCompartir {
        fun compartir(entity: PlaceEntity)
        fun eliminar(entity: PlaceEntity)
    }

}
