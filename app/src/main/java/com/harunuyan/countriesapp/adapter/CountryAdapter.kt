package com.harunuyan.countriesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harunuyan.countriesapp.databinding.ItemCountryBinding
import com.harunuyan.countriesapp.model.Country

class CountryAdapter(private val countryList: ArrayList<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    // this view created onCreateViewHolder and sended parameter this class
    inner class CountryViewHolder(var binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                countryNameItem.text = countryList[position].countryName
                countryRegionItem.text = countryList[position].countryRegion
            }
        }
    }

    // bind item_country.xml - Adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    //
    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    /**
     * İtemler güncellendiğinde adapter'a haber vermek için oluşturduğumuz fun.
     * Önce liste .clear() ile temizlenir.
     * Bize verilen newCountryList'deki değişiklikler countryList'e eklenir ve adapter'a haber verilir.
     * notifyDataSetChanged() adapter'ı yenilemek için kullandığımız yapıdır.
     * Böylece itemler ve değişiklikler güncellenmiş olur.
     */
    fun updateCountryList(newCountryList: List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }
}