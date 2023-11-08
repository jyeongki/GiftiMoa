package com.example.giftimoa.dto

import android.net.Uri
import com.google.gson.annotations.SerializedName

import java.io.Serializable

data class Collect_Gift(
    var giftName: String,
    var effectiveDate: String,
    var barcode: String,
    var usage: String
) : Serializable