package com.example.vitalii.yellowsjoblog


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.vitalii.yellowsjoblog.adapters.ClientsAdapter
import com.example.vitalii.yellowsjoblog.api.ClientsPOKO
import com.example.vitalii.yellowsjoblog.api.JobLogService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ClientsFragment : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private var client:MutableList<ClientsPOKO> = ArrayList()
    private val adapter = ClientsAdapter(client)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clients, container, false)

        mRecyclerView = view!!.findViewById(R.id.clientsRecycler)
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        serverConnect()
        return view
    }

    private var clientsObject:Callback<List<ClientsPOKO>>? = null
    private var clients:Call<List<ClientsPOKO>>? = null
    private var responseSaveClients:List<ClientsPOKO>? = null
    private lateinit var retrofit:Retrofit
    private val BASE_URL_DEV = "http://dev.joblog.yellows.pl/"
    private lateinit var service:JobLogService

    private fun serverConnect(){
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        clientsObject = object : Callback<List<ClientsPOKO>> {
            override fun onResponse(call: Call<List<ClientsPOKO>>, response: Response<List<ClientsPOKO>>) {
                if(response.body()!=null) {
                    responseSaveClients = response.body()!!
                    adapter.updateRecycler(response.body()!!.toMutableList())
                };else{
                    Toast.makeText(activity!!,"Server answer is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ClientsPOKO>>, t: Throwable) {
                Toast.makeText(activity!!, "Server doesn't responding!", Toast.LENGTH_SHORT).show()
            }
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_DEV)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        service = retrofit?.create(JobLogService::class.java)
        if(responseSaveClients==null){
            clients = service?.getClients()
            clients?.enqueue(clientsObject)
        }
    }

}
