package es.ua.eps.filmoteca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import es.ua.eps.filmoteca.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .requestEmail()
            .build();
        DataUser.googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Opciones del botón
        binding.loginGoogle.setColorScheme(SignInButton.COLOR_DARK)
        binding.loginGoogle.setSize(SignInButton.SIZE_WIDE)
        binding.loginGoogle.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        handleAccount(account)
    }

    fun signIn() {
        val intent : Intent? = DataUser.googleSignInClient.signInIntent
        if (intent != null) {
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun handleSignInResult (completedTask: Task<GoogleSignInAccount >) {
        try {
            val account : GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            handleAccount (account)
        }
        catch (e: ApiException ) {
            Log. d("GPS", "SignInResult Failed: ${e.statusCode} ")
        }
    }
    private fun handleAccount (account: GoogleSignInAccount ?) {
        if(account != null) {
            DataUser.account = account

            val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mainIntent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}