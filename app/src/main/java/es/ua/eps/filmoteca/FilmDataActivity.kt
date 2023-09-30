package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NavUtils
import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmDataBinding;

    companion object Extras {
        var EXTRA_FILM_TITLE = "";
        var MOVIE_RESULT = 0;
    }

    private val startForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            onActivityResult(MOVIE_RESULT, result.resultCode, result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EXTRA_FILM_TITLE = intent.getStringExtra("TITULO_PELICULA").toString();

        binding.textView1.text = EXTRA_FILM_TITLE

        binding.button1.setOnClickListener{ // Ver
            val dataIntent = Intent(this@FilmDataActivity, FilmDataActivity::class.java)
            dataIntent.putExtra("TITULO_PELICULA", "Pelicula relacionada")
            startActivity(dataIntent)
        }

        binding.button2.setOnClickListener{ // Editar
            val editIntent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
            if(Build.VERSION.SDK_INT >= 30) {
                startForResult.launch(editIntent)
            }
            else {
                @Suppress("DEPRECATION")
                startActivityForResult(editIntent, MOVIE_RESULT)
            }
        }

        binding.button3.setOnClickListener{ // Volver
            val returnIntent = Intent(this@FilmDataActivity, FilmListActivity::class.java)
            returnIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(returnIntent) // NavUtils.navigateUpTo(actividad_actual, intentObjetivo)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            MOVIE_RESULT ->
                if (resultCode == Activity.RESULT_OK) {
                    binding.textView2.text = "Se ha modificado"
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    binding.textView2.text = "NO se ha modificado"
                }
        }

    }

}