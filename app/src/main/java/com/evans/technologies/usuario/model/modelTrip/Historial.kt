package com.evans.technologies.usuario.model.modelTrip

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Historial (
    var id:String,
    var startAddress:String,
    var destinationAddress:String,
    var dateTrip:String,
    var tripCancell:Boolean,
    var tripAccepted:Boolean,
    var tripInitiated:Boolean,
    var tripFinalized:Boolean
):Parcelable