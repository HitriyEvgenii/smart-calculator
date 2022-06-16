package calculator

import java.math.BigInteger

//import java.util.Scanner

fun main() {
    var line = readln()
    val variablesMap = emptyMap<String, BigInteger>().toMutableMap()


    while (line != "/exit") {
        when (line) {
            "/help" -> println("The program calculates the sum of numbers")
            "" -> ""
            else -> {
                if (line.contains('=')) {
                    try {
                        variablesMap += addVariable(line, variablesMap)
                    }
                    catch(e: Exception) {
                        println("Invalid assignment")
                    }

                }
                //else if (line.split(" ").size == 1 && line[0] != '/') println(variablesMap[line])
                else checkLine(line, variablesMap)
                    //checkLineChar(line, variablesMap)
            }
        }
        line = readln()
    }
    println("bye")
}

fun readNumbers(numbers: String, variablesMap: MutableMap<String, BigInteger>): MutableList<String> {
    val numbers = numbers.replace("^\\s+".toRegex(), "")
    var num = numbers.split("\\s+".toRegex()).toMutableList()
    for (i in num) {
        if (i in variablesMap) num[num.indexOf(i)] = variablesMap[i].toString()
    }
    return num
}

fun stringToOperator (a: Int, i: String, b: Int): Int {
    val plus = i.count {operator -> operator == '+'}
    val minus = i.count {operator -> operator == '-'}
    if (plus > 0) return a + b
    else if (minus > 0 && minus % 2 == 0) return a + b
    else return a - b
}

fun checkLine(line: String, variablesMap: MutableMap<String, BigInteger>) {
    val lineList = readNumbers(line, variablesMap)

    if (lineList[0][0] == '/') println("Unknown command")
    else if (lineList.size == 1 && lineList[0].contains("[a-zA-Z]".toRegex())) println("Unknown variable")
    else if (line.contains("[/*]{2,}".toRegex())) println("Invalid expression")
    else if (line.count {i -> i == '('} != line.count {i -> i == ')'}) println("Invalid expression")
    else {
        try {
            var sum = 0.toBigInteger()

            if (lineList.size == 1) println(lineList[0])

            else {
                /* while (lineList.size != 0) {
                    sum = stringToOperator(lineList[0].toInt(), lineList[1], lineList[2].toInt())
                    repeat(3) {
                        lineList.removeAt(0)
                    }

                    if (lineList.size == 0) continue
                    else lineList.add(0, sum.toString())
                } */
                val postfixLine = infixToPostfix(line)
                sum = calculatePostfix(postfixLine, variablesMap)
                println(sum)
            }
        } catch (e: Exception) {
            println("Invalid expression")
        }
    }
}


fun addVariable(variable: String, variablesMap: MutableMap<String, BigInteger>): Pair<String, BigInteger> {
    val variab = variable.replace("\\s*".toRegex(), "")
    val pair = variab.split("=")


    if (pair[0].contains("[a-zA-Z]\\d+".toRegex()) || !pair[0].contains("[a-zA-Z]".toRegex())) {
        println("Invalid identifier")
        return Pair("null", 0.toBigInteger())
    }

    else if (pair[1].contains("[a-zA-Z]\\d+".toRegex())) {
        println("Invalid assignment")
        return Pair("null", 0.toBigInteger())
    }
    else if (pair[1] in variablesMap) {
        val key = pair[1]
        return Pair(pair[0], variablesMap[key]!!)
    }
    else return Pair(pair[0], pair[1].toBigInteger())
}

fun infixToPostfix(line: String): MutableList<String> {
    var line = line.replace("[(]".toRegex(), " ( ")
    line = line.replace("[)]".toRegex(), " ) ")

    var lineList = line.split("\\s+".toRegex()).toMutableList()
    for (i in 0..lineList.size-1) {
        if (lineList[i].contains("[-]+".toRegex())) {
            if (lineList[i].count {oper -> oper == '-'} % 2 ==0) lineList[i] = "+" else lineList[i] = "-"
        }
        else if (lineList[i].contains("[+]+".toRegex())) lineList[i] = "+"
    }
    val outputStack = emptyList<String>().toMutableList()
    val operatorsStack = emptyList<String>().toMutableList()
    for (i in lineList) {

        if (i.contains("\\w".toRegex())) outputStack.add(i)
        else if (operatorsStack.size == 0 || operatorsStack.last() == "(") operatorsStack.add(i)
        else if (i == "^") outputStack.add(i)
        else if (i.contains("[*/]".toRegex()) && operatorsStack.last().contains("[+-]".toRegex())) operatorsStack.add(i)
        else if (i.contains("[*/]".toRegex()) && operatorsStack.last().contains("[*/^]".toRegex())) {
            while (operatorsStack.last().contains("[+-]?\\(?".toRegex())) {
                outputStack.add(operatorsStack.last())
                operatorsStack.removeLast()
                if (operatorsStack.size == 0) break
            }
            operatorsStack.add(i)
        }
        else if (i.contains("[+-]".toRegex()) && operatorsStack.last().contains("[-+*/]".toRegex())) {
            while (operatorsStack.last() != "(") {
                outputStack.add(operatorsStack.last())
                operatorsStack.removeLast()
                if (operatorsStack.size == 0) break
            }
            operatorsStack.add(i)
        }
        else if (i.contains("[+-]".toRegex()) && operatorsStack.last() == "(") operatorsStack.add(i)
        else if (i == "(") operatorsStack.add(i)
        else if (i == ")") {
            while (operatorsStack.last() != "(") {
                outputStack.add(operatorsStack.last())
                operatorsStack.removeLast()
            }
            operatorsStack.removeLast()
        }
    }


    if (operatorsStack.size != 0) {
        while (operatorsStack.size != 0) {
            outputStack.add(operatorsStack.last())
            operatorsStack.removeLast()
        }
    }
    return outputStack
}

fun calculatePostfix(stack: MutableList<String>, variablesMap: MutableMap<String, BigInteger>): BigInteger {
    var sumStack = emptyList<BigInteger>().toMutableList()
    for (i in stack) {
        when {
            i.contains("\\w".toRegex()) -> {
                if (i.contains("\\d".toRegex())) sumStack.add(i.toBigInteger())
                else sumStack.add(variablesMap[i]!!)
            }
            i == "+" -> {
                val lastStack = sumStack.last()
                sumStack.removeLast()
                val previousLast = sumStack.last()
                sumStack.removeLast()
                sumStack.add(lastStack + previousLast)
            }
            i == "-" -> {
                val lastStack = sumStack.last()
                sumStack.removeLast()
                val previousLast = sumStack.last()
                sumStack.removeLast()
                sumStack.add(previousLast - lastStack)
            }
            i == "*" -> {
                val lastStack = sumStack.last()
                sumStack.removeLast()
                val previousLast = sumStack.last()
                sumStack.removeLast()
                sumStack.add(previousLast * lastStack)
            }
            i == "/" -> {
                val lastStack = sumStack.last()
                sumStack.removeLast()
                val previousLast = sumStack.last()
                sumStack.removeLast()
                sumStack.add(previousLast / lastStack)
            }
            i == "^" -> {
                val lastStack = sumStack.last()
                sumStack.removeLast()
                val previousLast = sumStack.last()
                sumStack.removeLast()
                sumStack.add(previousLast - lastStack)
            }
        }
    }
    return sumStack[0]
}