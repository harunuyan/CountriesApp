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

    }
}