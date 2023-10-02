package com.example.android.clinicmanagement.utilities

import android.animation.Animator

 interface AnimationEndListener : Animator.AnimatorListener {
    override fun onAnimationStart(p0: Animator) {
        //no implementation
    }

    override fun onAnimationCancel(p0: Animator) {
        //no implementation
    }

    override fun onAnimationRepeat(p0: Animator) {
        //no implementation
    }
}