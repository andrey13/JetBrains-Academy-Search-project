package search
import java.io.File

val data = mutableListOf<String>()
val indexData = mutableMapOf<String, MutableSet<Int>>()

fun inputData(fileName: String) {
    File(fileName).forEachLine { data.add(it) }
}

fun createIndex() {
    var iStr = -1
    for (i in data) {
        ++iStr
        for (word in i.split(" ")) {
            if (indexData.containsKey(word)) {
                indexData[word]?.add(iStr)
            } else {
                indexData[word] = mutableSetOf<Int>(iStr)
            }
        }
    }
}

fun searchALL(s: String): MutableSet<Int> {
    val result = mutableSetOf<Int>()
    for ((k, v) in indexData) {
        if (k.uppercase() == s.uppercase()) {
            for (i in indexData[k]!!) result.add(i)
            return result
        }
    }
    return result
}

fun searchStart(searchString: String, strategy: String): MutableSet<Int> {
    val a = searchString.split(" ")
    when(strategy) {
        "ALL" -> {
            var result = mutableSetOf<Int>()
            for (i in 0..data.size-1)  result.add(i)
            for (s in a) {
                val r = searchALL(s)
                result = result.intersect(r).toMutableSet()
            }
            return result
        }
        "ANY" -> {
            var result = mutableSetOf<Int>()
            for (s in a) {
                val r = searchALL(s)
                result = result.union(r).toMutableSet()
            }
            return result
        }
        "NONE" -> {
            var result = mutableSetOf<Int>()
            for (i in 0..data.size-1)  result.add(i)
            for (s in a) {
                val r = searchALL(s)
                result.removeAll(r)
            }
            return result
        }
    }
    return mutableSetOf<Int>()
}

fun printResult(result: MutableSet<Int>) {
    if (result.isEmpty()) println("No matching people found.")
    println("People found:")
    for (i in result) println(data[i])
}

fun selectMenu(): Int {
    while(true) {
        println("")
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        val ans = readLine()!!
        if (ans.matches(Regex("[012]"))) return ans.toInt()
        println("Incorrect option! Try again.")
    }
}

fun main(args: Array<String>) {
    inputData(args[1])
    createIndex()
    while (true) {
        val ans = selectMenu()
        when(ans) {
            0 -> break
            2 -> data.map{item -> println(item)}
            1 -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val strategy = readLine()!!
                println("Enter a name or email to search all suitable people.")
                val searchString = readLine()!!
                printResult(searchStart(searchString, strategy))
            }
        }
    }
    println("Bye!")
}