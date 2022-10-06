package br.com.douglas.esports

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val id: Int,
    val name: String,
    val players: List<Player> = listOf()
):Parcelable



