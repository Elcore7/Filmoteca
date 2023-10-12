package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import es.ua.eps.filmoteca.action_modes.FilmListActionModeCallback
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding

    private var actionMode: ActionMode? = null

    val actionModeCallback: ActionMode.Callback =
        object : ActionMode.Callback {
            // Se llama la primera vez que se crea la barra contextual
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                val inflater = mode?.menuInflater
                inflater?.inflate(R.menu.menu_action_bar, menu)
                return true
            }

            // Se llama después de crear la barra y cada vez que se invalida
            override fun onPrepareActionMode(mode: ActionMode,
                                             menu: Menu): Boolean {
                return false
            }

            // Se invoca cuando pulsamos sobre un item del menú contextual
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                when (item?.itemId) {
                    R.id.action_delete -> {
                        // TODO: SELECCIONAR
                        // mode?.finish()
                        return true
                    }
                }
                return false
            }

            // Se llama cuando salimos del action mode
            override fun onDestroyActionMode(mode: ActionMode) {
                actionMode = null
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImagesToObjects()

        setToolbar()
        setList()

        binding.button3.text = getString(R.string.ACERCA_DE);
        binding.button3.setOnClickListener{
            val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
            startActivity(aboutIntent)
        }

        //  No hace falta mantener este listado que solo se pedía en un ejercicio anterior de otra práctica
        /*binding.alternativeList.setOnClickListener {
            val recyclerIntent = Intent(this@FilmListActivity, FilmListRecyclerActivity::class.java)
            startActivity(recyclerIntent)
        }*/
    }

    private fun setToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        // val toolbar: Toolbar = binding.toolbar as Toolbar
        // toolbar.setNavigationIcon(com.google.android.material.R.drawable.ic_arrow_back_black_24)

        /*//Opt. común
        val groupId = Menu.NONE // Identificador de grupo (Grupo de menú)

        // Añadir peli
        val itemId = Menu.FIRST // Identificador único
        val itemOrder = Menu.FIRST // Posición del elemento
        val itemText = getString(R.string.ANYADIR_PELICULA) // Texto de la opción de menú

        // Acerca de
        val itemId2 = Menu.FIRST + 1 // Identificador único
        val itemOrder2 = Menu.FIRST + 1 // Posición del elemento
        val itemText2 = getString(R.string.ACERCA_DE) // Texto de la opción de menú

        toolbar.menu.add(groupId, itemId, itemOrder, itemText)
        toolbar.menu.add(groupId, itemId2, itemOrder2, itemText2)*/
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

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            if (filmAux.imageResId != null)
                filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
        }
    }

    private fun setList() {
        val adaptador = FilmListAdapter(
            this, R.layout.activity_film_list_item /*android.R.layout.simple_list_item_1*/, FilmDataSource.films
        )

        binding.list.adapter = adaptador
        // TODO: ListView.CHOICE_MODE_MULTIPLE

        binding.list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                // Pruebas para obtener un item completo en caso de necesitarlo + Serializable en clase Film
                // val listItem: Film = binding.list.getItemAtPosition(position) as Film

                val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
                dataIntent.putExtra("FILM_INDEX", position)
                startActivity(dataIntent)
            }

        binding.list.onItemLongClickListener =
            OnItemLongClickListener { parent, view, position, id ->
                // binding.toolbar.visibility = View.GONE
                adaptador.toggleSelection(position)
                val count = (binding.list.adapter as FilmListAdapter).selectedItems.size()
                if (count > 0) {
                    //startActionMode(FilmListActionModeCallback(adaptador))
                    actionMode = this@FilmListActivity.startSupportActionMode(actionModeCallback)
                }
                true
            }
    }

    override fun onRestart() { // Para aplicar los cambios al volver a esta "activity" [onResume era otra posibilidad]
        super.onRestart()
        setList()
    }


    // Auxiliares menú:

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                addBlankFilm()
            }
            R.id.item2 -> {
                val aboutIntent = Intent(this@FilmListActivity, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
        }
        return true
    }
}