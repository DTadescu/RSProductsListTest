package shdv.iotdev.rsproductstest.views

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.fragment_product_tax_view.*
import shdv.iotdev.rsproductstest.R
import java.lang.Exception
import com.bumptech.glide.Glide
import shdv.iotdev.rsproductstest.databinding.FragmentProductTaxViewBinding
import shdv.iotdev.rsproductstest.models.impl.ProductDetailModel
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM


/**
 * Fragment for showing taxonomy card of a product
 */
class ProductTaxView : Fragment() {

    private lateinit var binding: FragmentProductTaxViewBinding
    private var viewModel = ProductTaxVM.DEFAULT




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!this::binding.isInitialized){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_tax_view, container, false)

        }
        binding.taxModelview = viewModel
        binding.model = viewModel.model
        binding.addToCart.isEnabled = !viewModel.busy.get()
        binding.decQuantity.isEnabled = !viewModel.busy.get()
        binding.incQuantity.isEnabled = !viewModel.busy.get()


         return binding.root
    }

    /**
     * Set picture with Glide
     * @TODO: Replace to service for reusability
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

            Glide.with(this.context!!)
                .load(viewModel.model.imageUrl)
                .error(R.drawable.placeholder)
                .into(product_icon)
        }
        catch (e: Exception){
            e.printStackTrace()
            Log.d("IMAGE", e.message?:"")
        }
    }

    fun setViewModel(viewmodel: ProductTaxVM<ProductDetailModel>){
        viewModel = viewmodel
        if(this::binding.isInitialized){
            binding.taxModelview = viewModel
            binding.model = viewModel.model
        }

    }

    /**
     * Function, that @return true if image is on screen
     */
    fun checkViewOnScreen(bounds: Rect): Boolean =
        product_icon?.getLocalVisibleRect(bounds)?:false

}