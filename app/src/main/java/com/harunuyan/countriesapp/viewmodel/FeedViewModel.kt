package com.harunuyan.countriesapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harunuyan.countriesapp.model.Country
import com.harunuyan.countriesapp.service.CountryAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class FeedViewModel : ViewModel() {
    // Ülkeleri alacağımız liste. Değiştirilebilir olması için MutableLiveData kullanacağız ve liste olacağını belirteceğiz.
    val countries = MutableLiveData<List<Country>>()

    // Error tanımlamıştık. Olursa hata göstereceğimiz için boolean olarak belirteceğiz.
    val countryError = MutableLiveData<Boolean>()

    // Yükleniyor/Yüklenmiyor için boolean belirttiğimiz LiveData'yı kullanacağız.
    val countryLoading = MutableLiveData<Boolean>()

    // ApiService nesnemizi oluşturacağız.
    private val countryAPIService = CountryAPIService()

    /* Disposable oluşturacağız. Disposable: İnternetten veri indirirken her call hafızada bir yer
    kaplar ve fragmentlar kapandığında bu call'lardan kurtulmamızı sağlar. İndirdiğimiz verileri
    disposable içerisine atarız. Hafızamızı verimli kullanmamız için bu yapıyı kullanmamız gerekir */
    private val disposable = CompositeDisposable()


    fun refreshData() {
        getDataFromAPI()
    }
    // Ayrı fun oluşturacağız çünkü internetten ve sqLite'dan ayrı veriler çekeceğiz.

    private fun getDataFromAPI() {
        // Kullanıcı ilk önce progress bar'ı görecek.
        countryLoading.value = true

        disposable.add(
            countryAPIService.getData()
                // Yeni bir thread'de yapacak.
                .subscribeOn(Schedulers.newThread())
                // Ana thread'de göstereceğimizi belirtiyoruz.
                .observeOn(AndroidSchedulers.mainThread())
                // SinleObserver asyncronous bir şekilde bu disposable'ları yönetir.
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {

                    // Başarılı olursa :
                    override fun onSuccess(t: List<Country>) {
                        countries.value = t
                        countryLoading.value = false
                        countryError.value = false
                    }

                    // Hata olursa :
                    override fun onError(e: Throwable) {
                        countryLoading.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }

                }
                ))

    }
}