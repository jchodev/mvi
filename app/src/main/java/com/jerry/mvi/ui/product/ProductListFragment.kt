package com.jerry.mvi.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jerry.mvi.R
import com.jerry.mvi.base.BaseFragment
import com.jerry.mvi.base.ViewState
import com.jerry.mvi.databinding.FragmentProductListBinding
import com.jerry.mvi.ui.product.adapter.ProductAdapter
import com.jerry.mvi.ui.product.intent.ProductListIntent
import com.jerry.mvi.ui.product.viewmodel.ProductListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductListFragment : BaseFragment(R.layout.fragment_product_list) {

    private val viewModel by viewModels<ProductListViewModel>()
    private lateinit var productAdapter: ProductAdapter
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProductListBinding.bind(view)

        //setup swipe Refresh
        binding?.swipeToRefresh?.apply {
            setOnRefreshListener {
                isRefreshing = false
                requestProductList()
            }
        }

        //setup recycle view
        _binding?.rvProduct?.apply {
            productAdapter = ProductAdapter()
            setHasFixedSize(true)

            adapter = productAdapter
        }

        viewLifecycleOwner?.lifecycleScope?.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel?.productListViewState?.collect { viewState->
                    when (viewState) {
                        is ViewState.Success ->{
                            showLoading(false)
                            productAdapter.submitList(viewState.data)
                        }
                        is ViewState.Failure ->{
                            showLoading(false)
                            displayRetryDialog(viewState.errorAny)
                        }
                        is ViewState.Loading->{
                            showLoading(true)

                        }
                    }
                }
            }
        }

        requestProductList()
    }


    override fun doRetry() {
        requestProductList()
    }

    fun requestProductList(){
        viewLifecycleOwner?.lifecycleScope?.launch {
            viewModel?.productListIntent?.send(ProductListIntent.GetProductList)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}