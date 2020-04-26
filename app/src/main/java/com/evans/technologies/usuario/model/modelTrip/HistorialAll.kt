package com.evans.technologies.usuario.model.modelTrip

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistorialAll (
    var driver:String,
    var brandCar:String,
    var modelCar:String,
    var licenseCar:String,
    var travelRateDiscount:String,
    var travelRate:String,
    var latitudeOrigen:Double=15.00,
    var longitudeOrigen:Double=15.00,
    var latitudeDestino:Double=15.00,
    var longitudeDestino:Double=15.00,
    var startAddress:String,
    var destinationAddress:String,
    var dateTrip:String,
    var tripCancell:Boolean,
    var tripAccepted:Boolean,
    var tripInitiated:Boolean,
    var tripFinalized:Boolean,
    var ruta:ArrayList<LatLng>?=null
):Parcelable