package es.ua.eps.filmoteca

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

object DataUser {
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var account: GoogleSignInAccount
}