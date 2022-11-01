package com.codennamdi.ancopapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.FragmentHymnBinding
import com.codennamdi.ancopapp.models.HymnDataModel
import com.google.gson.Gson
import java.io.IOException

class HymnFragment : Fragment() {
    private lateinit var binding: FragmentHymnBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hymn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val jsonFileString = getJsonDataFromAsset(requireContext(), "a&m_hymns_asset.json")
            Log.i("data", jsonFileString!!)

            val jsonFileStringResponse = Gson().fromJson(jsonFileString, HymnDataModel::class.java)


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

//    private fun setUpRecyclerView(hymnsModelItem: HymnsModelItem) {
//        val hymnsTitleAdapter =
//            activity?.let { ItemHymnsTitleAdapter(, it) }
//        binding.hymnsRecyclerViewId.layoutManager = LinearLayoutManager(activity)
//        binding.hymnsRecyclerViewId.adapter = hymnsTitleAdapter
//    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}