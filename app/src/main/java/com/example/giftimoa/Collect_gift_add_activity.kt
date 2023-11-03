package com.example.giftimoa

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.giftimoa.databinding.LayoutCollectGiftAddBinding
import com.example.giftimoa.dto.Collect_Gift

class Collect_gift_add_activity : AppCompatActivity() {
    private lateinit var binding : LayoutCollectGiftAddBinding
    private var giftId = 0
    private var imgId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutCollectGiftAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //액션바 활성화
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "기프티콘 등록"

        galleryClickEvent()
    }


    private fun galleryClickEvent(){
        binding.uploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }
    }

    //갤러리 호출
    private val activityResult:ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if(it.resultCode == RESULT_OK && it.data != null){
            val uri = it.data!!.data

            Glide.with(this)
                .load(uri)
                .into(binding.uploadImage)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    
    private fun giftAdd(){ //기프티콘 등록
        val data = Collect_Gift(
            binding.textGiftName.text.toString(),
            binding.textEffectiveDate.text.toString(),
            binding.textBarcode.text.toString(),
            binding.textUsage.text.toString(),
            true
        )
    }
    
}