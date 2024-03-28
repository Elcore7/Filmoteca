package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityFilmListBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class FilmListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilmListBinding
    private val selectedItems = HashSet<Int>()
    var actionMode: ActionMode? = null
    lateinit var adaptador: FilmListAdapter

    private val actionModeCallback: ActionMode.Callback =
        object : ActionMode.Callback, MultiChoiceModeListener {

            override fun onCreateActionMode(mode: ActionMode,
                                            menu: Menu): Boolean {
                val inflater = mode.menuInflater
                inflater.inflate(R.menu.menu_action_list, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode,
                                             menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode,
                                             item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.deleteButton -> {
                        deleteSelectedItems()
                        mode.finish() // Cerramos el action mode
                        true
                    }
                    else -> false
                }
            }

            // Se llama cuando salimos del action mode
            override fun onDestroyActionMode(mode: ActionMode) {
                for(i in 0 until binding.list.childCount) { // Se resetea el background de cada elemento
                    val view = binding.list.getChildAt(i)
                    view.setBackgroundColor(Color.TRANSPARENT)
                }
                selectedItems.clear()
                actionMode = null
            }

            override fun onItemCheckedStateChanged(mode: ActionMode?, position: Int,
                                                   id: Long, checked: Boolean) {
                return
            }
        }

    private fun changeSelected(position: Int, view: View) {
        if (selectedItems.contains(position)) { // Ya estaba seleccionado
            selectedItems.remove(position)
            view.setBackgroundColor(Color.TRANSPARENT)
        } else { // Se deselecciona
            selectedItems.add(position)
            view.setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun deleteSelectedItems() {
        // Obtener items de selectedItems
        eraseSelectedFilms()
        // Eliminar elementos en la lista de seleccionados
        selectedItems.clear()
        adaptador.notifyDataSetChanged();
    }

    private fun eraseSelectedFilms() {
        var filmListCopy = FilmDataSource.films.toMutableList()

        for (i in selectedItems) {
            FilmDataSource.films.remove(filmListCopy[i])
        }

    }

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

        // En esta práctica no nos hace falta el recycler view (era una actividad alternativa
        // para el ejercicio 4 de la anterior práctica), además de que solo se menciona que
        // el borrado múltiple sea para la actividad
        /*binding.alternativeList.setOnClickListener {
            val recyclerIntent = Intent(this@FilmListActivity, FilmListRecyclerActivity::class.java)
            startActivity(recyclerIntent)
        }*/
    }

    private fun setMenu() {
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            if (filmAux.imageResId != -1) {
                filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
            }
        }
    }

    private fun setList() {
        adaptador = FilmListAdapter(
            this, R.layout.activity_film_list_item /*android.R.layout.simple_list_item_1*/, FilmDataSource.films
        )

        binding.list.adapter = adaptador

        binding.list.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                // Pruebas para obtener un item completo en caso de necesitarlo + Serializable en clase Film
                // val listItem: Film = binding.list.getItemAtPosition(position) as Film
                if (actionMode == null) {
                    val dataIntent = Intent(this@FilmListActivity, FilmDataActivity::class.java)
                    dataIntent.putExtra("FILM_INDEX", position)
                    startActivity(dataIntent)
                } else {
                    changeSelected(position, view)
                }
            }

        binding.list.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { parent, view, position, id ->
                if (actionMode != null) {
                    false
                }  else {
                    changeSelected(position, view)
                    actionMode = this@FilmListActivity.startActionMode(actionModeCallback)
                    view.isSelected = true
                    true
                }
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
        adaptador.notifyDataSetChanged()
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

        adaptador.notifyDataSetChanged()
    }


}