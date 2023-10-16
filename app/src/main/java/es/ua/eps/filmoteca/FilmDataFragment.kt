package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmDataFragment : Fragment() {

    companion object {
        var PARAM_POSITION: String = "PARAM_POSITION"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_film_data, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*var pos: Int = arguments?.getInt(PARAM_POSITION)!!;
        setFilm(pos)*/
    }

    // Hace falta esperar a que "exista" la vista para poder acceder a sus valores sin problemas
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pos: Int = arguments?.getInt(PARAM_POSITION)!!
        setFilm(pos)
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
            val viewIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(filmData.imdbUrl))
            if(viewIntent.resolveActivity(activity?.packageManager!!) != null) {
                startActivity(viewIntent)
            }
        }

        if (filmData.bitmapImage != null) {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(filmData.bitmapImage)
        } else {
            view?.findViewById<ImageView>(R.id.imageViewFilm)?.setImageBitmap(BitmapFactory.decodeResource(resources,R.drawable.default_film_image))
        }

    }
}