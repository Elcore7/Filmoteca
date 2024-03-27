package es.ua.eps.filmoteca

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.ua.eps.filmoteca.adapters.FilmListAdapter
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.databinding.ActivityMainBinding
import es.ua.eps.filmoteca.sources.FilmDataSource

class MainActivity : AppCompatActivity(), FilmListFragment.OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

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
        }

        // Permiso notif. push
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf("android.permission.POST_NOTIFICATIONS"), 101)
            }
        }
    }

    override fun onItemSelected(position: Int) {
        var dataFragment = supportFragmentManager.findFragmentById(R.id.film_data_fragment) as FilmDataFragment?

        if (dataFragment != null) { // Existe fragmento de "data" -> Pantalla pequeña
            // var list = supportFragmentManager.findFragmentById(R.id.film_list_fragment)
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
    }

    private fun loadImagesToObjects() {
        for (filmAux: Film in FilmDataSource.films) {
            filmAux.bitmapImage = BitmapFactory.decodeResource(resources, filmAux.imageResId)
        }
    }

    // Menú

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
                val aboutIntent = Intent(this@MainActivity, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            R.id.closeSession -> {
                DataUser.googleSignInClient?.signOut()
                val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(loginIntent)
            }
            R.id.disconnectAccount -> {
                DataUser.googleSignInClient?.revokeAccess()
                val loginIntent = Intent(this@MainActivity, LoginActivity::class.java)
                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(loginIntent)
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

    override fun onBackPressed() { // Finaliza la app (Evitamos volver al activity de login)
        finish()
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