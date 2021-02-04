package com.project.kosku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment (
    var nama:String? ="",
    var nominal: String? ="",
    var detail: String? =""
) : Parcelable