package es.ua.eps.filmoteca

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val verPelicula = R.string.VER_PELICULA

        val adaptador = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, FilmDataSource.films
        )

        binding.movieList.adapter = adaptador

        /*binding.button1.text = getString(verPelicula, " A")
        binding.button1.setOnClickListener{
            val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
            dataIntent.putExtra("TITULO_PELICULA", "Película A")
            startActivity(dataIntent)
        }

        binding.button2.text = getString(verPelicula, " B")
        binding.button2.setOnClickListener{
            val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
            dataIntent.putExtra("TITULO_PELICULA", "Película B")
            startActivity(dataIntent)
        }*/

        binding.button3.text = getString(R.string.ACERCA_DE);
        binding.button3.setOnClickListener{
            val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
            startActivity(aboutIntent)
        }
    }
}