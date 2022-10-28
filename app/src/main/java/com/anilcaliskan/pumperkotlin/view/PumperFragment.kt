package com.anilcaliskan.pumperkotlin.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anilcaliskan.pumperkotlin.CoinAPI
import com.anilcaliskan.pumperkotlin.R
import com.anilcaliskan.pumperkotlin.adapter.CoinAdapter
import com.anilcaliskan.pumperkotlin.model.CoinModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PumperFragment : Fragment() {
    private lateinit var coinModels: ArrayList<CoinModel>
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var recyclerView: RecyclerView
    private val BASE_URL = "https://api.binance.com/"
    var compositeDisposible: CompositeDisposable? = null


    lateinit var coin: Array<CoinModel>
    lateinit var percentage: Array<Double>
    lateinit var ref: Array<Double>
    lateinit var current: Array<Double>
    lateinit var news: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pumper, container, false)
        //dataInitialize()
        compositeDisposible = CompositeDisposable()
        recyclerView = view.findViewById(R.id.recyclerviewPumperFragment)
        val layoutManagerr : RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManagerr
        recyclerView.setHasFixedSize(true)
        coinModels = arrayListOf<CoinModel>()
        coinAdapter = CoinAdapter(coinModels)
        recyclerView.adapter = coinAdapter
        val shouldStopLoop=false
        val mHandler=Handler()

        val runnable: Runnable=object: Runnable {
            override fun run() {
                loadData()

                if (!shouldStopLoop) {
                    mHandler.postDelayed(this,250)
                }
            }
        }
        runnable.run()



        return view

    }
    private fun dataInitialize(){
      /*  coinModels = arrayListOf<CoinModel>()
        val newsCoin = CoinModel("BTC", 1.000, 2.00, 3.00)
        coinModels.add(newsCoin)*/
    }

    private fun onListItemClick(position: Int) {
        Toast.makeText(activity, "Clicked: ${coinModels[position].coinName}", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CheckResult")
    private fun loadData(){
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CoinAPI::class.java)

        compositeDisposible?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this :: handleResponse)




        )



    }
    private fun handleResponse(coinList: List<CoinModel>){

        var i = 0
        var ii = 0
                for(coinModel: CoinModel in coinList){
                    if (coinList[i].coinName.endsWith("USDT"))
                    {
                        if (!coinModels.contains(coinModel)) {
                            val newsCoin = CoinModel(coinList[i].coinName, 1.000, coinList[i].currentPoint, 3.00)
                            coinModels.add(ii,newsCoin)
                            coinAdapter.notifyItemInserted(ii)
                            coinAdapter.notifyDataSetChanged()
                        }else {
                            coinModels[ii].coinName = coinList[i].coinName
                            coinModels[ii].currentPoint = coinList[i].currentPoint
                            coinAdapter.notifyItemChanged(ii)
                        }
                        ii++
                    }
                    i++
                }
    }



    override fun onDestroy() {
        super.onDestroy()
        compositeDisposible?.clear()
    }

    /*
    private fun loadData(){
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CoinAPI::class.java)
        val call = service.getData()
        call.enqueue(object: Callback<List<CoinModel>> {
            override fun onResponse(call: Call<List<CoinModel>>,response: Response<List<CoinModel>>
            ) {
                coinModels = arrayListOf<CoinModel>()
                if (response.isSuccessful){
                    response.body()?.let {
                              coinModels = ArrayList(it)
                        println("aaa"+it)
                        for(coinModel: CoinModel in coinModels){
                            println(coinModel.coinName)
                            println(coinModel.currentPoint)

                                coinModels.let {
                                    coinAdapter = CoinAdapter(it.filter {  coinModel -> coinModel.coinName.endsWith("USDT")  } as ArrayList<CoinModel>) { position -> onListItemClick(position) }
                                    recyclerView.adapter = coinAdapter

                                }
                        }

                    }
                }

            }

            override fun onFailure(call: Call<List<CoinModel>>,t: Throwable) {
                t.printStackTrace()
            }

        })



    }

     */
}