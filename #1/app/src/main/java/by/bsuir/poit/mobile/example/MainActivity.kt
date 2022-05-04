package by.bsuir.poit.mobile.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    fun onFeedTheCatClick(view: View) {
        val intent : Intent = Intent(this, FeedCatActivity::class.java)
        startActivity(intent)
    }

    fun onAboutClick(view: View) {
        val intent : Intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    fun onResultsClick(view: View) {
        val intent : Intent = Intent(this, ResultsActivity::class.java)
        startActivity(intent)
    }
}