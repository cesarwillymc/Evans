package com.evans.technologies.usuario.model.modelTrip

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class trip (
    var data:HistorialAll,
    var datos:List<Historial>
):Parcelable