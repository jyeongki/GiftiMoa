package com.example.giftimoa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.giftimoa.databinding.LayoutSignUpEmailBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class SignUpemail_activity : AppCompatActivity() {

    private lateinit var binding: LayoutSignUpEmailBinding
    private lateinit var email: String // 사용자 이메일 주소

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutSignUpEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.email_double_check.setOnClickListener {
            email = binding.emailEditText.text.toString()

            if (email.isNotEmpty()) {
                // 이메일 중복 확인 요청
                checkEmailDuplicate(email)
            } else {
                Toast.makeText(this, "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextViewBtn.setOnClickListener {
            val enteredVerificationCode = binding.certificationNum.text.toString()

            if (enteredVerificationCode.isNotEmpty()) {
                // 인증 코드 확인 요청
                verifyVerificationCode(email, enteredVerificationCode)
            } else {
                Toast.makeText(this, "인증 코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkEmailDuplicate(email: String) {
        val client = OkHttpClient()
        val url = "http://3.35.110.246:3306/checkDuplicateEmail" // 서버의 중복 확인 엔드포인트
        val json = """{"email": "$email"}"""
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpemail_activity, "중복 확인 요청 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                val jsonResponse = JSONObject(responseText)

                if (response.isSuccessful) {
                    val message = jsonResponse.getString("message")
                    runOnUiThread {
                        Toast.makeText(this@SignUpemail_activity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val error = jsonResponse.getString("error")
                    runOnUiThread {
                        Toast.makeText(this@SignUpemail_activity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun verifyVerificationCode(email: String, enteredCode: String) {
        val client = OkHttpClient()
        val url = "http://3.35.110.246:3306/verify_verification_code" // 서버의 인증 코드 확인 엔드포인트
        val json = """{"email": "$email", "verificationCode": "$enteredCode"}"""
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = json.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpemail_activity, "인증 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                val jsonResponse = JSONObject(responseText)

                if (response.isSuccessful) {
                    val message = jsonResponse.getString("message")
                    runOnUiThread {
                        Toast.makeText(this@SignUpemail_activity, message, Toast.LENGTH_SHORT).show()
                        // 인증 성공 시, 다음 단계로 이동 (예: SignUpActivity)
                        val intent = Intent(this@SignUpemail_activity, SignUp_activity::class.java)
                        intent.putExtra("emailtext", email)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val error = jsonResponse.getString("error")
                    runOnUiThread {
                        Toast.makeText(this@SignUpemail_activity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
