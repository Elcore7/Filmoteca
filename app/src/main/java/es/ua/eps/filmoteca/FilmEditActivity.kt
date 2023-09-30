package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.ua.eps.filmoteca.databinding.ActivityFilmEditBinding
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding

class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener{ // Guardar
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.button2.setOnClickListener{ // Cancelar
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}