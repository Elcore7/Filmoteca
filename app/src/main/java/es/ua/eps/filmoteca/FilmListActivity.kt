package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImagesToObjects()

        setMenu()
        setList()

        binding.button3.text = getString(R.string.ACERCA_DE);
        binding.button3.setOnClickListener{
            val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
            startActivity(aboutIntent)
        }

        binding.alternativeList.setOnClickListener {
            val recyclerIntent = Intent(this@FilmListActivity, FilmListRecyclerActivity::class.java)
            startActivity(recyclerIntent)
        }
    }

    private fun setMenu() {
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
        }
    }

    private fun setList() {
        val adaptador = FilmListAdapter(
            this, R.layout.activity_film_list_item /*android.R.layout.simple_list_item_1*/, FilmDataSource.films
        )

        binding.list.adapter = adaptador

        binding.list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                // Pruebas para obtener un item completo en caso de necesitarlo + Serializable en clase Film
                // val listItem: Film = binding.list.getItemAtPosition(position) as Film

                val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
                dataIntent.putExtra("FILM_INDEX", position)
                startActivity(dataIntent)
            }

        binding.list.onItemLongClickListener = // TODO
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                // binding.toolbar.visibility = View.GONE
                /*adaptador.toggleSelection(position)
                val count = (binding.list.adapter as FilmListAdapter).selectedItems.size()
                if (count > 0) {
                    //startActionMode(FilmListActionModeCallback(adaptador))
                    actionMode = this@FilmListActivity.startSupportActionMode(actionModeCallback)
                }*/
                true
            }
    }

    override fun onRestart() { // Para aplicar los cambios al volver a esta "activity" [onResume era otra posibilidad]
        super.onRestart()
        setList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(elemento: MenuItem): Boolean {
        super.onOptionsItemSelected(elemento)
        when (elemento.itemId) {
            R.id.addFilm -> {
                addBlankFilm()
            }
            R.id.aboutUs -> {
                val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return false
    }

    private fun addBlankFilm() {
        var newFilm: Film = Film()
        newFilm.title = "Título provisional"
        newFilm.director = "John Doe"
        newFilm.year = 1938
        newFilm.genre = Film.Companion.GENRE_ACTION
        newFilm.format = Film.Companion.FORMAT_DVD
        newFilm.imageResId = R.drawable.default_film_image
        newFilm.bitmapImage = BitmapFactory.decodeResource(resources,R.drawable.default_film_image)
        newFilm.imdbUrl = ""
        newFilm.comments = "Comentario casual"

        FilmDataSource.films.add(newFilm)

        setList()
    }


}