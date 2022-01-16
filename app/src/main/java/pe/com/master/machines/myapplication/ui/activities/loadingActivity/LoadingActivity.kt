package pe.com.master.machines.myapplication.ui.activities.loadingActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.data.repository.repositoryLocal.DataSourceLocal
import pe.com.master.machines.myapplication.data.result.ResultData
import pe.com.master.machines.myapplication.data.viewModel.ViewModelFactory
import pe.com.master.machines.myapplication.data.viewModel.ViewModelLive
import pe.com.master.machines.myapplication.databinding.ActivityLoadingBinding
import pe.com.master.machines.myapplication.ui.activities.baseActivity.BaseActivity
import pe.com.master.machines.myapplication.ui.activities.mainActivity.MainActivity
import java.io.IOException
import javax.inject.Inject

class LoadingActivity : BaseActivity() {

    private lateinit var binding: ActivityLoadingBinding

    private val TAG = LoadingActivity::class.java.simpleName

    @Inject
    lateinit var dataSourceLocal: DataSourceLocal

    private val viewModel: ViewModelLive by viewModels {
        ViewModelFactory(dataSourceLocal)
    }

    override fun getFragManager(): FragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAppComponent().inject(this)
        binding.loadingBcp.playAnimation()

        readJson()
    }

    override fun onResume() {
        super.onResume()
        resultObserver()
    }

    private fun readJson() {
        try {
            val jsonArray = JSONArray(loadJSONFromAsset("json_money.json"))
            Log.d(TAG, "readJson jsonArray: " + jsonArray)
            val listMoney = mutableListOf<Money>()
            for (i in 0..(jsonArray.length() - 1)) {
                val jsonObject = jsonArray.getJSONObject(i)
                Log.d(TAG, "readJson jsonObject: " + jsonObject)
                val money = Gson().fromJson(jsonObject.toString(), Money::class.java)
                Log.d(TAG, "readJson money: " + money)
                listMoney.add(money)
            }
            viewModel.guardarMoney(listMoney)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun loadJSONFromAsset(name: String): String? {
        var json: String
        json = try {
            val `is` = assets.open(name)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    private fun moveToMainActivity() {
        Handler(Looper.getMainLooper())
            .postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
    }

    private fun resultObserver() {
        //guardarMoney
        lifecycleScope.launch {
            viewModel.guardarMoney.collect {
                when (it) {
                    is ResultData.Loading -> {
                        Log.d(TAG, "guardarMoney esta cargando: ${it.message}")
                    }
                    is ResultData.Error -> {
                        Log.d(TAG, "guardarMoney error: ${it.exception.message}")
                    }
                    is ResultData.Success -> {
                        Log.d(TAG, "guardarMoney finalizo: ${it.data}")
                        moveToMainActivity()
                    }
                }
            }
        }
    }
}