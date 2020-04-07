import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() {
    runBlocking {
//        produceSquares()

        // pipeline
//        val numbers = produceNumbers()
//        val squares = square(numbers)
//        repeat(5) {
//            println(squares.receive())
//        }
//        println("Done")
//        coroutineContext.cancelChildren()

//        var cur = numbersFrom(2)
//        repeat(10) {
//            val prime = cur.receive()
//            println(prime)
//            cur = filter(cur , prime)
//        }
//        coroutineContext.cancelChildren()

//        val producer = produceNumbers()
//
//        repeat(5) {
//            launchProcessor(it , producer)
//        }
//
//        delay(950)
//        producer.cancel()
//
//
//        val channel = Channel<String>()
//        launch { sendString(channel , "foo" , 200L) }
//        launch { sendString(channel , "Bar" , 500L) }
//
//        repeat(6) {
//            println(channel.receive())
//        }
//
//        coroutineContext.cancelChildren()

        bufferChannel()
    }
}


suspend fun CoroutineScope.channelBasics() {
    val channel = Channel<Int>()

    coroutineScope {
        launch {
            for (x in 1..5) {
                channel.send( x * x )
            }
        }

        repeat(5 ){
            println(channel.receive())
        }
    }
}

suspend fun CoroutineScope.closingOverChannels() {
    val channel = Channel<Int>()

    launch {
        for(x in 1..5) channel.send( x * x)
        channel.close()
    }

    for (y in channel) println(y)
    println("Done!")

}

fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
    for (x in 1..5) send(x * x)
}

fun CoroutineScope.produceNumbers() = produce<Int> {
    var x = 1
    while (true) send(x++) // infinite stream of integers starting from 1
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}

fun CoroutineScope.numbersFrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++)
}

fun CoroutineScope.filter(numbers: ReceiveChannel<Int>, prime: Int) = produce {
    for (x in numbers) if (x % prime != 0 ) send(x)
}

fun CoroutineScope.launchProcessor(id: Int , channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println(" Processor #$id received $msg")
    }
}

suspend fun sendString(channel: SendChannel<String> , s: String , time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}


fun bufferChannel() {
    runBlocking {
        val channel = Channel<Int>(4)
        val sendser = launch {
            repeat(10) {
                println(" Sending $it")
                channel.send(it)
            }
        }
        delay(1000)
        sendser.cancel()
    }
}
