package es.ua.eps.filmoteca.sources

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.activity.result.contract.ActivityResultContracts
import es.ua.eps.filmoteca.R
import es.ua.eps.filmoteca.classes.Film
import kotlin.coroutines.coroutineContext


object FilmDataSource {
    val films: MutableList<Film> = mutableListOf<Film>()

    init { // Los bitmap serán seteados posteriormente en la actividad principal
        var f = Film()
        f.title = "Regreso al futuro"
        f.director = "Robert Zemeckis"
        f.imageResId = R.drawable.film0
        // Resources.getSystem().getDrawable(R.drawable.film1).toBitmap()
        // BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.film1)
        f.bitmapImage = null
        f.comments = "Comentario interesante"
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_SCIFI
        f.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f.year = 1985
        films.add(f)

        f = Film()
        f.title = "Conoce a los Robinson"
        f.director = "Stephen J. Anderson"
        f.imageResId = R.drawable.film1
        f.bitmapImage =  null
        f.comments = "Funi :v"
        f.format = Film.Companion.FORMAT_BLURAY
        f.genre = Film.Companion.GENRE_COMEDY
        f.imdbUrl = "https://www.imdb.com/title/tt0396555/"
        f.year = 2007
        films.add(f)

        f = Film()
        f.title = "Spiderman 2"
        f.director = "Sam Raimi"
        f.imageResId = R.drawable.film2
        f.bitmapImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.film2)
        f.comments = "Fresquísima mi pana"
        f.format = Film.Companion.FORMAT_DVD
        f.genre = Film.Companion.GENRE_ACTION
        f.imdbUrl = "https://www.imdb.com/title/tt0316654/"
        f.year = 2004
        films.add(f)

    }
}