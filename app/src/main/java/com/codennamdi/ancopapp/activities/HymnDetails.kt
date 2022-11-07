package com.codennamdi.ancopapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityHymnDetailsBinding
import com.codennamdi.ancopapp.models.Hymn
import com.codennamdi.ancopapp.utils.Constants

class HymnDetails : AppCompatActivity() {
    private lateinit var hymnItem: Hymn
    private lateinit var binding: ActivityHymnDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHymnDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.HYMN_DETAILS)) {
            hymnItem =
                intent.getParcelableExtra(Constants.HYMN_DETAILS)!!
        }

        title = "Ancient & Modern"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        setUpHymnVerses()
    }

    private fun setUpHymnVerses() {
        binding.textViewTitleNum.text = hymnItem.num.toString()
        binding.textViewTitle.text = hymnItem.title
        Log.e("Hymn verse size", "${hymnItem.verses.size}")
        Log.e("Verse", "${hymnItem.verses}")
        for (item in 0 until hymnItem.verses.size) {
            Log.i("value $item", hymnItem.verses[item])
            try {
                when (hymnItem.verses.size) {
                    (1) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                    }
                    (2) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                    }
                    (3) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                    }
                    (4) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                    }
                    (5) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                        binding.textViewHymnVerse5.text = hymnItem.verses[4]
                    }
                    (6) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                        binding.textViewHymnVerse5.text = hymnItem.verses[4]
                        binding.textViewHymnVerse6.text = hymnItem.verses[5]
                    }
                    (7) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                        binding.textViewHymnVerse5.text = hymnItem.verses[4]
                        binding.textViewHymnVerse6.text = hymnItem.verses[5]
                        binding.textViewHymnVerse7.text = hymnItem.verses[6]
                    }
                    (8) -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                        binding.textViewHymnVerse5.text = hymnItem.verses[4]
                        binding.textViewHymnVerse6.text = hymnItem.verses[5]
                        binding.textViewHymnVerse7.text = hymnItem.verses[6]
                        binding.textViewHymnVerse8.text = hymnItem.verses[7]
                    }
                    else -> {
                        binding.textViewHymnVerse1.text = hymnItem.verses[0]
                        binding.textViewHymnVerse2.text = hymnItem.verses[1]
                        binding.textViewHymnVerse3.text = hymnItem.verses[2]
                        binding.textViewHymnVerse4.text = hymnItem.verses[3]
                        binding.textViewHymnVerse5.text = hymnItem.verses[4]
                        binding.textViewHymnVerse6.text = hymnItem.verses[5]
                        binding.textViewHymnVerse7.text = hymnItem.verses[6]
                        binding.textViewHymnVerse8.text = hymnItem.verses[7]
                    }
                }
            } catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
            }
        }
    }
}