package docs

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer
import kotlinx.coroutines.*
import java.lang.ArithmeticException
import kotlin.system.measureTimeMillis

fun main() {
    failedConcurrent()
}

fun sequentialByDefault(){

    runBlocking {
        val time = measureTimeMillis {
            val one = doSomethingUsefulOne()
            val two = doSomethingUsefulTwo()
            println(" The answer is ${one + two}")
        }
        println("Completed in $time ms")
    }

}

suspend fun doSomethingUsefulOne(): Int{
    delay(1000L)
    return 13
}

suspend fun doSomethingUsefulTwo(): Int{
    delay(1000L)
    return 29
}

fun concurrentAsync(){
    runBlocking {
        val time = measureTimeMillis {
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            println(" The answer is ${one.await() + two.await()}")
        }
        println("Compledted in $time ms")
    }
}

fun lazilyStaredAsync() {
    runBlocking {
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY){ doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY){ doSomethingUsefulTwo() }

            one.start()
            two.start()

            println("The answer is ${one.await() + two.await()}")
        }
        println("Completed in $time ms ")
    }
}

fun somethingUsefulOneAsync(): Deferred<Int> {
    return GlobalScope.async {
        doSomethingUsefulOne()
    }
}

fun somethingUsefulTwoAsync(): Deferred<Int> {
    return GlobalScope.async {
        doSomethingUsefulTwo()
    }
}

fun asyncStyleFunction() {
    runBlocking {
        val time = measureTimeMillis {
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()

            runBlocking {
                println(" The answer is ${one.await() + two.await()}")
            }

        }

        println(" Completed in $time ms")
    }
}

suspend fun concurrentSum(): Int{
    var data = 0
    coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        data = one.await() + two.await()
    }
    return  data
}

fun structuredConcurrencyWithAsync(){
    runBlocking {
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }
        println("Compeleted is $time ms")

    }

}

fun failedConcurrent(){
    runBlocking {
        try {
            failedConcurrentSum()
        } catch (e: ArithmeticException) {
            println("Computation failed with ArithmeticException")
        }
    }
}

suspend fun failedConcurrentSum(): Int =
    coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE)
                42
            } finally {
                println("First child was cancelled")
            }
        }

        val two = async<Int>{
            println(" Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }
