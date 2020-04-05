package docs

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer
import kotlinx.coroutines.*

fun main(args: Array<String>){
    timeOut()
}

fun cancellationAndTimeout() {

    runBlocking {
        val job = launch {
            repeat(1000) {
                println("job: I'm sleeping   $it  ")
                delay(500L)
            }
        }

        delay(900L)
        println("main i'm tried of waiting !!")
        job.cancel()
        println("cancle")
        job.join()
        println("main : Now I can quit")
    }

}

fun cancellationIsCooperative() {
    runBlocking {
        val startTime = System.currentTimeMillis()

        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0

            while ( i < 5 ){
                if (System.currentTimeMillis() > nextPrintTime) {
                    println(" job : I'm sleeping ${i++}")
                    nextPrintTime += 500L
                }
            }

        }

        delay(1300L)
        println("main: I'm tried of waiting!")
        job.cancelAndJoin()
        println("main: Now I can quit")
    }
}

fun makingComputationCode() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while(isActive) {
                if (System.currentTimeMillis() > nextPrintTime) {
                    println(" job : I'm sleeping ${i++}")
                    nextPrintTime += 500L
                }
            }
        }

        delay(1300L)
        println("main: I'm tired of wating")
        job.cancelAndJoin()
        println("main: Now I can quit")
    }
}

fun closingResourcesWithFinally(){
    runBlocking {
        val job = launch {
            try {
                repeat(1000) {
                    println("job : I'm sleeping $it")
                    delay(500L)
                }
            } catch (e: CancellationException){
                println("Catch")
            }
            finally {
                println("job : I'm running finally")
            }
        }
        delay(1300L)
        println("main: I'm tired of waiting")
        job.cancelAndJoin()
        println("main: Now I can quit")
    }
}

fun runNonCancellableBlock (){
    runBlocking {
        val job = launch {
            try {
                repeat(1000) {
                    println("job: I'm sleeping $it")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L)
        println("main : I'tried of wating")
        job.cancelAndJoin()
        println("main : Now I can quit")
    }
}

fun timeOut(){
    runBlocking {
        withTimeout(1300L) {
            repeat(1000) {
                println("I'm sleeping $it")
                delay(500L)
            }
        }
    }
}