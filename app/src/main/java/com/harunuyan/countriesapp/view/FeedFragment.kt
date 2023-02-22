package com.harunuyan.countriesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.harunuyan.countriesapp.R
import com.harunuyan.countriesapp.adapter.CountryAdapter
import com.harunuyan.countriesapp.databinding.FragmentFeedBinding
import com.harunuyan.countriesapp.viewmodel.FeedViewModel

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FeedViewModel
    private var countryAdapter = CountryAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* ViewModelProviders kullanarak hangi fragmentte olduğumuzu ve hangi viewModel sınıfıyla
         çalışmak istediğimizi söyleyebiliriz */
        viewModel = ViewModelProviders.of(this)[FeedViewModel::class.java]
        viewModel.refreshData()

        // Adapter'ı layout ile eşleştirip layoutManager'ını verdik
        binding.countryList.layoutManager = LinearLayoutManager(this.context)
        binding.countryList.adapter = countryAdapter

        // Fonksiyonumuzun çalışması için onViewCreated altında çağırdık.

        // Swipe'ı elden geçirip observeLiveData'yı sonra çağıracağız.

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.apply {
                countryList.visibility = View.GONE
                countryError.visibility = View.GONE
                countryLoading.visibility = View.VISIBLE
            }
            // Local database yerine API'den verileri alacağız.
            viewModel.refreshFromAPI()

            // Kendi eklediğimiz progress bar gözüksün diye
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()


    }

    // Verileri gözlemlemek için yine viewModel kullanırız. Observer.
    private fun observeLiveData() {
        /* Observer bize CountryList verir çünkü mutableData'ya Country tipinde bir list
        olacağını söylemiştik. Null gelme ihtimali olduğundan .let ile kontrol yaparız. Eğer null
        gelmiyorsa veriler. recyclerView'i görünür UI'daki error ve progress'i görünmez yapabiliriz
        */
        viewModel.countries.observe(viewLifecycleOwner, Observer { countries ->
            countries?.let {
                binding.countryList.visibility = View.VISIBLE
                /* Adapter'daki, güncellemeleri recyclerView'a haber vermek için oluşturduğumuz
                fonksiyonu çağırıp verilerimizi verdik.
                 */
                countryAdapter.updateCountryList(countries)
            }
        })
        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                // it = boolean. Boolean true ise hata mesajı var demektir.
                if (it) {
                    // RecyclerView ve Loading gizlenmesi için
                    binding.apply {
                        countryError.visibility = View.VISIBLE
                        countryLoading.visibility = View.GONE
                        countryList.visibility = View.GONE
                    }
                } else {
                    // View.GONE = Gösterme
                    binding.countryError.visibility = View.GONE
                }
            }
        })
        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {

                    // RecyclerView ve ErrorMessage gizlenmesi için
                    binding.apply {
                        countryLoading.visibility = View.VISIBLE
                        countryList.visibility = View.GONE
                        countryError.visibility = View.GONE
                    }
                } else {
                    binding.countryLoading.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}