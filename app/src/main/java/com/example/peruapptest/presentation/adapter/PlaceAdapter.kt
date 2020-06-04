package com.example.peruapptest.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peruapptest.R
import com.example.peruapptest.presentation.model.Place
import com.example.peruapptest.util.cargarImagen
import kotlinx.android.synthetic.main.places_items.view.*

class PlaceAdapter(private val context: Context, private val items: ArrayList<Place>)
    : RecyclerView.Adapter<PlaceAdapter.VHPlace>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceAdapter.VHPlace {
        return VHPlace(
            LayoutInflater
                .from(context)
                .inflate(R.layout.places_items, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PlaceAdapter.VHPlace, position: Int) {
        var item = items[position]
        item.imageURL?.let { holder.ivImagen.cargarImagen(it, R.drawable.ic_upload_image, R.drawable.ic_upload_image) }
        holder.tvDescripcionPlace.text = "Descripción: " + item.descripcionLugar
        holder.tvFecha.text = item.createdAt
        holder.tvFoto.text = item.imageURL
        holder.tvFoto.visibility = View.GONE

    }

    class VHPlace(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImagen: ImageView = itemView.iv_foto
        var tvDescripcionPlace: TextView = itemView.tv_descripcion
        var tvFecha: TextView = itemView.tv_fecha
        var tvFoto: TextView = itemView.tv_url_foto
    }

}