package es.ua.eps.filmoteca.adapters

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import es.ua.eps.filmoteca.R
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListAdapter(context: Context?, resource: Int, val objects: List<Film>?) : ArrayAdapter<Film>(context!!, resource, objects!!) {

    public val selectedItems = SparseBooleanArray()

    override fun getView(position: Int, convertView: View?,
                         parent: ViewGroup): View {

        var view: View = convertView?: LayoutInflater.from(this.context)
            .inflate(R.layout.activity_film_list_item, parent, false)

        val titulo = view.findViewById(R.id.filmName) as TextView
        val director = view.findViewById(R.id.filmDirector) as TextView
        val icono = view.findViewById(R.id.filmIcon) as ImageView

        getItem(position)?.let {
            titulo.text = it.title
            director.text = it.director
            // icono.setImageResource(it.imageResId)
            icono.setImageBitmap(it.bitmapImage)
        }

        return view
    }

    fun toggleSelection(position: Int) {
        selectedItems.put(position, !selectedItems.get(position))
        notifyDataSetChanged()
    }

    // Método para eliminar elementos seleccionados
    fun deleteSelectedItems() {
        val itemsToDelete = mutableListOf<Film>()
        for (i in 0 until FilmDataSource.films.size) {
            if (selectedItems.get(i)) {
                objects?.get(i)?.let { itemsToDelete.add(it) }
            }
        }
        for (item in itemsToDelete) {
            FilmDataSource.films.remove(item)
        }
        selectedItems.clear()
        notifyDataSetChanged()
    }
}