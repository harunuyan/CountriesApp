package com.harunuyan.countriesapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harunuyan.countriesapp.model.Country

/* Bu veritabanında birden fazla obje oluşmasın istiyoruz. Farklı zamanlarda ve farklı thread'lerden
veritabanına ulaşılırsa çakışma oluşturmamalıdır. Aşağıda singleton yapı kullanmalıyız */

@Database(entities = [Country::class], version = 1)
abstract class CountryDatabase : RoomDatabase() {

    // Dao class'ı implement edilir.
    abstract fun countryDao(): CountryDao

    // Sinleton

    // Statik bir şekilde değişken oluşturup sınıfın dışından ulaşmamızı sağlayacak
    companion object {
        // Farklı thread'lere de görünür halde
        @Volatile
        private var instance: CountryDatabase? = null

        // synchronized'e parametre vermek için oluşturuldu.
        private val lock = Any()

        // instance'ın varlığını kontrol edecek. Yok ise oluşturacak yok ise oluşturacak
        // var ise instance'ı döndürecek.
        // synchronized() -> aynı anda birden fazla thread bu objeye ulaşamayacak
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            // Eğer yok ise makeDatabase'i çağır.
            instance ?: makeDatabase(context).also {
                // ve instance'ı ona eşitle.
                instance = it
            }
        }

        // Database oluşturucu
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        ).build()
    }
}