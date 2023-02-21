package com.harunuyan.countriesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/* Coroutines kullanmak için class'ta belirtmemiz gerektiğinden ayrı bir class açıp
viewModel ve coroutines extend alıp işlemlerimizi burada yapacağız.
AndroidViewModel: içerisine context istediğinden kullanırız. ApplicationContext vererek uygulamanın
destroy ve fragment'ın clear olma durumlarında kopmalar yaşamayacağız. ViewModel yerine AndroidView
Model kullanmak daha mantıklıdır ve daha güvenlidir. applicationContext kullanmak için class paramet-
resinde istememiz gerekiyor
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    // Coroutine kullanmak için bunu yapmalıyız.

    // Coroutine'in ne yapacağını belirtmeliyiz. Arkaplanda yapılacak bir iş oluşturur.
    private val job = Job()

    // Önce işi yapıp ardından main thread'e dönecek.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // Eğer App context'i de giderse bu iş iptal edilecektir.
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}