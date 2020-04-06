package docs

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.concurrent.fixedRateTimer
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

fun main() {

    catchingDeclaratively()


    runBlocking {

//        (1..3).asFlow().map {
//            flatteningFlow(it)
//        }




//        val time = measureTimeMillis {
//            foo().collectLatest { value -> // 취소 & 재시작
//                println("Collecting $value")
//                delay(300) // 처리하는 척
//                println("Done $value")
//            }
//        }
//        val time = measureTimeMillis {
//            buffering()
//                .conflate() // conflate emissions, don't process each one
//                .collect { value ->
//                    delay(300) // pretend we are processing it for 300 ms
//                    println(value)
//                }
//        }
//        println("Collected in $time ms")
    }


//
//    runBlocking {
//        val time = measureTimeMillis {
//            buffering()
//                .buffer()
//                .collect {
//                delay(300)
//                println(it)
//            }
//        }
//
//        println(" Collected in  $time ms")
//    }

//    runBlocking {
//        flowOnOperator().collect {
//            log("Collected $it")
//        }
//    }

//    runBlocking(Dispatchers.Default) {
//        wrongEmissionWithContext().collect{
//            println(it)
//        }
//    }

//    runBlocking {
//        flowContext().collect {
//            log("colledted  $it")
//        }
//    }

//    (1..5)
//        .filter {
//            println("Filter $it")
//            it % 2 == 0
//        }
//        .map {
//            println("Map $it")
//            "string $it"
//        }
//
//    println("---------------------------")
//
//
//    runBlocking {
//        flowAreSequential()
//    }


//
//    runBlocking {
//        sizeLimitOperator()
//            .take(2)
//            .collect {
//                println(it)
//            }
//    }




//    runBlocking {
//        (1..3).asFlow()
//            .transform {
//                request ->
//                emit("Making request $request")
//                emit(performRequest(request))
//            }
//            .collect {
//                println(it)
//            }
//    }




//    runBlocking<Unit> {
//        (1..3).asFlow()
//            .map { request -> performRequest(request) }
//            .collect { response -> println(response) }
//    }


}




fun representMultipleValue(){
    val list = listOf<Int>(1,2,3)

    list.forEach {
        println(it)
    }
}

fun sequence(): Sequence<Int> {
    return sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }
}

suspend fun suspendingFunction():List<Int> {
    delay(5000)
    return listOf<Int>(1 , 2, 3)
}

fun flows(): Flow<Int> {
    return flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

}

fun flowAreCold(): Flow<Int> {
    println("flow start")
    return flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }
}

fun flowCancellation(): Flow<Int> {
    return flow{
        for (i in 1..3){
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }
}

suspend fun performRequest(request: Int): String {
    return "response $request"
}

fun sizeLimitOperator (): Flow<Int> {
    return flow {
        try {
            emit(1)
            emit(2)
            println(" not executed this line")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }

}

suspend fun flowAreSequential(){
    (1..5).asFlow()
        .filter {
            println("Filter $it")
            it % 2 == 0
        }
        .map {
            println("Map $it")
            "string $it"
        }
        .collect {
            println("Colleect $it")
        }
}

fun flowContext(): Flow<Int> {
    return flow {

        log(" Started flow ")

        for (i in 1..3) {
            emit(i)
        }
    }
}

fun wrongEmissionWithContext(): Flow<Int> {
    return flow {
        kotlinx.coroutines.withContext(Dispatchers.Default) {
            for (i in 1..3) {
                Thread.sleep(100)
                emit(i)
            }
        }
    }

}

fun flowOnOperator(): Flow<Int> {
    return flow {
        for (i in 1..3){
            Thread.sleep(100)
            log("Emitting $i")
            emit(i)
        }
    }.flowOn(Dispatchers.Default)
}

fun buffering(): Flow<Int> {
    return flow {
        for (i in 1..3) {
            emit(i)
        }
    }
}


fun foo(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}

fun zip() {
    runBlocking {
        val nums = (1..3).asFlow()

        val strs = flowOf("one" , "two" , "three")

        nums.zip(strs) { a, b ->
            "$a -> $b"
        }
            .collect {
                println(it)
            }
    }


}

fun flatteningFlow(i: Int): Flow<String>{
    return flow {
        emit("$i : Fisrt")
        delay(500)
        emit(" $i: Seconde")
    }
}

fun collectorTryCatch() {
    runBlocking {
        try {
            foo().collect {
                println(it)
                check(it <= 1) {
                    "Collected $it"
                }
            }

        } catch (e: Throwable) {
            println("Caught $e")
        }
    }

}

fun everyThingIsCaught(): Flow<String> {
    return flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i)
        }
    }
        .map {
            check(it <= 1) {
                "Crashed on $it"
            }
            "string $it"
        }

}

fun exceptionTransparency() {

    runBlocking {
        foo()
            .catch { e -> println("Caught $e ") }
            .collect {
                check(it <= 1) {
                    " Collected $it "
                }
                println(it)
            }
    }

}

fun catchingDeclaratively() {
    runBlocking {
        foo()
            .onEach {
                check(it <= 1) {
                    "Collected $it"
                }
                println(it)
            }
            .catch { e -> println("Caufht $e") }
            .collect()
    }
}