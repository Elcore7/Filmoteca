package es.ua.eps.filmoteca

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.DiffUtil
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListFragment : ListFragment() {

    var callback: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val adapter = FilmListAdapter(activity, R.layout.activity_film_list_item, FilmDataSource.films)
        listAdapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Comprueba que la actividad implemente la interfaz definida
        // y guarda una referencia en la variable "callback"
        callback = try {
            context as OnItemSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " debe implementar OnItemSelectedListener")
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        // super.onListItemClick(l, v, position, id)
        callback?.onItemSelected(position)
    }

}