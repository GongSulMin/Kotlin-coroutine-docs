package docs

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking


fun main(args: Array<String>) {
    runBlocking<Unit> {
        flowsAreSqeuential()
    }
}

/**
 *
 *         this computation blocks the main thread
 */
//fun foo(): Sequence<Int> = sequence {
//    for (i in 1..3) {
//        Thread.sleep(100)
//        yield(i)
//    }
//}


//suspend fun foo(): List<Int> {
//    delay(1000)
//    return listOf(1,2,3)
//}

fun foo(): Flow<Int> = flow {

    /**
     *          Flows are cold Main
     *
     */
//    runBlocking {
//        println(" Calling foo .... ")
//        val flow = foo()
//        println(" Calling collect ....")
//        foo().collect { value -> println(value)}
//        println(" Calling collect again..... ? ")
//        foo().collect { value -> println(value)}
//    }

//    runBlocking {
//        launch {
//            for (k in 1..3) {
//                println(" I'm not blocked $k ")
//                delay(100)
//            }
//        }
//        foo().collect { value -> println(value)}
//    }

    println(" Flow started ")
    for (i in 1..3){
        delay(100)
        emit(i)
    }
}

fun flowCancellation(): Flow<Int> = flow {

    /**
     *  Main
     *
     */
//    runBlocking {
//        withTimeoutOrNull(400) {
//            flowCancellation().collect { value -> println(value) }
//        }
//    }
//    println(" Done ")

    for (i in 1..3){
        delay(100)
        println("emitting $i")
        emit(i)
    }
}

fun flowBuilders() {
    runBlocking {
        (1..3).asFlow().collect { value -> println(value) }
    }
}

suspend fun intermediateFlowOperator(request: Int): String {
    /**
     *                    (1..3).asFlow()
                                .map { request -> intermediateFlowOperator(request) }
                                .collect { reponse -> println(reponse) }
     *
     */

    // error
    delay(1000)
    return "response $request"
}

suspend fun performRequest(request: Int): String {

    /**
     *
     *                       (1..3).asFlow()
                                    .transform {
                                        request ->
                                            emit(" Making request $request")
                                            emit(performRequest(request))
                                    }
                                        .collect { println(it) }
     *
     */
    delay(1000)
    return "reponse $request "
}


fun sizeLimitOperstor(): Flow<Int> = flow {


//    sizeLimitOperstor()
//        .take(2)
//        .collect { value -> println(value) }
    try {
        emit(1)
        emit(2)
        println(" This line will not execute")
        emit(3)
    }finally {
        println(" Finally in numbers ")
    }
}

suspend fun reduce(){
    val sum = (1..5).asFlow()
        .map{ it * it}
        .reduce { a, b ->  a + b}
    println(sum)
}

suspend fun flowsAreSqeuential() {
    (1..5).asFlow()
        .filter{
            println(" Filter $it")
            it % 2 == 0
        }
        .map {
            println(" Map $it ")
            "String $it"
        }
        .collect {
            println(" Collect $it ")
        }
}

