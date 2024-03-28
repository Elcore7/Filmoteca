package es.ua.eps.filmoteca

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import es.ua.eps.filmoteca.classes.Film
import es.ua.eps.filmoteca.sources.FilmDataSource
import java.io.IOException
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var token: String
    val channelId = "movies"

    // La notificacion recibida, tendrá los siguientes campos:
    /*
        title,
        director,
        year,
        comment,
        genre,
        format,
        imdb,
        image,
        action (Este puede ser: "create" o "delete")
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) { // Mensaje entrante
        /*val title = remoteMessage.notification?.title // Titulo y cuerpo de mensaje
        val body = remoteMessage.notification?.body
        val imageUrl = remoteMessage.notification?.imageUrl*/
        // val pruebaTitle = remoteMessage.data.getValue("title")

        showNotification(remoteMessage)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val notifId = 111;

        val messageTitle = remoteMessage.notification?.title // Titulo del mensaje
        val action = remoteMessage.data.getValue("action")

        if ("create".equals(action)) { // Se añade la pelicula
            anyadirPelicula(remoteMessage.data)
        } else if ("delete".equals(action)) { // Se borra la pelicula (Solo se espera el nombre de la que hay que borrar)
            remoteMessage.data.get("title")?.let { borrarPelicula(it) }
        }

        // Crea un intent para abrir la aplicación cuando se toque la notificación
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        // Crea la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(messageTitle)
            .setContentText(remoteMessage.data.get("title"))
            .setSmallIcon(R.drawable.baseline_videocam_24) // Icono de la notificación (La misma usada en el Login)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Cierra la notificación cuando se toca

        remoteMessage.data.get("image")?.let { // Para la imagen, si existe se muestra, si no, no
            try {
                val bitmap = BitmapFactory.decodeStream(URL(remoteMessage.data.get("image").toString()).openConnection().getInputStream())
                notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // A partir de Oreo (api 26), se debe poner un "canal"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Crea un canal de notificación
            val channel = NotificationChannel(
                channelId,
                "movies",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Muestra la notificación
        notificationManager.notify(notifId, notificationBuilder.build())
    }

    fun anyadirPelicula(data: MutableMap<String, String>) {
        var newFilm: Film = Film()
        newFilm.title = data.get("title") // Uso get en vez de getValue, ya que no me interesa lanzar excepción sino que simplemente el campo se a null
        newFilm.director = data.get("director")
        newFilm.year = data.get("year").toString().toInt()
        newFilm.genre = data.get("genre").toString().toInt()
        newFilm.format = data.get("format").toString().toInt()
        newFilm.imageResId = -1
        try {
            val url = URL(data.get("image"))
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            newFilm.bitmapImage = image
        } catch (e: IOException) {
            println(e)
        }
        newFilm.imdbUrl = data.get("imdb")
        newFilm.comments = data.get("comment")

        FilmDataSource.films.add(newFilm)
    }

    fun borrarPelicula(filmTitle: String) {
        var found: Boolean = false
        var iterator: Int = 0;
        for (film in FilmDataSource.films) {
            if (film.title == filmTitle) {
                found = true
                break
            }
            iterator++
        }
        // Otra forma seria haciendo uso de "removeIf" pero no lo uso para evitar que tener que controlar entre versiones, solución simple y ya
        /*val condition = Predicate<Film> {
            it.title == filmTitle
        }*/
        if (found) {
            FilmDataSource.films.removeAt(iterator)
            // FilmDataSource.films.removeIf(condition)
        }
    }

    fun cargarMainIntent() {

    }

    fun askToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener<String?> { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                token = task.result
                Log.d(TAG, "FCM registration Token: ${token}")
            })
    }

    override fun onNewToken(newToken: String) {
        token = newToken
        Log.d(TAG, "Refreshed token: $token")
    }
}