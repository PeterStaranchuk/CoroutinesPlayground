package com.peterstaranchuk.coroutinesplaygraung

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            launch {
                try {
                    throw Exception("MAIN Ecxeption")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val someValue = GlobalScope.async(Dispatchers.IO) {
                throw Exception("IO Ecxeption")
            }

            try {
                launch {
                    try {
                        throw Exception("ONE MORE EXCEPTION")
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
                someValue.await()
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

    private suspend fun fetchUserInfo(): UserInfo {
        throw IllegalStateException("User info cannot be fetched")
        return UserInfo("Peter", 28)
    }

    data class UserInfo(val name: String, val age: Int)
}
