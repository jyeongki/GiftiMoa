package com.example.giftimoa

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.giftimoa.databinding.LayoutCollectGiftAddBinding
import com.example.giftimoa.dto.Collect_Gift
import java.io.InputStream

class Collect_gift_add_activity : AppCompatActivity() {
    private lateinit var binding : LayoutCollectGiftAddBinding
/*    private var giftId = 0
    private var imgId = 0*/
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

    //
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

    override fun onSupportNavigateUp(): Boolean { // 액션바 뒤로가기
        onBackPressed()
        return true
    }


    private fun giftAdd() {
        binding.addBtn.setOnClickListener {
            val giftName = binding.textGiftName.text.toString()
            val effectiveDate = binding.textEffectiveDate.text.toString()
            val barcode = binding.textBarcode.text.toString()
            val usage = binding.textUsage.text.toString()

        }


    }

    
}