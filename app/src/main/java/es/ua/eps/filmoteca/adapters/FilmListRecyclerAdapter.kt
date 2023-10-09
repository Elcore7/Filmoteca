package es.ua.eps.filmoteca.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.filmoteca.FilmDataActivity
import es.ua.eps.filmoteca.R
import es.ua.eps.filmoteca.classes.Film

class FilmListRecyclerAdapter(val films: List<Film>) : RecyclerView.Adapter<FilmListRecyclerAdapter.ViewHolder?>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titulo: TextView
        var director: TextView
        var icono: ImageView

        fun bind(l: Film) {
            titulo.text = l.title
            director.text = l.director
            icono.setImageBitmap(l.bitmapImage)
        }

        init {
            titulo = v.findViewById(R.id.filmName)
            director = v.findViewById(R.id.filmDirector)
            icono = v.findViewById(R.id.filmIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_film_list_recycler_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(films[position])

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, FilmDataActivity::class.java)
            intent.putExtra("FILM_INDEX", position)
            v.context.startActivity(intent)
        }
    }
}