package com.harunuyan.countriesapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.harunuyan.countriesapp.model.Country
import com.harunuyan.countriesapp.service.CountryAPIService
import com.harunuyan.countriesapp.service.CountryDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

// ViewModel'i kaldırıp kendi oluşturduğumuz AndroidViewModel'dan extend alan BaseViewModel'ı veriyoruz.
class FeedViewModel(application: Application) : /* ViewModel() */ BaseViewModel(application) {
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
                        storeInSQLite(t)

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

    // onSuccess içerisinde yapacağımız işlemleri burada yapacağız.
    // bu fonksiyonu storeInSqLite() içerisine koyacağız.
    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryLoading.value = false
        countryError.value = false
    }

    // verileri sqlite'a kaydedeceğiz
    // Database'de suspend fun kullandığımızdan işlemleri coroutine ile yapacağız.
    private fun storeInSQLite(list: List<Country>) {
        // Database ile ilgili işlemleri yaparız.
        launch {
            // Dao'yu değişken olarak oluşturup suspend fonksiyonlara erişebiliriz
            val dao = CountryDatabase(getApplication()).countryDao()
            // Önce içerisinde ne varsa siliyoruz.
            dao.deleteAllCountries()
            // İçerisine ekleme yaptık.
            // Listeyi tekil eleman haline getirir. list -> individual
            val listLong = dao.insertAll(*list.toTypedArray())

            // Bize döndürülen uuid'leri listemiz içerisindeki elemanlara atıyoruz.
            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i += 1
            }
            // Son olarak ülkeleri gösteriyoruz. ve atamaları yapıyoruz.
            showCountries(list)
        }


    }
}