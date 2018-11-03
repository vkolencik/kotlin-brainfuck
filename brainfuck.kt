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
    var parens = processParens(code)
    
    loop@ while (codePtr in code.indices) {
      when (code[codePtr]) {
        '>' -> dataPtr++
        '<' -> dataPtr--
        '+' -> mem++
        '-' -> mem--
        '.' -> if (mem == 255.toByte()) break@loop else output.append(mem.toChar().toString())
        ',' -> mem = input[inputPtr++].toByte()
        '[' -> if (mem == 0.toByte()) codePtr = parens[codePtr]!!
        ']' -> if (mem != 0.toByte()) codePtr = parens[codePtr]!!
      }
      codePtr++      
    }
    
    return output.toString()
  }
  
  fun processParens(code: String): Map<Int, Int> {
    val stack = Stack<Int>()
    val parens = HashMap<Int, Int>()
    code.forEachIndexed { i, ch ->
      if (ch == '[')
        stack.push(i)
      if (ch == ']') {
        if (stack.empty())
          throw IllegalArgumentException("']' without a matching '[' on position $i")
        parens[i] = stack.pop()
        parens[parens[i]!!] = i // bidirectional map
      }
    }
    
    if (!stack.empty())
      throw IllegalArgumentException("'[' without a matching ']' on position ${stack.pop()}")
    
    return parens
  }
  
  
}
