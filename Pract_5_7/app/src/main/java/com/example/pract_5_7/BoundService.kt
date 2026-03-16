package com.example.pract_5_7

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class BoundService : Service() {

    private val mBinder = MyBinder()

    private var randomNum = Random.nextInt()
    private val scope = CoroutineScope(Dispatchers.IO)

    inner class MyBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }

    fun setRandomNum(){
        scope.launch {
            while (true){
                delay(2000L)
                randomNum = Random.nextInt(100)
                CountState.time.value = randomNum
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(this, "Service binded", Toast.LENGTH_LONG).show()
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        setRandomNum()
        Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show()
    }

    override fun onRebind(intent: Intent) {
        Toast.makeText(this, "Service rebinded", Toast.LENGTH_LONG).show()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Toast.makeText(this, "Service Unbind", Toast.LENGTH_LONG).show()
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        scope.cancel()
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }

}