package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        setToolbar()
        setFilmData(filmIndex)

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
            onBackPressed() // Mejor opción (evita el "reseteo" de pantalla)
        }
    }

    private fun setToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar? = binding.toolbar2
        setSupportActionBar(toolbar)
        if (toolbar != null) {
            toolbar.setNavigationIcon(com.google.android.material.R.drawable.ic_arrow_back_black_24)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

}