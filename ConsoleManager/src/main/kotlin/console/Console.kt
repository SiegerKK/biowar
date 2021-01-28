package console

class Console (

){
    companion object{
        fun write(text: String = "") = print(text)
        fun writeln(text: String = "")= print(text + "\n")
    }
}