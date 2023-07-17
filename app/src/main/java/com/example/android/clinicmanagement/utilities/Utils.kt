package com.example.android.clinicmanagement.utilities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.animation.doOnEnd
import androidx.core.view.drawToBitmap
import com.example.android.clinicmanagement.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*


/**
 * Potentially animate showing a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot of the view, and animate this in, only changing the visibility (and
 * thus layout) when the animation completes.
 */
fun BottomNavigationView.show() {
    if (visibility == View.VISIBLE) return

    val parent = parent as ViewGroup
    // View needs to be laid out to create a snapshot & know position to animate. If view isn't
    // laid out yet, need to do this manually.
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        startDelay = resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
        duration = resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.linear_out_slow_in
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            visibility = View.VISIBLE
        }
        start()
    }
}

/**
 * Potentially animate hiding a [BottomNavigationView].
 *
 * Abruptly changing the visibility leads to a re-layout of main content, animating
 * `translationY` leaves a gap where the view was that content does not fill.
 *
 * Instead, take a snapshot, instantly hide the view (so content lays out to fill), then animate
 * out the snapshot.
 */
fun BottomNavigationView.hide() {
    if (visibility == View.GONE) return

    if (isLaidOut) {
        val drawable = BitmapDrawable(context.resources, drawToBitmap())

        val parent = parent as ViewGroup
        drawable.setBounds(left, top, right, bottom)
        parent.overlay.add(drawable)
        visibility = View.GONE
        ValueAnimator.ofInt(top, parent.height).apply {
            startDelay = resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            duration = resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            interpolator = AnimationUtils.loadInterpolator(
                context,
                android.R.interpolator.fast_out_linear_in
            )
            addUpdateListener {
                val newTop = it.animatedValue as Int
                drawable.setBounds(left, newTop, right, newTop + height)
            }
            doOnEnd {
                parent.overlay.remove(drawable)
            }
            start()
        }
    } else {
        visibility = View.GONE
    }
}

fun View.crossFadeIn() {
    // Set the content view to 0% opacity but visible, so that it is visible
    // (but fully transparent) during the animation.
    alpha = 0f
    visibility = View.VISIBLE

    // Animate the content view to 100% opacity, and clear any animation
    // listener set on the view.
    animate()
        .alpha(1f)
        .setDuration(resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong())
        .setListener(null)
}

fun View.crossFadeOut() {
    animate()
        .alpha(0f)
        .setDuration(resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}
fun View.scaleUp() {
    visibility = View.VISIBLE
    alpha=0f
    scaleX = 0f
    scaleY=0f
    animate()
        .alpha(1f)
        .scaleX(1f).scaleY(1f)
        .setDuration(resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong())
        .setListener(null)
}

fun View.scaleDown() {
    animate()
        .alpha(0f)
        .scaleX(0f).scaleY(0f)
        .setDuration(resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}


sealed class UiState {
    data class Loading(@StringRes val messageResource:Int) : UiState()
    data class Success<T>(val content: T) : UiState()
    data class Failure(@StringRes val tagLineResource: Int,
                       @StringRes val messageResource: Int,
                       @DrawableRes val imageDrawableRes:Int
                       ) : UiState()
}


/**
* This method converts a date in format  dd/MM/yyyy to date in seconds for
 * use in persisting date into the database
* @param dateString must be in format dd/MM/yyyy
 */
fun convertDateStringToDateSeconds(dateString:String):Long{

    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    //We add noon time to data string here to avoid mistakes in dates
    //for countries that add one hour in summer
    val date = dateFormat.parse("$dateString 12:00:00") as Date
    return date.time.div(1000)
}
/**
* This method converts date in seconds to date in format dd/MM/yyyy
* @param dateInSeconds the date in seconds
 */
fun convertDateSecondsToDateString(dateInSeconds:Long):String{
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(dateInSeconds*1000)
}

/**
 * This method converts a date time in format  dd/MM/yyyy HH:mm to date in seconds for
 * use in persisting date and time in database as one single dateInSeconds field.
 * @param dateString must be in format dd/MM/yyyy
 * @param timeString must be in format HH:mm
 */
fun convertDateTimeStringToDateSeconds(dateString:String,timeString:String):Long{
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val date = dateTimeFormat.parse("$dateString $timeString") as Date
    return date.time.div(1000)
}




/**
 * This method checks if the passed string argument is not blank
 * , if it is not blank it returns the passed string itself
 * , if it is blank it returns null instead
 */
fun getStringOrNull(str: String): String? {
    return str.takeIf { it.isNotBlank() }
}



const val MALE_CHARACTER = 'M'
const val FEMALE_CHARACTER = 'F'

const val SESSIONS_COMPLETION_STATE_COMPLETED = 1
const val SESSIONS_COMPLETION_STATE_IN_PROGRESS = 0


