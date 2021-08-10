package com.dave.springchatapp.tools

import android.content.Context
import android.content.DialogInterface
import android.view.Window
import androidx.appcompat.app.AlertDialog

/**
 * Created by David Hakobyan on 6/25/21.
 */
class DialogShower {
    companion object {
        fun showSimpleOKDialog(
            context: Context,
            title: String?,
            message: String?,
            listener: DialogInterface.OnClickListener?
        ) {
            val alertBuilder = AlertDialog.Builder(context)
            alertBuilder.setTitle(title)
            alertBuilder.setMessage(message)
            alertBuilder.setCancelable(false)
            alertBuilder.setPositiveButton("OK", listener)
            val alertDialog = alertBuilder.create()
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog.show()
        }
    }
}