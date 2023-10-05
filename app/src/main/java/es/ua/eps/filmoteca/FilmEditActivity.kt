package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.databinding.ActivityFilmFormEditBinding


class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmFormEditBinding /*ActivityFilmEditBinding*/

    private var imageCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.data != null) {
            onActivityResult(CAMERA_IMAGE_CODE, result.resultCode, result.data)
        }
    };
    private var imageGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if(result.data != null) {
            onActivityResult(GALLERY_IMAGE_CODE, result.resultCode, result.data)
        }
    };
    // Ya por defecto realizará la llamada "onActivityResult", no hace falta llamarla aquí

    var CAMERA_IMAGE_CODE = 1
    var GALLERY_IMAGE_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmFormEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtons()
    }

    private fun setButtons() {
        binding.buttonSave.setOnClickListener { // Guardar
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.buttonCancel.setOnClickListener { // Cancelar
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.buttonTakePhoto.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(Build.VERSION.SDK_INT >= 30) {
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    imageCamera.launch(cameraIntent)
                } else {
                    Toast.makeText(this, resources.getString(R.string.NO_COMPATIBLE), Toast.LENGTH_LONG).show()
                }
            } else {
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    @Suppress("DEPRECATION")
                    startActivityForResult(cameraIntent, CAMERA_IMAGE_CODE)
                } else {
                    Toast.makeText(this, resources.getString(R.string.NO_COMPATIBLE), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.buttonGallery.setOnClickListener {
            val galleryIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
            intent.type = "image/*"
            if(Build.VERSION.SDK_INT >= 30) {
                // Caso en el que se use ActivityResultContracts.GetContent() [Línea 19]
                // --> imageGallery.launch("image/*")
                // Se podría evitar el construir el Intent en este caso con lo de arriba
                if(galleryIntent.resolveActivity(packageManager) != null) {
                    imageGallery.launch(galleryIntent)
                } else {
                    Toast.makeText(this, resources.getString(R.string.NO_COMPATIBLE), Toast.LENGTH_LONG).show()
                }
            }
            else {
                if(galleryIntent.resolveActivity(packageManager) != null) {
                    @Suppress("DEPRECATION")
                    startActivityForResult(
                        Intent.createChooser(galleryIntent, getString(R.string.SELECCIONE_IMAGEN)),
                        GALLERY_IMAGE_CODE
                    )
                } else {
                    Toast.makeText(this, resources.getString(R.string.NO_COMPATIBLE), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Si se llama el super al comienzo, este llamará al de "imageCamera" y este volverá aquí realizando 2 llamadas
        // super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_IMAGE_CODE ->
                if (resultCode == RESULT_OK && data != null && data.data != null) {
                    binding.imageMovie.setImageURI(data.data)
                }
            CAMERA_IMAGE_CODE ->
                if (resultCode == RESULT_OK && data != null) {
                        val photo = data?.extras?.get("data") as Bitmap
                        binding.imageMovie.setImageBitmap(photo)
                }
            else ->
            {
                if (data != null) {
                    super.onActivityResult(requestCode, resultCode, data)
                }
            }
        }

    }
}