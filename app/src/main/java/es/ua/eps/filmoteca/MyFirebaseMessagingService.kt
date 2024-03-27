package es.ua.eps.filmoteca

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging
import java.io.IOException
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {

    lateinit var token: String
    val channelId = "movies"

    override fun onMessageReceived(remoteMessage: RemoteMessage) { // Mensaje entrante
        val title = remoteMessage.notification?.title // Titulo y cuerpo de mensaje
        val body = remoteMessage.notification?.body
        val imageUrl = remoteMessage.notification?.imageUrl

        if (title != null && body != null) { // Si no son nulos, notificacion
            showNotification(title, body, imageUrl)
        }
    }

    private fun showNotification(title: String, body: String, imageUrl: Uri?) {
        val notifId = 111;

        // Crea un intent para abrir la aplicación cuando se toque la notificación
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        // Crea la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_videocam_24) // Icono de la notificación (La misma usada en el Login)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Cierra la notificación cuando se toca

        imageUrl?.let { // Para la imagen
            try {
                val bitmap = BitmapFactory.decodeStream(URL(imageUrl.toString()).openConnection().getInputStream())
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