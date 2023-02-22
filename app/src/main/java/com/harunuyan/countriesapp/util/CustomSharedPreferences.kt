package com.harunuyan.countriesapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {

    companion object {
        private val PREFERENCES_TIME = "preferences_time"
        // Null SharedPreference'ı null oluşturduk
        private var sharedPreferences: SharedPreferences? = null

        // class'tan oluşturulan tek objemiz bu olacak: instance
        @Volatile
        private var instance: CustomSharedPreferences? = null

        private val lock = Any()

        // obje var mı yok mu kontrol edilip varsa instance verilecek.
        operator fun invoke(context: Context): CustomSharedPreferences = instance ?: synchronized(
            lock
        ) {
            // yoksa senkronize bir şekilde shared preference oluşturma işlemi yapılacak.
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }
        private fun makeCustomSharedPreferences(context: Context): CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }
    }

    // Zamanı en küçük nanotime birimine inerek alacağız
    fun saveTime(time: Long) {
        // Varsayılan olarak false olduğundan commit'i true olarak belirtiriz
        sharedPreferences?.edit(commit = true) {
        putLong(PREFERENCES_TIME, time)
        }

    }
    // Zamanı alabilecek bir değer tanımladık
    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME,0)
}