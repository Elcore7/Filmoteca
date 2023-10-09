package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NavUtils
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmDataBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmDataBinding;

    companion object Extras {
        var EXTRA_FILM_TITLE = "";
        var FILM_DATA = Film();
        var MOVIE_RESULT = 1;
    }

    private var filmIndex: Int = -1

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(MOVIE_RESULT, result.resultCode, result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filmIndex = intent.getIntExtra("FILM_INDEX", -1)

        setFilmData(filmIndex)

        /*EXTRA_FILM_TITLE = intent.getStringExtra("TITULO_PELICULA").toString();

        binding.textViewTitle.text = EXTRA_FILM_TITLE*/

        /*binding.buttonImdb.setOnClickListener{ // Ver en IMDB
            // val dataIntent = Intent(this@FilmDataActivity, FilmDataActivity::class.java)
            // dataIntent.putExtra("TITULO_PELICULA", "Pelicula relacionada")
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.imdb.com/title/tt0088763/"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }*/

        binding.buttonEdit.setOnClickListener{ // Editar
            val editIntent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
            editIntent.putExtra("FILM_INDEX", filmIndex)
            if(Build.VERSION.SDK_INT >= 30) {
                startForResult.launch(editIntent)
            }
            else {
                @Suppress("DEPRECATION")
                startActivityForResult(editIntent, MOVIE_RESULT)
            }
        }

        binding.buttonReturnMainMenu.setOnClickListener{ // Volver al menú
            /*val returnIntent = Intent(this@FilmDataActivity, FilmListActivity::class.java)
            returnIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(returnIntent) // NavUtils.navigateUpTo(actividad_actual, intentObjetivo)*/
            onBackPressed() // Mejor opción (evita el "reseteo" de pantalla)
        }
    }

    private fun setFilmData(filmIndex: Int) {
        if (filmIndex == -1)
            return
        // Si no da caso error, se cargan los datos
        FILM_DATA = FilmDataSource.films[filmIndex]

        binding.textViewTitle.text = FILM_DATA.title
        binding.textViewDirector.text = FILM_DATA.director
        binding.textViewYear.text = FILM_DATA.year.toString()

        // Enumeraciones:
        var enumFormato = resources.getStringArray(R.array.FORMATO)
        var enumGenre = resources.getStringArray(R.array.GENERO)
        binding.textViewGenre.text = enumGenre.get(FILM_DATA.genre)
        binding.textViewFormat.text = enumFormato.get(FILM_DATA.format)

        binding.buttonImdb.setOnClickListener {
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse(FILM_DATA.imdbUrl))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }
        if (FILM_DATA.bitmapImage != null) {
            binding.imageViewFilm.setImageBitmap(FILM_DATA.bitmapImage)
        } else {
            binding.imageViewFilm.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.default_film_image))
        }
        /*if (FILM_DATA.imageResId != null) {
            binding.imageViewFilm.setImageResource(FILM_DATA.imageResId)
        } else {
            binding.imageViewFilm.setImageResource(R.drawable.default_film_image)
        }*/

        binding.textViewComment.text = FILM_DATA.comments
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            MOVIE_RESULT ->
                if (resultCode == Activity.RESULT_OK) {
                    binding.textViewSaveState.text = getString(R.string.SAVED_STATE_SUCCESS)
                    setFilmData(filmIndex)
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    binding.textViewSaveState.text = getString(R.string.SAVED_STATE_CANCEL)
                }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }

    }

}