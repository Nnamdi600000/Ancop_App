package com.codennamdi.ancop_admin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codennamdi.ancop_admin.activities.AddLeaderActivity
import com.codennamdi.ancop_admin.databinding.FragmentLeadersAdminBinding

class LeadersFragmentAdmin : Fragment() {
    private lateinit var binding: FragmentLeadersAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeadersAdminBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionBtnAddLeaderId.setOnClickListener {
            startActivity(Intent(requireContext(), AddLeaderActivity::class.java))
        }
    }
}