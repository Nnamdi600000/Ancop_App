package com.codennamdi.ancopapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codennamdi.ancopapp.activities.HymnDetails
import com.codennamdi.ancopapp.adapters.ItemHymnsTitleAdapter
import com.codennamdi.ancopapp.databinding.FragmentHymnBinding
import com.codennamdi.ancopapp.models.Hymn
import com.codennamdi.ancopapp.models.HymnData
import com.codennamdi.ancopapp.utils.Constants
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

class HymnFragment : Fragment() {
    private lateinit var binding: FragmentHymnBinding
    private lateinit var hymnDataList: List<Hymn>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHymnBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val hymnJsonFromAsset = getJsonDataFromAsset(requireContext(), Constants.HYMN_JSON_FILE)
            Log.i("data", hymnJsonFromAsset!!)

            val hymnData = Json.decodeFromString<HymnData>(hymnJsonFromAsset)
            val hymnDataSize = hymnData.hymns.size

            Log.i("Total hymn size", "$hymnDataSize")
            hymnDataList = hymnData.hymns

        } catch (e: IOException) {
            e.printStackTrace()
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val hymnsTitleAdapter =
            activity?.let {
                ItemHymnsTitleAdapter(
                    hymnDataList as ArrayList<Hymn>,
                    requireActivity()
                )
            }
        binding.hymnsRecyclerViewId.layoutManager = LinearLayoutManager(activity)
        binding.hymnsRecyclerViewId.adapter = hymnsTitleAdapter

        hymnsTitleAdapter?.setOnClickListener(object :
            ItemHymnsTitleAdapter.OnClickListener {
            override fun onClick(position: Int, items: Hymn) {
                activity.let {
                    Log.e("verse", "${items.verses}")
                    val intent = Intent(it, HymnDetails::class.java)
                    intent.putExtra(Constants.HYMN_DETAILS, items)
                    // Log.e("verse", "${items.verses}")
                    startActivity(intent)
                }
            }
        })

    }

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