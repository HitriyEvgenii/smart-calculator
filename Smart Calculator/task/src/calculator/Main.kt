package calculator
//import java.util.Scanner

fun main() {
    var line = readln()
    //var line = "9 +++ 10 -- 8"

    while (line != "/exit") {
        when (line) {
            "/help" -> println("The program calculates the sum of numbers")
            "" -> ""

            else -> {
                checkLine(line)

            }
        }

        line = readln()
    }
    println("bye")

}

fun readNumbers(numbers: String): MutableList<String> = numbers.split("\\s+".toRegex()).toMutableList()
    // val line = numbers.split(" ").toMutableList()
    // line.removeAll(List(line.count {i -> i == ""}){""})

fun stringToOperator (a: Int, i: String, b: Int): Int {
    val plus = i.count {operator -> operator == '+'}
    val minus = i.count {operator -> operator == '-'}
    if (plus > 0) return a + b
    else if (minus > 0 && minus % 2 == 0) return a + b
    else return a - b
}

fun checkLine(line: String) {
    val lineList = readNumbers(line)

    if (lineList[0][0] == '/') println("Unknown command")
    else {
        try {
            var sum = 0

            if (lineList.size == 1) println(lineList[0].toInt())
            else {
                while (lineList.size != 0) {
                    sum = stringToOperator(lineList[0].toInt(), lineList[1], lineList[2].toInt())
                    repeat(3) {
                        lineList.removeAt(0)
                    }

                    if (lineList.size == 0) continue
                    else lineList.add(0, sum.toString())
                }
                println(sum)
            }
        } catch (e: Exception) {
            println("Invalid expression")
        }
    }
}
