@file:JvmName("Main")

import org.w3c.dom.Text
import util.Color
import util.VectorLong
import virtualWindow.VirtualWindow
import virtualWindow.view.*

lateinit var window: VirtualWindow
lateinit var console: VirtualWindow

fun main(){
    init()

    val table = TableView("idTableView1", VectorLong(10,25), VectorLong(3, 5), VectorLong(20, 3))
    table.setPadding(1, 3)
    window.addView(table)

    val totalView = 20
    for (i in 0 until totalView) {
        val textView = TextView("idTextView$i", VectorLong(5 + 3 * i.toLong(), 5 + 2 * i.toLong()), VectorLong(90, 3),
            "XYU XYU XYU XYU XYU 1 XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU " +
                    "XYU XYU XYU XYU XYU XYU XYU 2 XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU " +
                    "XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU 3 XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU XYU " +
                    "XYU XYU XYU XYU XYU XYU 4 XYU")
        textView.paddingHorizontal = 2
        textView.paddingVertical = 1
        textView.colorBackground = Color(0.4,  i.toDouble() / totalView, 0.2)
        window.addView(textView)
    }
    window.addView(NoiseView("idNoiseView1", VectorLong(5,5), VectorLong(5, 5)))

    Thread.sleep(1000)
    window.deleteView("idTextView1")

    for(i in 0 until totalView) {
        Thread.sleep(400)

        window.getView("idNoiseView1")!!.size.x += 5
        window.getView("idNoiseView1")!!.size.y += 1

        val viewText: TextView? = window.getView("idTextView14") as TextView
        if(viewText != null){
            viewText.size.x -= 3
            viewText.size.y += 1
//            viewText.position.y -= 1
            viewText.position.x -= 1

            viewText.colorBackground = Color(i.toDouble() / totalView, i.toDouble() / totalView,i.toDouble() / totalView)
            viewText.colorForeground = Color(1 - i.toDouble() / totalView, 1 - i.toDouble() / totalView,1 - i.toDouble() / totalView)
        }

//        window.deleteView("idTextView$i")
        window.getView("idTextView$i")?.visible = false
    }

    for(i in 0 until totalView) {
        Thread.sleep(200)

        window.getView("idNoiseView1")!!.size.x -= 3
        window.getView("idNoiseView1")!!.size.y -= 1

        window.getView("idTextView$i")?.visible = true
    }
}

// Setup functions
fun init(){
    window = VirtualWindow(VectorLong(0, 0), VectorLong(150, 50))
    window.frequency = 20.0
    window.show()
}