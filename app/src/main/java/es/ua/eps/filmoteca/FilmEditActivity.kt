package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.ua.eps.filmoteca.databinding.ActivityFilmEditBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmFormEditBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmFormEditBinding /*ActivityFilmEditBinding*/

    // TODO: A preguntar: layoutMargin (principal), guardado de datos al dataView?, cambio de pantalla edit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmFormEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener{ // Guardar
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.buttonCancel.setOnClickListener{ // Cancelar
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}