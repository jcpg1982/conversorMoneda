package pe.com.master.machines.myapplication.ui.fragments.conversorFragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal
import pe.com.master.machines.myapplication.data.result.ResultData
import pe.com.master.machines.myapplication.data.viewModel.ViewModelFactory
import pe.com.master.machines.myapplication.data.viewModel.ViewModelLive
import pe.com.master.machines.myapplication.databinding.FragmentConversorBinding
import pe.com.master.machines.myapplication.helpers.Constants
import pe.com.master.machines.myapplication.interfaces.TextWatcherAdapter
import pe.com.master.machines.myapplication.ui.activities.activityListMoney.ActivityListMoney
import pe.com.master.machines.myapplication.ui.fragments.baseFragment.BaseFragment
import java.text.DecimalFormat
import javax.inject.Inject

class ConversorFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var binding: FragmentConversorBinding

    @Inject
    lateinit var dataSourceLocal: DataSourceLocal

    private val viewModel: ViewModelLive by viewModels {
        ViewModelFactory(dataSourceLocal)
    }

    private lateinit var mResultSend: ActivityResultLauncher<Intent>
    private lateinit var mResultReceiver: ActivityResultLauncher<Intent>
    private lateinit var mMoneySend: Money
    private lateinit var mMoneyReceiver: Money
    private var format = DecimalFormat("###,###,###.00")

    companion object {

        val TAG = ConversorFragment::class.java.simpleName

        fun newInstance(): ConversorFragment {
            val fragment = ConversorFragment()
            var args: Bundle? = fragment.arguments
            if (args == null) {
                args = Bundle()
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().inject(this)
        initResult()
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConversorBinding.inflate(inflater, container, false)
        viewModel.getAllMoney()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.btnChange.id -> {
                changeMoney()
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        when (view.id) {
            binding.containerMoneySend.id -> {
                val intent = Intent(requireContext(), ActivityListMoney::class.java)
                mResultSend.launch(intent)
            }
            binding.containerMoneyReceiver.id -> {
                val intent = Intent(requireContext(), ActivityListMoney::class.java)
                mResultReceiver.launch(intent)
            }
        }
        return false
    }

    private fun init() {
        resultObserver()
    }

    private fun initEvents() {
        binding.btnChange.setOnClickListener(this)
        binding.containerMoneySend.setOnLongClickListener(this)
        binding.containerMoneyReceiver.setOnLongClickListener(this)

        binding.inputSend.addTextChangedListener(object : TextWatcherAdapter {
            override fun action(
                action: TextWatcherAdapter.Action,
                charOrEditable: CharSequence,
                start: Int,
                count: Int,
                afterOrBefore: Int
            ) {
                when (action) {
                    TextWatcherAdapter.Action.afterTextChanged -> {
                        if (charOrEditable.length > 0) {
                            moneyExchange(charOrEditable.toString().toDouble())
                        }
                    }
                }
            }
        })
    }

    private fun initResult() {
        mResultSend =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d(TAG, "initResult mResultSend: ${result.data}")
                mMoneySend =
                    result.data?.getParcelableExtra(Constants.Intents.INTENT_EXTRA_MONEY)!!
                Log.d(TAG, "initResult mMoneySend: ${mMoneySend}")
                setDataMoneySend(mMoneySend)
            }

        mResultReceiver =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d(TAG, "initResult mResultReceiver: ${result.data}")
                mMoneyReceiver =
                    result.data?.getParcelableExtra(Constants.Intents.INTENT_EXTRA_MONEY)!!
                Log.d(TAG, "initResult mMoneyReceiver: ${mMoneyReceiver}")
                setDataMoneyReceiver(mMoneyReceiver)
            }
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
                        setDataMoneySend(it.data[6])
                        setDataMoneyReceiver(it.data[1])
                    }
                }
            }
        }
    }

    private fun setDataMoneySend(money: Money) {
        mMoneySend = money
        binding.txtMoneySend.post {
            binding.txtMoneySend.setText(mMoneySend.nameMoney)
        }
        binding.txtPriceCompra.post {
            binding.txtPriceCompra.setText("Compra: ${obtainbuy()}")
        }
        binding.txtPriceVenta.post {
            binding.txtPriceVenta.setText("Compra: ${obtainSell()}")
        }
        if (!TextUtils.isEmpty(binding.inputSend.text.toString()))
            moneyExchange(binding.inputSend.text.toString().toDouble())
    }

    private fun setDataMoneyReceiver(money: Money) {
        mMoneyReceiver = money
        binding.txtMoneyReceived.post {
            binding.txtMoneyReceived.setText(mMoneyReceiver.nameMoney)
        }
        binding.txtPriceCompra.post {
            binding.txtPriceCompra.setText("Compra: ${obtainbuy()}")
        }
        binding.txtPriceVenta.post {
            binding.txtPriceVenta.setText("Compra: ${obtainSell()}")
        }
        if (!TextUtils.isEmpty(binding.inputSend.text.toString()))
            moneyExchange(binding.inputSend.text.toString().toDouble())
    }

    private fun changeMoney() {
        val temp = mMoneySend
        mMoneySend = mMoneyReceiver
        mMoneyReceiver = temp
        setDataMoneySend(mMoneySend)
        setDataMoneyReceiver(mMoneyReceiver)
        if (!TextUtils.isEmpty(binding.inputSend.text.toString()))
            moneyExchange(binding.inputSend.text.toString().toDouble())
        binding.txtPriceCompra.post {
            binding.txtPriceCompra.setText("Compra: ${obtainbuy()}")
        }
        binding.txtPriceVenta.post {
            binding.txtPriceVenta.setText("Compra: ${obtainSell()}")
        }
    }

    private fun moneyExchange(moneySend: Double) {
        val soles = obtainSoles(moneySend)
        val result = obtainresult(soles)
        binding.inputReceiver.post {
            binding.inputReceiver.setText("${result}")
        }
    }

    fun obtainSoles(moneySend: Double): Double {
        val multi = 1 * moneySend
        val result = multi / mMoneySend.buy
        return format.format(result).toDouble()
    }

    fun obtainresult(moneySend: Double): Double {
        val multi = moneySend * mMoneyReceiver.buy
        val result = multi / 1
        return format.format(result).toDouble()
    }

    fun obtainbuy(): Double {
        val soles = obtainSoles(1.0) * 1
        val result = soles / mMoneyReceiver.buy
        return format.format(result).toDouble()
    }

    fun obtainSell(): Double {
        val soles = obtainSoles(1.0) * 1
        val result = soles / mMoneyReceiver.sell
        return format.format(result).toDouble()
    }
}