package id.penadidik.gesturerl

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SimpleGestureFilter.SimpleGestureListener {
    private var detector: SimpleGestureFilter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Detect touched area
        detector = SimpleGestureFilter(this@MainActivity, this)
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        // Call onTouchEvent of SimpleGestureFilter class
        detector!!.onTouchEvent(me)
        return super.dispatchTouchEvent(me)
    }

    override fun onSwipe(direction: Int) {

        //Detect the swipe gestures and display toast
        var showToastMessage = ""
        val intent = Intent(this, Community::class.java)
        when (direction) {
            SimpleGestureFilter.SWIPE_RIGHT -> {
                intent.putExtra("title", "Right")
                startActivity(intent)
            }
            SimpleGestureFilter.SWIPE_LEFT -> {
                intent.putExtra("title", "Left")
                startActivity(intent)
            }
            SimpleGestureFilter.SWIPE_DOWN -> showToastMessage = "You have Swiped Down."
            SimpleGestureFilter.SWIPE_UP -> showToastMessage = "You have Swiped Up."
        }
    }

    //Toast shown when double tapped on screen
    override fun onDoubleTap() {
        Toast.makeText(this, "You have Double Tapped.", Toast.LENGTH_SHORT)
                .show()
    }
}