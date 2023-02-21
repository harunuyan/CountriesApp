package com.harunuyan.countriesapp.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harunuyan.countriesapp.model.Country

@Dao
interface CountryDao {
    // Data Access Object

    // INSERT INTO
    @Insert
    // suspend - > coroutine, pause & resume
    // vararg -> multipy country object
    // List<Long> return primaryKeys
    suspend fun insertAll(vararg countries: Country): List<Long>


    // @Entity'i boş bıraktığımız için sınıf ismimizle verileri çekeceğiz.
    @Query("SELECT * FROM country")
    suspend fun getAllCountries(): List<Country>

    @Query("SELECT * FROM country WHERE uuid = :countryId")
    suspend fun getCountry(countryId: Int): Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()
}