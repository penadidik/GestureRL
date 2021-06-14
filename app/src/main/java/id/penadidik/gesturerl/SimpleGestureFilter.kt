package id.penadidik.gesturerl

import android.app.Activity
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

class SimpleGestureFilter internal constructor(
    private val context: Activity,
    simpleGestureListener: SimpleGestureListener
) : SimpleOnGestureListener() {
    // Swipe distances
    var swipeMinDistance = 100
    var swipeMaxDistance = 350
    var swipeMinVelocity = 100
    var mode = MODE_DYNAMIC
    private var running = true
    private var tapIndicator = false
    private val detector: GestureDetector
    private val listener: SimpleGestureListener
    fun onTouchEvent(event: MotionEvent) {
        if (!running) return
        val result = detector.onTouchEvent(event)
        // Get the gesture
        if (mode == MODE_SOLID) event.action =
            MotionEvent.ACTION_CANCEL else if (mode == MODE_DYNAMIC) {
            if (event.action == ACTION_FAKE) event.action =
                MotionEvent.ACTION_UP else if (result) event.action =
                MotionEvent.ACTION_CANCEL else if (tapIndicator) {
                event.action = MotionEvent.ACTION_DOWN
                tapIndicator = false
            }
        }
        // else just do nothing, it's Transparent
    }

    fun setEnabled(status: Boolean) {
        running = status
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        var velocityX = velocityX
        var velocityY = velocityY
        val xDistance = Math.abs(e1.x - e2.x)
        val yDistance = Math.abs(e1.y - e2.y)
        if (xDistance > swipeMaxDistance
            || yDistance > swipeMaxDistance
        ) return false
        velocityX = Math.abs(velocityX)
        velocityY = Math.abs(velocityY)
        var result = false

        if (velocityX > this.swipeMinVelocity
            && xDistance > this.swipeMinDistance
        ) {
            if (e1.x > e2.x) // right to left
                listener.onSwipe(SWIPE_LEFT) else listener.onSwipe(
                SWIPE_RIGHT
            )
            result = true
        } else if (velocityY > this.swipeMinVelocity
            && yDistance > this.swipeMinDistance
        ) {
            if (e1.y > e2.y) // bottom to up
                listener.onSwipe(SWIPE_UP) else listener.onSwipe(
                SWIPE_DOWN
            )
            result = true
        }
        return result
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        tapIndicator = true
        return false
    }

    override fun onDoubleTap(arg: MotionEvent): Boolean {
        listener.onDoubleTap()
        return true
    }

    override fun onDoubleTapEvent(arg: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(arg: MotionEvent): Boolean {
        if (mode == MODE_DYNAMIC) { // we owe an ACTION_UP, so we fake an
            arg.action = ACTION_FAKE // action which will be converted to an
            // ACTION_UP later.
            context.dispatchTouchEvent(arg)
        }
        return false
    }

    internal interface SimpleGestureListener {
        fun onSwipe(direction: Int)
        fun onDoubleTap()
    }

    companion object {
        // Swipe gestures type
        const val SWIPE_UP = 1
        const val SWIPE_DOWN = 2
        const val SWIPE_LEFT = 3
        const val SWIPE_RIGHT = 4
        const val MODE_TRANSPARENT = 0
        const val MODE_SOLID = 1
        const val MODE_DYNAMIC = 2
        private const val ACTION_FAKE = -13 // just an unlikely number
    }

    init {
        detector = GestureDetector(context, this)
        listener = simpleGestureListener
    }
}