package es.ua.eps.filmoteca

import android.os.Bundle
import android.view.MotionEvent
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.ua.eps.filmoteca.adapters.FilmListRecyclerAdapter
import es.ua.eps.filmoteca.databinding.ActivityFilmListRecyclerBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListRecyclerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListRecyclerBinding

    var recyclerView: RecyclerView? = null
    var adapter: RecyclerView.Adapter<*>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerList
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager

        val films = FilmDataSource.films

        val adapter = FilmListRecyclerAdapter(films)
        recyclerView?.setAdapter(adapter)
        this.adapter = adapter

        /*recyclerView!!.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }
        }*/
            /*OnItemClickListener { parent, view, position, id ->
                // Pruebas para obtener un item completo en caso de necesitarlo + Serializable en clase Film
                // val listItem: Film = binding.list.getItemAtPosition(position) as Film

                val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
                dataIntent.putExtra("FILM_INDEX", position)
                startActivity(dataIntent)
            }*/
    }

    override fun onRestart() { // Para aplicar los cambios al volver a esta "activity" [onResume era otra posibilidad]
        super.onRestart()
        val films = FilmDataSource.films
        val adapter = FilmListRecyclerAdapter(films)
        recyclerView?.setAdapter(adapter)
        this.adapter = adapter
    }
}