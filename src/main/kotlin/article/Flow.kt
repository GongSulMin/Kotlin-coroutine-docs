package article

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>){
    exampleFlow6_Switchmap()
}

fun exampleFlow1_Basic(){
    runBlocking {
        flow {
            for (i in 0..5) {
                delay(100)
                emit(i)
            }
        }.collect {
            println(" $it ")
        }
    }
}

fun exampleFlow2_Exception(){
    runBlocking {
        try {
            flow<Double>{
                throw Exception(" Wrong ")
            }.collect{
                println(it)
            }
        }catch (e: Exception){

        }
    }
}

sealed class DownloadStatus {
    data class Progerss(val value: Double): DownloadStatus()
    object Success: DownloadStatus()
    data class Error(val message: String): DownloadStatus()
}

fun exampleFlow3_DownloadStatus(){
    runBlocking {
        flow<DownloadStatus>{
            emit(DownloadStatus.Progerss(0.1))

            try {

            }catch (e: Exception){
                emit(DownloadStatus.Error(e.message!!))
            }

        }.collect {
            println(it)
        }
    }
}

fun exampleFlow4_Completetion(){
    runBlocking {
        flow<String> {
            emit("aaaaa")
        }.onCompletion {
            println("cccccc")
        }.collect {
            println("bbbbbb")
        }
    }
}
 fun exampleFlow5_Map(){

     runBlocking {
         flow{
             for (i in 0..5){
                 delay(100)
                 emit(i)
             }
         }.map {
             it * 2
         }.collect {
             println(it)
         }
     }

}

fun exampleFlow6_Switchmap(){

    runBlocking {
        flow<Int> {
            for (i in 0..5) {
                delay(100)
                emit(i)
            }
        }.transformLatest<Int,String> {
            flow {
                emit("first value $it")
                delay(500)
                emit("seond value $it")
            }.collect{
                println(" third $it")
            }
        }.collect {
            println(it)
        }
    }

}