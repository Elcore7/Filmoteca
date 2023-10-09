package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImagesToObjects()
        /*val verPelicula = R.string.VER_PELICULA*/
        setList()

        binding.button3.text = getString(R.string.ACERCA_DE);
        binding.button3.setOnClickListener{
            val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
            startActivity(aboutIntent)
        }
    }

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
        }
    }

    private fun setList() {
        val adaptador = FilmListAdapter(
            this, R.layout.activity_film_list_item /*android.R.layout.simple_list_item_1*/, FilmDataSource.films
        )

        binding.list.adapter = adaptador

        binding.list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                // Pruebas para obtener un item completo en caso de necesitarlo + Serializable en clase Film
                // val listItem: Film = binding.list.getItemAtPosition(position) as Film

                val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
                dataIntent.putExtra("FILM_INDEX", position)
                startActivity(dataIntent)
            }
    }

    override fun onRestart() { // Para aplicar los cambios al volver a esta "activity" [onResume era otra posibilidad]
        super.onRestart()
        setList()
    }
}