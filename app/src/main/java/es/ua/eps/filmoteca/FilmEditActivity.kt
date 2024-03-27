package es.ua.eps.filmoteca

import android.R.attr.bitmap
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.Firebase
import com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import com.google.firebase.messaging.remoteMessage
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmFormEditBinding
import es.ua.eps.filmoteca.sources.FilmDataSource
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class FilmEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmFormEditBinding /*ActivityFilmEditBinding*/

    var filmIndex = -1
    private lateinit var bitmapImage: Bitmap;

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

        filmIndex = intent.getIntExtra("FILM_INDEX", -1)

        setFilmData(filmIndex)

        setButtons()
    }

    private fun setButtons() {
        binding.buttonSave.setOnClickListener { // Guardar
            setResult(Activity.RESULT_OK)
            if (filmIndex != -1) {
                saveFilm(filmIndex)
                enviarFirebaseNotif()
            }
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

    private fun saveFilm(filmIndex: Int) {
        FilmDataSource.films[filmIndex].title = binding.editTextTitle.text.toString()
        FilmDataSource.films[filmIndex].director = binding.editTextDirectorName.text.toString()
        FilmDataSource.films[filmIndex].year = binding.editTextYear.text.toString().toInt()
        FilmDataSource.films[filmIndex].imdbUrl = binding.editTextImdb.text.toString()
        FilmDataSource.films[filmIndex].comments = binding.editTextComment.text.toString()
        FilmDataSource.films[filmIndex].format = binding.spinnerFormat.selectedItemPosition
        FilmDataSource.films[filmIndex].genre = binding.spinnerGenre.selectedItemPosition

        if (binding.imageMovie.drawable.toBitmap() != FilmDataSource.films[filmIndex].bitmapImage) { // SET imagen
            var bitAux = binding.imageMovie.drawable.toBitmap()
            FilmDataSource.films[filmIndex].bitmapImage = binding.imageMovie.drawable.toBitmap()
        }
    }

    private fun setFilmData(filmIndex: Int) {
        if (filmIndex == -1)
            return
        // Si no da caso error, se cargan los datos
        var filmContent: Film = FilmDataSource.films[filmIndex]

        binding.editTextTitle.setText(filmContent.title)
        binding.editTextDirectorName.setText(filmContent.director)
        binding.editTextYear.setText(filmContent.year.toString())

        // Enumeraciones:
        binding.spinnerGenre.setSelection(filmContent.genre)
        binding.spinnerFormat.setSelection(filmContent.format)

        binding.editTextImdb.setText(filmContent.imdbUrl)
        binding.editTextComment.setText(filmContent.comments)

        binding.imageMovie.setImageBitmap(filmContent.bitmapImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Si se llama el super al comienzo, este llamará al de "imageCamera" y este volverá aquí realizando 2 llamadas
        // super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALLERY_IMAGE_CODE ->
                if (resultCode == RESULT_OK && data != null && data.data != null) {
                    // binding.imageMovie.setImageURI(data.data)
                    val galleryImage = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                    bitmapImage = galleryImage
                    binding.imageMovie.setImageBitmap(galleryImage)
                }
            CAMERA_IMAGE_CODE ->
                if (resultCode == RESULT_OK && data != null) {
                    val photo = data?.extras?.get("data") as Bitmap
                    bitmapImage = photo
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val returnIntent = Intent(this@FilmEditActivity, MainActivity::class.java)
                // Si se usan en conjunto estas FLAGS se evita el efecto de "reseteo de pantalla"
                returnIntent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(returnIntent)
            }
        }
        return true
    }

    // Mensaje de Firebase
    fun enviarFirebaseNotif() { // TODO: No parece enviar los mensajes correctamente
        var topic = "movies"

        val message = RemoteMessage.Builder(topic)
            .setMessageId(Integer.toString((0..Int.MAX_VALUE).random()))
            .setData(mapOf("title" to "New film",
                "body" to binding.editTextTitle.text.toString())) // Datos adicionales si es necesario
            .build()
        FirebaseMessaging.getInstance().send(message)
        // Firebase.messaging.send(message)
        /*FirebaseMessaging.getInstance().send(
            RemoteMessage.Builder(topic)
            .setMessageId(java.lang.Integer.toString((0..Int.MAX_VALUE).random())) // ID único del mensaje
            .setData(mapOf(
                "title" to "New film",
                "body" to binding.editTextTitle.text.toString()
            ))
            .build())*/
    }
}