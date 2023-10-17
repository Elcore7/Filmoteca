package es.ua.eps.filmoteca

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import es.ua.eps.filmoteca.sources.FilmDataSource


class FilmDataFragment : Fragment() {

    companion object {
        var PARAM_POSITION: String = "PARAM_POSITION"
        var MOVIE_RESULT = 1
    }

    private var filmIndex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_film_data, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        var pos: Int? = arguments?.getInt(PARAM_POSITION);
        if (pos == null)
            pos = 0
        filmIndex = pos!!
        /*setFilm(pos)*/
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(MOVIE_RESULT, result.resultCode, result.data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            FilmDataActivity.MOVIE_RESULT ->
                if (resultCode == Activity.RESULT_OK) {
                    view?.findViewById<TextView>(R.id.textViewSaveState)?.text = getString(R.string.SAVED_STATE_SUCCESS)
                    setFilm(filmIndex)
                    (activity as MainActivity).actualizarLista()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    view?.findViewById<TextView>(R.id.textViewSaveState)?.text = getString(R.string.SAVED_STATE_CANCEL)
                }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    // Hace falta esperar a que "exista" la vista para poder acceder a sus valores sin problemas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pos: Int? = arguments?.getInt(PARAM_POSITION)
        if (pos == null)
            pos = 0
        setFilm(pos)

        view?.findViewById<Button>(R.id.buttonEdit)?.setOnClickListener {
            val editIntent = Intent(activity, FilmEditActivity::class.java)
            editIntent.putExtra("FILM_INDEX", filmIndex)
            if(Build.VERSION.SDK_INT >= 30) {
                startForResult.launch(editIntent)
            }
            else {
                @Suppress("DEPRECATION")
                startActivityForResult(editIntent, FilmDataActivity.MOVIE_RESULT)
            }
        }

        view?.findViewById<Button>(R.id.buttonReturnMainMenu)?.visibility = View.GONE
    }

    public fun getFilmIndex() : Int {
        return filmIndex
    }

    public fun setFilm(index: Int) {
        val filmData = FilmDataSource.films[index]

        val textTitle = view?.findViewById<TextView>(R.id.textViewTitle)

        view?.findViewById<TextView>(R.id.textViewTitle)?.text = filmData.title
        view?.findViewById<TextView>(R.id.textViewDirector)?.text = filmData.director
        view?.findViewById<TextView>(R.id.textViewYear)?.text = filmData.year.toString()
        view?.findViewById<TextView>(R.id.textViewComment)?.text = filmData.comments

        // Enumeraciones:
        var enumFormato = resources.getStringArray(R.array.FORMATO)
        var enumGenre = resources.getStringArray(R.array.GENERO)
        view?.findViewById<TextView>(R.id.textViewGenre)?.text = enumGenre.get(FilmDataActivity.FILM_DATA.genre)
        view?.findViewById<TextView>(R.id.textViewFormat)?.text = enumFormato.get(FilmDataActivity.FILM_DATA.format)

        view?.findViewById<Button>(R.id.buttonImdb)?.setOnClickListener {
            val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse(filmData.imdbUrl))
            if(viewIntent.resolveActivity(activity?.packageManager!!) != null) {
                startActivity(viewIntent)
            }
        }

        if (filmData.bitmapImage != null) {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(filmData.bitmapImage)
        } else {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.default_film_image))
        }

        filmIndex = index
    }

    public fun updateImage() { // Funcion auxiliar para actualizar la imagen al comienzo (Si existe pantalla grande)
        if (filmIndex != null) {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(FilmDataSource.films[filmIndex].bitmapImage)
        } else {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.default_film_image))
        }
    }
}