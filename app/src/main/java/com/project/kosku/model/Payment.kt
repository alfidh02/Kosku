package com.project.kosku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment (
    var nominal: String? ="",
    var detail: String? =""
) : Parcelable