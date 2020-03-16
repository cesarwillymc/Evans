package com.evans.technologies.usuario.Utils.firebase

import android.content.Intent
import android.util.Log
import com.evans.technologies.usuario.Utils.Services.NotificationReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

       // sendNotification(remoteMessage)
        remoteMessage.data?.let {
            val intent = Intent("clase")

            intent.putExtra("data", remoteMessage.data.toString())
            val brod: NotificationReceiver =NotificationReceiver()
            brod.onReceive(applicationContext,intent)

            //val  broadcaster: LocalBroadcastManager = LocalBroadcastManager.getInstance(this)
           // broadcaster.sendBroadcast(intent)
        }
       /* if(!remoteMessage.data.isEmpty()){


        }
*/
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ")

        }

    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */



    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */


    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}