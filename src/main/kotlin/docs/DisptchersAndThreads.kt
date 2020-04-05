package docs

import kotlinx.coroutines.*

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun main() {
    runBlocking {
        val activity = Activity1()
        activity.doSomething()
        println("Launched coroutines")
        delay(500L)
        println("Destroying activity!")
        activity.destroy()
        delay(1000L)
    }
}

fun dispatchers(){
    runBlocking {
        launch {
            println("main runBlocking     : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) {
            println(" Unconfined     : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Default) {
            println(" Default     : I'm working in thread ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyOwnThread")) {
            println(" newSingleThreadContext     : I'm working in thread ${Thread.currentThread().name}")
        }
    }
}

fun unconfined(){

    runBlocking {
        launch(Dispatchers.Unconfined) {
            println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
            delay(500)
            println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
        }

        launch {
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
            delay(1000)
            println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
        }
    }

}

fun debuggingCoroutine() {

    runBlocking {
        val a = async {
            log("I'm computing a piece of the answer")
            6
        }

        val b = async {
            log("I'm computing another piece of the answer")
            7
        }

        log("The answer is ${a.await()   *    b. await()}")
    }

}

fun jumpBetweenThread(){
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use {ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2){
                    log("Working in ctx2")
                }
                log("back to ctx1")
            }
        }
    }
}

fun jobInTheContext(){
    runBlocking {
        println("My job is ${coroutineContext[Job]}")
    }
}

fun childrenOfCoroutine(){
    runBlocking {
        val request = launch {
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation of the request")
            }

            launch {
                delay(100)
                println("job2: I am a child of the request coroutine")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled")
            }
        }

        delay(500)
        request.cancel()
        delay(1000)
        println("main: Who has survived request cancellation?")
    }
}

fun namingCoroutineForDebugging(){

    runBlocking(CoroutineName("main")) {
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            log(" Computing v1")
            252
        }

        val v2 = async(CoroutineName("v2coroutine")) {
            delay(1000)
            log(" Computing v2 ")
            6
        }
        log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
    }

}

class Activity {
    private val mainScope = MainScope()

    fun destroy(){
        mainScope.cancel()
    }

    fun doSomething() {
//        mainScope.launch {  }
//        mainScope.launch(Dispatchers.Main) {
//            repeat(10) { i ->
//                launch {
//                    delay( (i + 1) * 200L)
//                    println("Coroutine $i is done ")
//                }
//            }
//        }
    }
}

class Activity1 : CoroutineScope by CoroutineScope(Dispatchers.Default){

    fun destroy(){
        cancel()
    }

    fun doSomething() {
        repeat(10) { i ->
            launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, ... etc
                println("Coroutine $i is done")
            }
        }
    }
}

