package pe.com.master.machines.myapplication.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import pe.com.master.machines.myapplication.data.entity.Money
import pe.com.master.machines.myapplication.databinding.ItemListMoneyBinding
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class ListMoneyAdapter(val context: Context) :
    RecyclerView.Adapter<ListMoneyAdapter.HolderBody>() {

    companion object {
        private val TAG = ListMoneyAdapter::class.java.simpleName
    }

    private var mDataList: ArrayList<Money> = ArrayList()
    private lateinit var mListener: OnItemClickListener
    private var format = DecimalFormat("###,###,###.00")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBody {
        val binding =
            ItemListMoneyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HolderBody(binding)
    }

    override fun onBindViewHolder(holderBody: HolderBody, position: Int) {
        val item: Money = mDataList[position]
        holderBody.bind(item, position)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setOnListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun setData(dataList: List<Money>) {
        if (mDataList == null)
            mDataList = ArrayList()
        mDataList.clear()
        mDataList.addAll(dataList)
        notifyDataSetChanged()
    }

    inner class HolderBody(
        binding: ItemListMoneyBinding
    ) :
        RecyclerView.ViewHolder(binding.getRoot()), View.OnClickListener {
        val mBinding: ItemListMoneyBinding = binding
        var pos: Int = 0
        lateinit var money: Money
        fun bind(
            item: Money,
            position: Int
        ) {
            money = item
            pos = position

            mBinding.txtCountryMoney.setText(money.country)
            mBinding.txtConversion.setText("1 PEN = ${money.buy}  ${money.codMoney}")

            Glide.with(context)
                .load(money.flag)
                .fitCenter()
                .into(mBinding.logoMoney)

            Log.d(
                TAG, String.format(
                    Locale.getDefault(),
                    "HolderBody %d (%s) is complete! ${money.flag}", position, money.nameMoney
                )
            )
        }

        init {
            mBinding.containerItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                mBinding.containerItem.id -> {
                    if (mListener != null) {
                        mListener.onItemClick(view, money, pos, false)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    interface OnItemClickListener {

        fun onItemClick(view: View, item: Money, position: Int, longPress: Boolean): Boolean

    }
}