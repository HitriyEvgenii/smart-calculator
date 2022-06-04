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
                var sum = 0
                val lineList = readNumbers(line)
                if (lineList.size == 1) println(lineList[0].toInt())
                else {
                    while (lineList.size != 0) {
                        sum = stringToOperator(lineList[0].toInt(), lineList[1], lineList[2].toInt())
                        repeat(3){
                            lineList.removeAt(0)
                        }

                        if (lineList.size == 0) continue
                        else lineList.add(0, sum.toString())
                    }
                    println(sum)
                }

            }
        }

        line = readln()
    }
    println("bye")

}

fun readNumbers(numbers: String): MutableList<String> {
    val line = numbers.split(" ").toMutableList()
    line.removeAll(List(line.count {i -> i == ""}){""})
    return line
}

fun stringToOperator (a: Int, i: String, b: Int): Int {
    val plus = i.count {operator -> operator == '+'}
    val minus = i.count {operator -> operator == '-'}
    if (plus > 0) return a + b
    else if (minus > 0 && minus % 2 == 0) return a + b
    else return a - b
}