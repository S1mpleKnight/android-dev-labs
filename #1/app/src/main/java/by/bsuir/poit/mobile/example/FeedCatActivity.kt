package by.bsuir.poit.mobile.example

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FeedCatActivity : AppCompatActivity() {

    companion object {
        private var counter: Long = 0
        private const val PREFS_FILE = "Results"

        fun getCounter(): Long {
            return counter;
        }
    }

    private var counterLabel: TextView? = null
    private lateinit var feedAnimation: AnimationDrawable
    private lateinit var catImageView: ImageView
    private lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_cat)
        pref = getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        counterLabel = findViewById(R.id.satietyCounterLabel)
        counterLabel?.text = counter.toString()
        catImageView = findViewById(R.id.catImage)
    }

    fun onAddResults(view: View) {
        val editor: SharedPreferences.Editor = pref.edit()
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        editor.putString(dateFormat.format(date), getCounter().toString())
        editor.apply()
    }

    fun onFeedButtonClick(view: View) {
        counter++
        if (counter.mod(15) == 0) {
            catImageView.apply {
                setBackgroundResource(R.drawable.feed_animation)
                feedAnimation = background as AnimationDrawable
            }
            feedAnimation.start()
        }
        if (counter.mod(15) == 1) {
            catImageView.apply {
                setBackgroundResource(R.drawable.cat)
            }
        }
        counterLabel?.text = counter.toString()
    }

    fun onShareClick(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hey, checkout my FeedTheCat result")
        intent.putExtra(Intent.EXTRA_TEXT, counter.toString())
        startActivity(Intent.createChooser(intent, "Share using"))
    }
}