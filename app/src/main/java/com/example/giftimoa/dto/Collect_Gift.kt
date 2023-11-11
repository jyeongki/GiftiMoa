package com.example.giftimoa.dto
import java.io.Serializable

class Collect_Gift(
    val giftName: String,
    val effectiveDate: String,
    val barcode: String,
    val usage: String,
    val imageUrl: String,
    var state: Int
) : Serializable

data class Badge(
    val content: String,
    val color: String
)