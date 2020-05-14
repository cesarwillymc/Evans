package com.evans.technologies.usuario.model.modelTrip

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class trip (
    var data:HistorialAll,
    var datos:List<Historial>,
    var ewallet:Wallet
):Parcelable
@Parcelize
data class Wallet(
    var value:String
):Parcelable