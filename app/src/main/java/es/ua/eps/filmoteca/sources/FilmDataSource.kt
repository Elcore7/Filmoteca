package es.ua.eps.filmoteca.sources

import es.ua.eps.filmoteca.R
import es.ua.eps.filmoteca.classes.Film

object FilmDataSource {
    val films: MutableList<Film> = mutableListOf<Film>()

    init {
        var f = Film()
        f.title = "Regreso al futuro"
        f.director = "Robert Zemeckis"
        f.imageResId = R.drawable.back_to_the_future
        f.comments = "Comentario interesante"
        f.format = Film.Companion.FORMAT_DIGITAL
        f.genre = Film.Companion.GENRE_SCIFI
        f.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f.year = 1985
        films.add(f)

        f = Film()
        f.title = "Los Robinson"
        f.director = "No lo sé, no lo he mirado"
        f.imageResId = R.drawable.back_to_the_future
        f.comments = "Esta memorable la verdad"
        f.format = Film.Companion.FORMAT_BLURAY
        f.genre = Film.Companion.GENRE_COMEDY
        f.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f.year = 2008 // creo
        films.add(f)

        f = Film()
        f.title = "Spiderman 2"
        f.director = "Un grande, no lo conozco"
        f.imageResId = R.drawable.back_to_the_future
        f.comments = "Fresquísima mi pana"
        f.format = Film.Companion.FORMAT_DVD
        f.genre = Film.Companion.GENRE_ACTION
        f.imdbUrl = "http://www.imdb.com/title/tt0088763"
        f.year = 2002 // creo
        films.add(f)

    }
}