package com.project.kosku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet (
    var date:String? ="",
    var nominal: String? ="",
    var detail: String? ="",
    var tipe: Boolean? = false
) : Parcelable