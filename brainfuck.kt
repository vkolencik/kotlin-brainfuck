import java.util.*

class BrainLuck(private val code: String) {
  
  private var codePtr = 0
  
  private val memory = HashMap<Int, Byte>()
  
  private var dataPtr = 0
  
  private var mem: Byte
    get() = memory[dataPtr] ?: 0
    set(value: Byte) { memory[dataPtr] = value }
  
  fun process(input: String): String {
    var inputPtr = 0
    var output = StringBuilder()
    var matchingParens = compileParens(code)
    
    loop@ while (codePtr in code.indices) {
      when (code[codePtr]) {
        '>' -> dataPtr++
        '<' -> dataPtr--
        '+' -> mem++
        '-' -> mem--
        '.' -> if (mem == 255.toByte()) break@loop else output.append(mem.toChar().toString())
        ',' -> mem = input[inputPtr++].toByte()
        '[' -> if (mem == 0.toByte()) codePtr = matchingParens[codePtr]!!
        ']' -> if (mem != 0.toByte()) codePtr = matchingParens[codePtr]!!
      }
      codePtr++      
    }
    
    return output.toString()
  }
  
  fun compileParens(code: String): Map<Int, Int> {
    val stack = Stack<Int>()
    val matchingParens = HashMap<Int, Int>()
    code.forEachIndexed { i, ch ->
      if (ch == '[')
        stack.push(i)
      if (ch == ']') {
        if (stack.empty())
          throw IllegalArgumentException("']' without a matching '[' on position $i")
        matchingParens[i] = stack.pop()
        matchingParens[matchingParens[i]!!] = i // bidirectional map
      }
    }
    
    if (!stack.empty())
      throw IllegalArgumentException("'[' without a matching ']' on position ${stack.pop()}")
    
    return matchingParens
  }
    
}
