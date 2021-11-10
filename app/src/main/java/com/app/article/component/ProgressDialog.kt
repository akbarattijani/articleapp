package com.app.article.component

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.Window
import com.app.article.R

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

class ProgressDialog(private val activity: Activity) : Dialog(activity) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.custom_dialog_progressdialog)
        setCancelable(false)
    }

    @SuppressLint("NewApi")
    override fun show() {
        if (!activity.isDestroyed && !isShowing) {
            super.show()
        }
    }

    @SuppressLint("NewApi")
    override fun dismiss() {
        if (!activity.isDestroyed) {
            super.dismiss()
        }
    }
}