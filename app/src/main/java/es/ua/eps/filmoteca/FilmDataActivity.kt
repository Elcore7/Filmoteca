package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
        var MOVIE_RESULT = 1;
    }

    private val startForResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { /*result: ActivityResult ->
            onActivityResult(MOVIE_RESULT, result.resultCode, result.data)*/
            // Ya por defecto, "ActivityResultContracts.StartActivityForResult()" realiza la llamada "onActivityResult"
            // Se ha comentado puesto que está duplicando la llamada, aquí se podrían realizar
            // los cambios de "onActivityResult" si no se controla las API < 30

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EXTRA_FILM_TITLE = intent.getStringExtra("TITULO_PELICULA").toString();

        binding.textViewTitle.text = EXTRA_FILM_TITLE

        binding.buttonImdb.setOnClickListener{ // Ver en IMDB
            // val dataIntent = Intent(this@FilmDataActivity, FilmDataActivity::class.java)
            // dataIntent.putExtra("TITULO_PELICULA", "Pelicula relacionada")
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.imdb.com/title/tt0088763/"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }

        binding.buttonEdit.setOnClickListener{ // Editar
            val editIntent = Intent(this@FilmDataActivity, FilmEditActivity::class.java)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            MOVIE_RESULT ->
                if (resultCode == Activity.RESULT_OK) {
                    binding.textViewSaveState.text = getString(R.string.SAVED_STATE_SUCCESS)
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    binding.textViewSaveState.text = getString(R.string.SAVED_STATE_CANCEL)
                }
        }

    }

}