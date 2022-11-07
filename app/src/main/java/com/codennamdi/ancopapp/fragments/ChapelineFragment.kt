package com.codennamdi.ancopapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.FragmentChapelineBinding

class ChapelineFragment : Fragment() {
    private lateinit var binding: FragmentChapelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChapelineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewChapelineNameId.text = getString(R.string.chapeline_name)
        binding.chapelineBioDetails.text = getString(R.string.chapeline_details)
        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.floatingButtonMessageChapelineId.setOnClickListener {
            composeSmsMessageIntent("Good day sir! ", "+2348034430546")
        }

        binding.floatingButtonCallChapelineId.setOnClickListener {
            dialPhoneNumberIntent("+2348034430546")
        }
    }

    private fun dialPhoneNumberIntent(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    private fun composeSmsMessageIntent(message: String, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message)
        }
        startActivity(intent)
    }
}