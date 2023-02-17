package com.harunuyan.countriesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harunuyan.countriesapp.model.Country

class FeedViewModel : ViewModel() {
    // Ülkeleri alacağımız liste. Değiştirilebilir olması için MutableLiveData kullanacağız ve liste olacağını belirteceğiz.
    val countries = MutableLiveData<List<Country>>()

    // Error tanımlamıştık. Olursa hata göstereceğimiz için boolean olarak belirteceğiz.
    val countryError = MutableLiveData<Boolean>()

    // Yükleniyor/Yüklenmiyor için boolean belirttiğimiz LiveData'yı kullanacağız.
    val countryLoading = MutableLiveData<Boolean>()


    fun refreshData() {
        val country = Country("Turkey", "Europe", "Ankara", "TRY", "Turkish", ".com")
        val country2 = Country("france", "Europe", "Paris", "Euro", "French", ".com")
        val country3 = Country("Deutchland", "Europe", "Ankara", "TRY", "Turkish", ".com")
        val country4 = Country("Turkey", "Europe", "Ankara", "TRY", "Turkish", ".com")
        val country5 = Country("Turkey", "Europe", "Ankara", "TRY", "Turkish", ".com")

        val countryList = arrayListOf<Country>(country, country2, country3, country4, country5)

        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }
}