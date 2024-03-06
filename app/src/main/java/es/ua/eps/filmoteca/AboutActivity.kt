package es.ua.eps.filmoteca

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.net.toUri
import es.ua.eps.filmoteca.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAccountInfo()

        binding.buttonWeb.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://es.wikipedia.org/wiki/Patricio_Estrella"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }

        binding.buttonSupport.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            val viewIntent = Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:vck1@gcloud.ua.es"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }

        binding.buttonReturnMainMenu.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            finish()
        }
    }

    fun setUpAccountInfo() {
        binding.userId.text = "ID: " + DataUser.account.id
        binding.userName.text = "Name: " + DataUser.account.displayName
        binding.userMail.text = "Email: " + DataUser.account.email
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }
}