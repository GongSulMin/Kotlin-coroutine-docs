package docs

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

fun main(args: Array<String>){
    primeNumbersWithPipeLine()
}

fun channelBascis(){
    runBlocking {
        val channel = Channel<Int>()

        launch {
            for( x in 1..5 ) channel.send( x * x )
        }

        repeat(5){
            println(channel.receive())
        }

        println("Done!!")
    }
}

fun closingChannel(){
    runBlocking {
        val channels = Channel<Int>()

        launch {
            for( x in 1..5) channels.send( x * x)
            channels.close()
        }
        for (channel in channels) println(channel)
        println("Done ")
    }
}



fun buildingChannelProducers(){
    runBlocking {
        val squares = produceSquares()
        squares.consumeEach { println(it) }
        println(" Done ")
    }
}

fun pipeLines(){
    runBlocking {
        val numbers = produceNumbers()
        val squares = squre(numbers)

        for (i in 1..5) println(squares.receive())
        println("Done")
        coroutineContext.cancelChildren()
    }
}
// producing infinite
fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++)
}

fun CoroutineScope.squre(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send( x * x)
}


fun primeNumbersWithPipeLine(){

    runBlocking {
        var cur = numbersFrom(2)
        for (i in 1..10 ){
            var prime = cur.receive()
            println(prime)
            cur = filter(cur , prime)
        }
    }

}

fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++)
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int> , prime: Int) = produce<Int> {
    for (x in numbers) if (x % prime != 0 ) send(x)
}