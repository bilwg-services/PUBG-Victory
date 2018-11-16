package com.deucate.pubgvictory

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast

class InitialClass(val context: Context) {

    var listner: OnCallBack? = null

    fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("OK") { _, _ -> }.show()
    }

    fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showAlertDialogWithAction(title: String, message: String, id: Int) {
        AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton("OK") { dialog, which ->
            listner!!.onAlertDialogPositiveAction(id, dialog, which)
        }.setNegativeButton("NO") { dialog, which ->
            listner!!.onAlertDialogNegativeAction(id, dialog, which)
        }.show()
    }

    interface OnCallBack {
        fun onAlertDialogPositiveAction(id: Int, dialog: DialogInterface?, which: Int)
        fun onAlertDialogNegativeAction(id: Int, dialog: DialogInterface?, which: Int)
    }

}