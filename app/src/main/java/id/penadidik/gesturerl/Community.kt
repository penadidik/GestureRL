package id.penadidik.gesturerl

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_community.*

class Community: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val extras: Bundle? = intent.extras
        val titleCom = extras?.getString("title")

        titleCommunity.text = "Community from $titleCom"
    }
}