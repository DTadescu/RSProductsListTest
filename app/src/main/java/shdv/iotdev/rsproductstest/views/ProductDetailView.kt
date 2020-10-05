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
import shdv.iotdev.rsproductstest.MainActivity
import shdv.iotdev.rsproductstest.R
import shdv.iotdev.rsproductstest.databinding.FragmentProductDetailViewBinding
import shdv.iotdev.rsproductstest.databinding.FragmentProductTaxViewBinding
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.models.impl.ProductTaxModel
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM


class ProductDetailView(private val viewModel: ProductTaxVM<ProductDetailModel> = ProductTaxVM(ProductDetailModel())) : Fragment() {

    private lateinit var binding: FragmentProductDetailViewBinding
    private var model: ProductDetailModel = viewModel.model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!this::binding.isInitialized){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail_view, container, false)
        }
        binding.model = model
        binding.viewmodel = viewModel
        binding.categoriesList.adapter = ArrayAdapter<String>(context!!, R.layout.list_text_item, model.categories.toTypedArray())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(this.context?: MainActivity.context)
            .load(model.imageUrl)
            .into(product_icon)

        super.onViewCreated(view, savedInstanceState)
    }


}