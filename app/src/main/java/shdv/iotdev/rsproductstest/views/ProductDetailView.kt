package shdv.iotdev.rsproductstest.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_product_tax_view.*
import shdv.iotdev.rsproductstest.R
import shdv.iotdev.rsproductstest.databinding.FragmentProductDetailViewBinding
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.utils.setHeightByContent
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM


class ProductDetailView() : Fragment() {

    private lateinit var binding: FragmentProductDetailViewBinding
    private var viewModel = ProductTaxVM.DEFAULT


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!this::binding.isInitialized){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_view, container, false)
        }
        binding.model = viewModel.model
        binding.viewmodel = viewModel
        binding.categoriesList.adapter = ArrayAdapter<String>(context!!, R.layout.list_text_item, viewModel.model.categories.toTypedArray())

        // set listview height depends on items
        binding.categoriesList.setHeightByContent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this.context!!)
            .load(viewModel.model.imageUrl)
            .into(product_icon)

    }

    class Builder() {

        private var viewmodel: ProductTaxVM<ProductDetailModel> = ProductTaxVM.DEFAULT

        fun setViewModel(viewmodel: ProductTaxVM<ProductDetailModel>) {
            this.viewmodel = viewmodel
        }

        fun build() = ProductDetailView().apply {
            viewModel = viewmodel
        }
    }

}