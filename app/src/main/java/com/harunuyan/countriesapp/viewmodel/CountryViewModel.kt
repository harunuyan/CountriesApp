package com.harunuyan.countriesapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.harunuyan.countriesapp.model.Country
import com.harunuyan.countriesapp.service.CountryDatabase
import kotlinx.coroutines.launch

// Coroutine kullanacağımızda BaseViewModel den extend alacağız
class CountryViewModel(application: Application) : BaseViewModel(application) {

    val countryLiveData = MutableLiveData<Country>()

    // Room kullanıp verileri local olarak kaydedeceğiz.
    fun getDataFromRoom(uuid: Int) {
        launch {
        val dao = CountryDatabase(getApplication()).countryDao()
            // Lazım olan id'yi fonksiyon parametresinden istedik
            val country = dao.getCountry(uuid)
            countryLiveData.value = country
        }
    }
}