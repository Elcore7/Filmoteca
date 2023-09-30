package es.ua.eps.filmoteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.ua.eps.filmoteca.databinding.ActivityAboutBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val verPelicula = R.string.VER_PELICULA

        binding.button1.text = getString(verPelicula, " A")
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
        }

        binding.button3.text = getString(R.string.ACERCA_DE);
        binding.button3.setOnClickListener{
            val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
            startActivity(aboutIntent)
        }
    }
}