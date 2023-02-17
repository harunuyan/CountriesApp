package com.harunuyan.countriesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harunuyan.countriesapp.model.Country

class CountryViewModel: ViewModel() {

    val countryLiveData = MutableLiveData<Country>()

    // Room kullanıp verileri local olarak kaydedeceğiz.
    fun getDataFromRoom() {
        val country = Country("Turkey","Asia","Ankara", "TRY", "Turkish","com")

        countryLiveData.value = country
    }
}