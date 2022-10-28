package com.anilcaliskan.pumperkotlin.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anilcaliskan.pumperkotlin.R


class RemoveCoinFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var list: ArrayList<String>
    lateinit var adapter: ArrayAdapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_remove_coin,container,false)
        list = ArrayList()
        list.add("BTTC")
        list.add("ETH")
        list.add("BNB")
        list.add("BUSD")
        val activity: Activity?=activity
        listView = view.findViewById(R.id.listView)
        searchView = view.findViewById(R.id.searchView)

        adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter

        searchView.setOnQueryTextListener(
            object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (list.contains(query)) {
                        adapter.filter.filter(query)

                    } else {
                        val activity: Activity?=activity
                        Toast.makeText(activity, "No Match found", Toast.LENGTH_LONG).show()
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter.filter(newText)
                    return false
                }
            })
        return view

        }





}