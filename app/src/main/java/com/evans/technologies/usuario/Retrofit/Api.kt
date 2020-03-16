@file:Suppress("unused")

package com.evans.technologies.usuario.Retrofit

import com.evans.technologies.usuario.model.*
import com.evans.technologies.usuario.model.ResponsesApi.LoginResponse
import com.evans.technologies.usuario.model.ResponsesApi.RegisterResponse
import com.evans.technologies.usuario.model.user
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("user/forgot")
    fun sendCorreo_recuperar(
        @Field("email") email: String
    ): Call<user>
    @FormUrlEncoded
    @POST("user/reset/confirm")
    fun sendCodigo_recuperar(
        @Field("id") id:String,
        @Field("token") token: String
    ): Call<user>
    @FormUrlEncoded
    @POST("user/validarEmail")
    fun enviarCorreo_validate(
        @Field("email") email:String
    ): Call<user>
    @FormUrlEncoded
    @POST("user/validate/confirm")
    fun validarCode_validate(
        @Field("id") id:String,
        @Field("token") token:String
    ): Call<LoginResponse>
    @FormUrlEncoded
    @POST("user/reset/change/{id}")
    fun sendContraseña_recuperar(
        @Path("id") id:String,
        @Field("token") token: String,
        @Field("password") password: String
    ): Call<user>

    ///
    @FormUrlEncoded
    @POST("user/signup")
    fun createUser(
            @Field("name") name: String,
            @Field("surname") surname: String,
            @Field("numDocument") dni: String,
            @Field("email") email: String,
            @Field("cellphone") celphone: String,
            @Field("city") city: String,
            @Field("password") password: String,
            @Field("passwordconfirm") passwordconfirm: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("user/signin")
    fun loginUser(
            @Field("email") email:String,
            @Field("password") password:String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("repository/getPrice")
    fun travel(
            @Header("authorization") token:String,
            @Field("originLatitude") originLatitude:String,
            @Field("originLongitude") originLongitude:String,
            @Field("destinationLatitude") destinationLatitude:String,
            @Field("destinationLongitude") destinationLongitude:String
    ): Call<getPrice>


    @GET
    fun getPath(
            @Url  url:String
    ):Call<String>

    @FormUrlEncoded
    @PUT("user/updateValues/{id}/")
    fun tokenFCM(
            @Header("authorization") token:String,
            @Path("id") id:String,
            @Field("accessToken") accessToken:String,
            @Field("fcmToken") fcmtoken:String
    ):Call<user>

    @GET("user/getInformation/{id}/")
    fun getUserInfo(
            @Header("authorization") token:String,
            @Path("id") id:String

    ):Call<LoginResponse>
    @GET("getStatusTrip/{driverId} ")
    fun getStatusTrip(
            @Path("driverId") driverId:String
    ): Call<infoDriver>
    @FormUrlEncoded
    @POST("user/requestDriver/{id}/")
    fun requestDriver(
            @Path("id") id:String,
            @Field("latitudeOrigen") latitudeOrigen:String,
            @Field("longitudeOrigen") longitudeOrigen:String,
            @Field("latitudeDestino") latitudeDestino:String,
            @Field("longitudeDestino") longitudeDestino:String,
            @Field("startAddress") startAddress:String,
            @Field("destinationAddress") destinationAddress:String,
            @Field("dateTrip") dateTrip:String,
            @Field("travelRate") travelRate:String,
            @Field("travelRateDiscount") travelRateDiscount:String,
            @Field("city") city:String
    ): Call<user>
    @FormUrlEncoded
    @PUT("Trip/UpdatePaymentAmount/{TripId} ")
    fun changePriceTrip(
        @Path("TripId") TripId:String,
        @Field("travelRate") travelRate:String,
        @Field("travelRateDiscount") travelRateDiscount:String
    ): Call<user>

    @FormUrlEncoded
    @POST("user/sendNotification/{userId} ")
    fun userTOdriver(
            @Path("userId") userId:String,
            @Field("driverId") driverId:String,
            @Field("price") price:String,
            @Field("pricediscount") pricediscount:String,
            @Field("response") response:String
    ): Call<user>
    @GET("notifications/getInformationDriver/{driverId} ")
    fun getInfoDriver(
            @Path("driverId") driverId:String
    ): Call<infoDriver>
    @FormUrlEncoded
    @PUT("statusTrip/{tripId} ")
    fun puStatusTrip(
            @Path("tripId") Driverid:String,
            @Field("tripCancell") tripCancell:Boolean,
            @Field("tripAccepted") tripAccepted:Boolean,
            @Field("tripInitiated") tripInitiated:Boolean,
            @Field("tripFinalized") tripFinalized:Boolean

    ): Call<user>
    @Multipart
    @PUT("upload/img/user/{userId}")
    fun guardarImagenes(
            @Path("userId")userId:String,
            @Part profile: MultipartBody.Part,
            @Part("profile") name: RequestBody
    ): Call<user>
    @GET("driver/getcoodinates/{driverId}")
    fun updateCoordenadasDriver(
            @Path("driverId")driverId:String
    ): Call<infoDriver>
    @GET("v/user")
    fun optenerVersionAndroid(): Call<infoDriver>
    @FormUrlEncoded
    @POST("user/valorarviaje/{driverId}")
    fun calificarDriver(
        @Path("driverId") driverid:String,
        @Field("mensaje") mensaje:String,
        @Field("rating") rating:String,
        @Field("userId") userId:String,
        @Field("nombre") nombre:String
    ): Call<getPrice>
    @GET("v/user")
    fun suggestion_user_direction(): Call<ArrayList<sugerenciasLocale>>
    @FormUrlEncoded
    @POST("repository/getCalcNewPriceWithCupon/{userId}")
    fun getPriceDiscount(
        @Path("userId") userId:String,
        @Field("Price") precioOriginal:String
    ): Call<getPrice>
    @FormUrlEncoded
    @POST("other/userGetCoupon")
    fun setCuponUser(
        @Field("userId") userId:String,
        @Field("key") key:String
    ): Call<getPrice>

    @GET("user/listcouponsuser/{userId}")
    fun getListCoupon(
        @Path("userId") userId:String
    ): Call<data>

}