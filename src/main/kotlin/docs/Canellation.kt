package docs

import kotlinx.coroutines.*

fun main(args: Array<String>){
    withTimeOutOrNull()
}

fun cancellingCoroutineExecution(){
    runBlocking {
      val job = launch {
          repeat(1000) {
              println("job : I'm sleeping $it")
              delay(500L)
          }
      }
        delay(1300L)
        println(" main: waiting stop")
        job.cancel()
        job.join()
        println(" main : stop")
    }
}


/**
 *          if a coroutine is working in a computation and does not check for cancellation, then it cannot be cancelled,
 *
 */
fun cancellationCooperativeInWoring(){
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch (Dispatchers.Default){
            var nextPrintTime = startTime
            var i = 0
            while( i < 5){
                if (System.currentTimeMillis() >= nextPrintTime){
                    println(" job i'm sleeping ${i++}")
                    nextPrintTime += 500L
                }
            }
        }

        delay(1300L)
        println(" main: waiting stop ")
        job.cancelAndJoin()
        println(" main : stop")

    }

}

fun makingComputationCacellable(){
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default){
            var nextPrintTime = startTime
            var i = 0
            while (isActive){
                if (System.currentTimeMillis() >= nextPrintTime){
                    println(" job i'm sleeping ${i++}")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L)
        println(" main: waiting stop ")
        job.cancelAndJoin()
        println(" main : stop")
    }
}


/**
 *  Cancellable suspending functions throw CancellationException on cancellation
 *
 */
fun cancellingWithFinally(){
    runBlocking {

       val job = launch {
            try {
                repeat(1000) {
                    println(" job i'm sleeping $it")
                    delay(500L)
                }
            }catch (e: Exception){
                println(" job : i'm Exception ")
            }finally {
                println(" job : i'm running finally ")
            }

        }

        delay(1300L)
        println(" main: waiting stop ")
        job.cancelAndJoin()
        println(" main : stop")
    }
}

// cancel 된 코루틴 에서 corountine이 필요한 경우

fun runNonCancellableBlock(){
    runBlocking {
        val job = launch {
            try {
                repeat(1000){
                    println("job : i'm sleeping $it ....  ")
                    delay(500L)
                }
            }finally {
                withContext(NonCancellable){
                    println("job : I'm running finally ")
                    delay(1000L)
                    println(" job : And I've just delayed for 1 sec because i'm non-cancellable ")
                }

            }
        }

        delay(1300L)
        println(" main: waiting stop ")
        job.cancelAndJoin()
        println(" main : stop")
    }
}

fun timeOut(){
    runBlocking {
        withTimeout(1300L){
            repeat(1000){
                println(" I'm sleeping $it  ")
                delay(500L)
            }
        }
    }
}

fun withTimeOutOrNull(){
    runBlocking {
        val result = withTimeoutOrNull(1300L){
            repeat(1000) {
                println(" I'm sleeping $it")
                delay(500L)
            }
            "Done"
        }
        println(" Result is $result  ")
    }
}