package com.project.kosku.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tenant (
    var name: String? ="",
    var noHp: String? ="",
    var tgglMasuk: String? ="",
    var status: Boolean? = false
) : Parcelable