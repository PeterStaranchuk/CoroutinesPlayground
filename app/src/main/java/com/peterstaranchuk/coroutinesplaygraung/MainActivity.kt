package com.peterstaranchuk.coroutinesplaygraung

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            try {
                login()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        val userInfo = async {
            fetchUserInfo()
        }

        launch {
            try {
                userInfo.await()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        GlobalScope.launch(handler) {
            try {
                login()
                userInfo.await()
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun login() {
        delay(2000)
        Log.d("TAG", "User logged")
        throw IllegalStateException("User not exist")
    }

    private suspend fun fetchUserInfo() : UserInfo {
        throw IllegalStateException("User info cannot be fetched")
        return UserInfo("Peter", 28)
    }

    data class UserInfo(val name : String, val age : Int)
}
