package docs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*

fun main() {
    coroutineScope()
}


/**
 *
 *      main function is turned into coroutine by using runblocking
 */

//fun main() = runBlocking {
//    GlobalScope.launch {
//        delay(1000L)
//        println("World")
//    }
//
//    println("Hello")
//    delay(2000L)
//}

fun bridging(){
    GlobalScope.launch {
        delay(1000L)
        println("World!!")
    }

    println("Hello")

    runBlocking {
        delay(2000L)
    }

    println("Gong")

//    Thread.currentThread().interrupt()
}

fun waitingJob() {
    runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            println("World")
        }
        println("Hello")
        job.join()
    }
}

fun runBlockingLaunch(){
    runBlocking {
        launch {
            delay(1000L)
            println("World")
        }
        println("Hello")
    }
}

fun coroutineScope(){
    runBlocking {
        launch {
            delay(200L)
            println("Task From runBlocking")
        }

        coroutineScope {
            launch {
                delay(500L)
                println("Task From launch")
            }

            delay(100L)
            println("Task from coroutine scope" +
                    "")
        }
        println("Coroutine scope is over")
    }
}

suspend fun doWorld(){
    delay(1000L)
    println("World")
}