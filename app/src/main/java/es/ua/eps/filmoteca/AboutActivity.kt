package es.ua.eps.filmoteca

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import es.ua.eps.filmoteca.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            val viewIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://es.wikipedia.org/wiki/Patricio_Estrella"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }

        binding.button2.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            val viewIntent = Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:vck1@gcloud.ua.es"))
            if(viewIntent.resolveActivity(packageManager) != null) {
                startActivity(viewIntent)
            }
        }

        binding.button3.setOnClickListener{
            //Toast.makeText(this, resources.getString(R.string.NO_FUNC_ERROR), Toast.LENGTH_LONG).show()
            finish()
        }
    }
}