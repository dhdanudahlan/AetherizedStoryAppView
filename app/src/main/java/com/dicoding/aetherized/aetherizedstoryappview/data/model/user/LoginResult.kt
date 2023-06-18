package com.dicoding.aetherized.aetherizedstoryappview.data.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResult(
    val userId: String = "GUEST",
    val name: String = "GUEST",
    val token: String = "GUEST"
) : Parcelable
