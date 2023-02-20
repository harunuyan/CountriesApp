package com.harunuyan.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.harunuyan.countriesapp.R

// Extension fun ile Glide kullanacağız.
fun ImageView.downloadFromUrl(url: String, progressDrawable: CircularProgressDrawable) {

    // placeholder(): görsel inene kadar ne göstereceğiz:
    val options = RequestOptions()
        .placeholder(progressDrawable)
        // Hata varsa bu göresli göstereceğiz.
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)

}

//.placeholder(,) içerisine göndermek için CircularProgressDrawable döndüren bir fun yazdık.
fun placeHolderProgressBar(context: Context): CircularProgressDrawable {
    // Bizden istediği context'i fonksiyonda parametre olarak isteyeceğiz.
    return CircularProgressDrawable(context).apply {
        // Özelliklerini burda yazabiliriz.
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}