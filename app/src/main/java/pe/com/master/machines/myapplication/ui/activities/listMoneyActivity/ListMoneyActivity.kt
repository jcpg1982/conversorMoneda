package pe.com.master.machines.myapplication.ui.activities.listMoneyActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal
import pe.com.master.machines.myapplication.data.result.ResultData
import pe.com.master.machines.myapplication.data.viewModel.ViewModelFactory
import pe.com.master.machines.myapplication.data.viewModel.ViewModelLive
import pe.com.master.machines.myapplication.databinding.ActivityListMonedasBinding
import pe.com.master.machines.myapplication.helpers.Constants
import pe.com.master.machines.myapplication.ui.activities.baseActivity.BaseActivity
import pe.com.master.machines.myapplication.ui.adapter.ListMoneyAdapter
import javax.inject.Inject

class ListMoneyActivity : BaseActivity() {

    private val TAG = ListMoneyActivity::class.java.simpleName
    private lateinit var binding: ActivityListMonedasBinding

    @Inject
    lateinit var dataSourceLocal: DataSourceLocal

    private val viewModel: ViewModelLive by viewModels {
        ViewModelFactory(dataSourceLocal)
    }

    private lateinit var mAdapter: ListMoneyAdapter

    private val onItem = object : ListMoneyAdapter.OnItemClickListener {
        override fun onItemClick(
            view: View,
            item: Money,
            position: Int,
            longPress: Boolean
        ): Boolean {
            if (!longPress) {
                val result = Intent()
                result.putExtra(Constants.Intents.INTENT_EXTRA_MONEY, item)
                setResult(RESULT_OK, result)
                finish()
            }
            return false
        }
    }

    override fun getFragManager(): FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMonedasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAppComponent().inject(this)
        setupRecyclerView()
        viewModel.getAllMoney()
    }

    override fun onResume() {
        super.onResume()
        resultObserver()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
    }

    private fun setupRecyclerView() {
        mAdapter = ListMoneyAdapter(this)
        mAdapter.setOnListener(onItem)
        binding.recycler.setAdapter(mAdapter)
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)
    }

    private fun resultObserver() {
        //guardarMoney
        lifecycleScope.launch {
            viewModel.getAllMoney.collect {
                when (it) {
                    is ResultData.Loading -> {
                        Log.d(TAG, "guardarMoney esta cargando: ${it.message}")
                    }
                    is ResultData.Error -> {
                        Log.d(TAG, "guardarMoney error: ${it.exception.message}")
                    }
                    is ResultData.Success -> {
                        Log.d(TAG, "guardarMoney finalizo: ${it.data}")
                        mAdapter.setData(it.data)
                    }
                }
            }
        }
    }
}