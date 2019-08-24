package docs

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

fun main(args: Array<String>){
    channeldAreFair()
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

fun fanOut() {
    runBlocking {
        val producer = produceNumbersWithDelay()
        repeat(5) {
            launchProcessor(it , producer)
        }
        delay(950)
        producer.cancel()
    }
}

fun CoroutineScope.produceNumbersWithDelay() = produce<Int> {
    var x = 1
    while(true){
        send(x++)
        delay(100)
    }
}

fun CoroutineScope.launchProcessor(id: Int , channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println(" Processor #$id   received $msg")
    }
}

fun fanIn() {
    runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel , "foo" , 200L) }
        launch { sendString(channel , "Bar" , 500L) }
        repeat(6) {
            println(channel.receive())
        }

        coroutineContext.cancelChildren()
    }
}

suspend fun sendString(channel: SendChannel<String> , s: String , time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun bufferedChannels() {
    runBlocking {
        val channel = Channel<Int>(4)
        val sender = launch {
            repeat(10) {
                println(" Sending  $it")
                channel.send(it)
            }
        }

        delay(1000)
        sender.cancel()
    }
}

fun channeldAreFair(){
    runBlocking {
        val table = Channel<Ball>()
        launch { player("ping" , table) }
        launch { player("pong" , table) }
        table.send(Ball(0))
        delay(1000)
        coroutineContext.cancelChildren()
    }
}

data class Ball(var hits: Int)

suspend fun player(name: String , table: Channel<Ball>) {
    for (ball in table) {
        ball.hits++
        println("$name $ball")
        delay(300)
        table.send(ball)
    }
}