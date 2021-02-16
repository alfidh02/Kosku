package com.project.kosku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet (
    var id:String? ="",
    var date:String? ="",
    var year:String? ="",
    var month:String? ="",
    var nominal: String? ="",
    var detail: String? ="",
    var tipe: Boolean? = false
) : Parcelable