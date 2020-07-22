package com.example.receive_and_send_sms

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS),111)
        }
        else
            receiveMessage()
        button2.setOnClickListener{
                    var sms= SmsManager.getDefault()
                    sms.sendTextMessage(editTextPhone2.text.toString(),"ME",editTextTextMultiLine2.text.toString(),null,null)
                    editTextPhone2.text.clear()
            editTextTextMultiLine2.text.clear()
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode === 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            receiveMessage()
        }
    }
    private  fun receiveMessage(){
        var broadCast =object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
               {
                for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                   // Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()
                editTextPhone2.setText(sms.originatingAddress)
                    editTextTextMultiLine2.setText(sms.displayMessageBody)
                }
               }
            }
        }
        registerReceiver(broadCast, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}