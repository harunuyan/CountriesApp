package com.harunuyan.countriesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.harunuyan.countriesapp.R
import com.harunuyan.countriesapp.databinding.FragmentCountryBinding
import com.harunuyan.countriesapp.viewmodel.CountryViewModel
import com.harunuyan.countriesapp.util.downloadFromUrl
import com.harunuyan.countriesapp.util.placeHolderProgressBar

class CountryFragment : Fragment() {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CountryViewModel
    private var countryUuid = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid
        }

        viewModel = ViewModelProviders.of(this)[CountryViewModel::class.java]
        viewModel.getDataFromRoom(countryUuid)

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->
            country?.let {
                with(binding) {
                    countryName.text = country.countryName
                    countryCapital.text = country.countryCapital
                    countryCurrency.text = country.countryCurrency
                    countryLanguage.text = country.countryLanguage
                    countryRegion.text = country.countryRegion
                    // Extension fun.
                    countryFlag.downloadFromUrl(
                        // return to label, jump
                        country.imageUrl ?: return@let,
                        placeHolderProgressBar(context ?: return@let)
                    )
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
