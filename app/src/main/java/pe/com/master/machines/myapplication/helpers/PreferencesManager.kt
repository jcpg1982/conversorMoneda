package pe.com.master.machines.myapplication.helpers

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

class PreferencesManager @Inject constructor(context: Context) {

    val mPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val mEditor = mPreferences.edit()

}