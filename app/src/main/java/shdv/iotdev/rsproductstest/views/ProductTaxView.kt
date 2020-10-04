package shdv.iotdev.rsproductstest.views

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import kotlinx.android.synthetic.main.fragment_product_tax_view.*
import shdv.iotdev.rsproductstest.R
import java.lang.Exception
import com.bumptech.glide.Glide
import shdv.iotdev.rsproductstest.MainActivity
import shdv.iotdev.rsproductstest.databinding.FragmentProductTaxViewBinding
import shdv.iotdev.rsproductstest.models.impl.ProductTaxModel
import shdv.iotdev.rsproductstest.viewmodels.base.IProductVM
import shdv.iotdev.rsproductstest.viewmodels.impl.ProductTaxVM


/**
 * A simple [Fragment] subclass.
 * Use the [ProductTaxView.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductTaxView (private val viewModel: ProductTaxVM = ProductTaxVM.DEFAULT) : Fragment() {

    private lateinit var binding: FragmentProductTaxViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if(!this::binding.isInitialized){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_tax_view, container, false)

        }
        binding.taxModelview = viewModel
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
           // product_icon.setImageURI(Uri.parse("https://picsum.photos/id/124/200/300"))
            //binding.taxProductCategory.text = binding.taxModel?.category?:""
            Glide.with(this.context?:MainActivity.context)
                .load(binding.taxModelview?.model?.imageUrl)
                .into(product_icon)
            Log.d("IMAGE", "Success to set image")
        }
        catch (e: Exception){
            Log.d("IMAGE", e.message?:"")
        }
        super.onViewCreated(view, savedInstanceState)
    }



}