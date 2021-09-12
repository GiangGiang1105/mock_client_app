package com.example.clientapp.view.auth

import com.example.clientapp.base.BaseActivity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import com.example.clientapp.R
import com.example.clientapp.databinding.ActivityAuthBinding
import com.example.clientapp.model.localsource.DataStoreManager
import com.example.clientapp.utils.LoadingDialog
import com.example.clientapp.view.main.MainActivity
import com.example.clientapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val mViewModel: AuthViewModel by viewModels()
    private val dialog: LoadingDialog by lazy { LoadingDialog(this) }

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun getActivityBinding(layoutInflater: LayoutInflater) =
        ActivityAuthBinding.inflate(layoutInflater)

    override fun getNavHostFragment() =
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

    override fun handleTask() {
        checkTokenUser()

        initListener()
        initObserve()
    }

    private fun checkTokenUser() {
        dataStoreManager.accessToken.asLiveData().observe(this, {
            if (it != null) {
                MainActivity.start(this)
            }
        })
    }

    private fun initObserve() {
        mViewModel.eventLoading.observe(this, { eventLoading ->
            if (eventLoading.getContentIfNotHandled() == true) {
                dialog.startLoading()
            } else {
                dialog.dismissDialog()
            }
        })

        mViewModel.eventError.observe(this, { eventError ->
            Toast.makeText(this, eventError.getContentIfNotHandled(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun initListener() {
        lifecycle.addObserver(mViewModel)
    }
}