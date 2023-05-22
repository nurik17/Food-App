package com.example.foodapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewModel.MealViewModel

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var viewModel: MealViewModel
    private lateinit var mealId : String
    private lateinit var mealName : String
    private lateinit var mealThumb : String
    private lateinit var youtubeLink : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MealViewModel::class.java]

        getMealInformation()
        setInformation()

        loadingCase()
        viewModel.getMealDetail(mealId)
        observerMealDetailsLiveData()
        youtubeLink()
    }

    private fun youtubeLink() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun observerMealDetailsLiveData() {
        viewModel.observeMealDetailsLiveData().observe(this
        ) { value ->
            binding.tvCategory.text = value.strCategory
            binding.tvLocation.text = value.strArea
            binding.tvInstructions.text = value.strInstructions

            youtubeLink = value.strYoutube
            onResponseCase()
        }
    }

    private fun setInformation() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

    }

    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAl_THUMB)!!
    }

    private fun loadingCase(){
        binding.apply {
            btnFavourite.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            tvLocation.visibility = View.INVISIBLE
            tvInstructions.visibility = View.INVISIBLE
            imgYoutube.visibility = View.INVISIBLE
            progressbar.visibility = View.VISIBLE
        }

    }
    private fun onResponseCase(){
        binding.apply {
            btnFavourite.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvLocation.visibility = View.VISIBLE
            tvInstructions.visibility = View.VISIBLE
            imgYoutube.visibility = View.VISIBLE
            progressbar.visibility = View.INVISIBLE
        }
    }
}