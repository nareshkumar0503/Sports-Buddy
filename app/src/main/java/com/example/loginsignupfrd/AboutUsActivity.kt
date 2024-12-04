package com.example.loginsignupfrd


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignupfrd.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the ActionBar if needed
        supportActionBar?.title = "About Us"
    }


}
