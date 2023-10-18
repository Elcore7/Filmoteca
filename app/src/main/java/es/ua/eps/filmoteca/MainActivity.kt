package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.TextView
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityMainBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class MainActivity : AppCompatActivity(), FilmListFragment.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    var actionMode: ActionMode? = null
    private val selectedItems = HashSet<Int>()

    private val actionModeCallback: ActionMode.Callback =
        object : ActionMode.Callback, AbsListView.MultiChoiceModeListener {

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
                var filmListFragment: FilmListFragment
                try {
                    filmListFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as FilmListFragment
                } catch (e: Exception) {
                    filmListFragment = supportFragmentManager.findFragmentById(R.id.film_list_fragment) as FilmListFragment
                }

                for(i in 0 until filmListFragment.listView.childCount) { // Se resetea el background de cada elemento
                    val view = filmListFragment.listView.getChildAt(i)
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

    private fun deleteSelectedItems() {
        // Obtener items de selectedItems
        eraseSelectedFilms()
        // Eliminar elementos en la lista de seleccionados
        selectedItems.clear()
        actualizarLista()
    }

    private fun eraseSelectedFilms() {
        var filmListCopy = FilmDataSource.films.toMutableList()

        for (i in selectedItems) {
            FilmDataSource.films.remove(filmListCopy[i])
        }

        getDataFragment()?.updateView() // Si existe un data fragment, comprobamos si hay que actualizarlo o no
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImagesToObjects()

        if (savedInstanceState != null) return

        val listFragment = FilmListFragment()
        listFragment.arguments = intent.extras

        if (binding.fragmentContainer != null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, listFragment)
                .addToBackStack(null).commit()
        } else {

        }
    }

    override fun onItemSelected(position: Int, view: View) {
        if (actionMode == null) {
            var dataFragment = supportFragmentManager.findFragmentById(R.id.film_data_fragment) as FilmDataFragment?

            if (dataFragment != null) { // Existe fragmento de "data" -> Pantalla pequeña
                // var list = supportFragmentManager.findFragmentById(R.id.film_list_fragment)
                dataFragment.requireView().findViewById<TextView>(R.id.textViewSaveState)?.text = ""
                dataFragment.setFilm(position)
            } else { // Pantalla grande
                dataFragment = FilmDataFragment()
                val args = Bundle()
                args.putInt(FilmDataFragment.PARAM_POSITION, position)
                dataFragment.arguments = args

                val t = supportFragmentManager.beginTransaction()
                t.replace(R.id.fragment_container, dataFragment)
                t.addToBackStack(null)
                t.commit()
            }
        } else {
            changeSelected(position, view)
        }
    }

    override fun onLongItemSelected(position: Int, view: View) {
        if (actionMode != null) {
            false
        }  else {
            changeSelected(position, view)
            actionMode = this@MainActivity.startActionMode(actionModeCallback)
            view.isSelected = true
            true
        }
    }

    public fun getListFragment() : FilmListFragment {
        var filmListFragment: FilmListFragment
        try {
            filmListFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as FilmListFragment
        } catch (e: Exception) {
            filmListFragment = supportFragmentManager.findFragmentById(R.id.film_list_fragment) as FilmListFragment
        }
        return filmListFragment
    }

    public fun getDataFragment() : FilmDataFragment? {
        var filmDataFragment: FilmDataFragment
        try {
            filmDataFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as FilmDataFragment
        } catch (e: Exception) {
            if (supportFragmentManager.findFragmentById(R.id.film_data_fragment) != null)
                filmDataFragment = supportFragmentManager.findFragmentById(R.id.film_data_fragment) as FilmDataFragment
            else
                return null
        }
        return filmDataFragment
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

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
        }
        var fragment = supportFragmentManager.findFragmentById(R.id.film_data_fragment)
        if (fragment != null) {
            (fragment as FilmDataFragment).updateImage()
        }
    }

    // Menú

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null) { // Pantalla pequeña
            when (currentFragment) {
                is FilmListFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayShowHomeEnabled(false)
                    menuInflater.inflate(R.menu.menu_list, menu)
                }
                is FilmDataFragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
                // Agrega más casos según tus fragmentos
            }
            return true
        } else {
            menuInflater.inflate(R.menu.menu_list, menu)
            return true
        }
    }

    override fun onOptionsItemSelected(elemento: MenuItem): Boolean {
        super.onOptionsItemSelected(elemento)
        when (elemento.itemId) {
            R.id.addFilm -> {
                addBlankFilm()
            }
            R.id.aboutUs -> {
                val aboutIntent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            android.R.id.home -> {
                onBackPressed()
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

        // Obtengo el fragment y notifico el cambio en la lista
        actualizarLista()
    }

    override fun onRestart() { // Para aplicar los cambios al volver a esta "activity" [onResume era otra posibilidad]
        super.onRestart()
        actualizarLista()
    }

    public fun actualizarLista() {
        try {
            var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as FilmListFragment?
            if (fragment != null) {
                (fragment?.listAdapter as FilmListAdapter).notifyDataSetChanged()
            } else {
                var fragmentListBig = supportFragmentManager.findFragmentById(R.id.film_list_fragment) as FilmListFragment?
                (fragmentListBig?.listAdapter as FilmListAdapter).notifyDataSetChanged()
            }
        } catch (e: Exception) { // Si no existe el ListFragment, entonces vendrá del dataFragment
            var fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as FilmDataFragment?
            fragment?.setFilm(fragment.getFilmIndex())
        }
    }
}