package docs

import kotlinx.coroutines.*



fun main(args: Array<String>) {

//    runBlocking {
//        launch {
//            basicsExampleSuspendfun()
//        }
//        println("Hello")
//    }
    coroutinesARE()

}

fun coroutinesARE() {
//    runBlocking {
//        repeat(100_000){
//            launch {
//                delay(1000L)
//                println(".")
//            }
//        }
//    }
    runBlocking {

        GlobalScope.launch {
            repeat(1000) {
                println("I'm sleeping $it")
                delay(500L)
            }
        }
        delay(1300L)
    }

}

suspend fun basicsExampleSuspendfun(){
    delay(1000L)
    println("world")
}

/**
 *          runblocking is not suspend fun
 *          coroutineScope is suspend fun
 */

fun basicsExampleThree() = runBlocking {
    launch {
        delay(200L)
        println(" Task from runBlocking")
    }


    coroutineScope {

        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope")
    }

    println(" Coroutine scope is over")
}


fun basicsExampleTwo() = runBlocking {
    launch {
        delay(1000L)
        println("World")
    }
    println("Hello")
}

fun basicsExampleOne() = runBlocking {
    /**
     *  Global scope is used to launch top-level coroutines which are operating on the whole application lifetime and are not cancelled prematurely.
     */
    val job = GlobalScope.launch {
        delay(1000L)
        println("world")
    }

    println("hello")
    job.join()

//    Thread.sleep(2000L)

//    runBlocking {
//        delay(2000L)
//    }
}
