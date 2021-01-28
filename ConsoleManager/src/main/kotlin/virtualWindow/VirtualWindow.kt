package virtualWindow

import util.Color
import util.Console.moveCursorDown
import util.Console.moveCursorLeft
import util.Console.moveCursorRight
import util.Console.moveCursorUp
import util.VectorLong
import virtualWindow.view.View
import java.lang.Exception
import kotlin.concurrent.thread

open class VirtualWindow(
        var position: VectorLong,
        var size: VectorLong,
        var debugMode: Boolean = false
) {
    var frequency = 1.0
    var background = ArrayList<Pixel>()
    var consoleSize = VectorLong(20, size.y)

    protected var pixels = ArrayList<Pixel>()
    protected var views = ArrayList<View>()
    protected var lockView = false;

    protected var cursorPosition = VectorLong(0, 0)
    protected var backgroundColor = Color(0.8, 0.8, 0.8)
    protected var foregroundColor = Color(0.0, 0.0, 0.0)

    init{
        // Init pixels on screen
        for(i in 0 until size.y)
            for(j in 0 until size.x)
                background.add(Pixel(
                        VectorLong(x = j, y = i),
                        ' ',
                        backgroundColor,
                        foregroundColor
                ))
    }

    fun endPosition(): VectorLong = position + size

    fun show(){
        thread(start = true) {
            // Init window
            for (i in 0..size.y - 1)
                println()
            moveCursorUp(size.y)

            // TODO("Refactor endless loop")
            while (true) {
                var timeFrameGeneration = System.currentTimeMillis()
                moveCursor(position)
                printWindow()
                printConsole()
                timeFrameGeneration = (System.currentTimeMillis() - timeFrameGeneration)

                var timeSleep = (1.0 / frequency * 1000).toLong() - timeFrameGeneration
                if(timeSleep < 0) timeSleep = 0

                if(timeSleep > 0)
                    Thread.sleep(timeSleep)
            }
        }
    }

    private fun generateScreen(){
        pixels = ArrayList()
        for(pixel in background)
            pixels.add(Pixel(pixel.position, pixel.value, pixel.colorBackground, pixel.colorForeground))

        // Integrate views' Pixels to VirtualWindow
        while(lockView){Thread.sleep(1)}
        lockView = true
        for (view: View in views){
            if(!view.visible) continue
            val pixelsView = view.getPixels()
            for (pixel in pixelsView){
                val localPixelPosition = pixel.position + view.position
                if(localPixelPosition.x < size.x && localPixelPosition.y < size.y)
                    getPixel(pixel.position + view.position).setValue(pixel)
            }
        }
        lockView = false
    }
    private fun printWindow(){
        // Make picture
        generateScreen()

        // Print pixels
        for (i in 0..size.y - 1){
            for (j in 0..size.x - 1){
                val pixel = pixels[(i * size.x + j).toInt()]
                moveCursor(pixel.position)

                // Optimization
                if(!backgroundColor.equals(pixel.colorBackground)){
                    backgroundColor = pixel.colorBackground
                    print("\u001b[48;5;${backgroundColor.asciiCode}m")
                }
                if(!foregroundColor.equals(pixel.colorForeground)){
                    foregroundColor = pixel.colorForeground
                    print("\u001b[38;5;${foregroundColor.asciiCode}m")
                }
                print(pixel.value)
                cursorPosition.x++
            }
        }
        /*for(view in views){
            val printBuffer = view.getPrintBuffer()
            moveCursor(view.position)

            // Print view
            print(printBuffer.buffer)
        }*/
//        moveCursor(VectorLong(0, 0))

        // Reset terminal colors
        foregroundColor = Color(0.95, 0.95, 0.95)
        backgroundColor = Color()
        print("\u001b[48;5;${backgroundColor.asciiCode}m")
        print("\u001b[38;5;${foregroundColor.asciiCode}m")
    }
    private fun printConsole() {
        TODO("Not yet implemented")
    }

    fun addView(view: View) {
        while(lockView) Thread.sleep(1);
        lockView = true;
        for (viewTemp: View in views)
            if(viewTemp.id == view.id)
                throw Exception("Kundel: ${view.id} already exist in the list")

        views.add(view)
        lockView = false;
    }
    fun deleteView(view: View): Boolean{
        return deleteView(view.id)
    }
    fun deleteView(id: String): Boolean{
        var result = false
        var viewForDeleting: View? = null

        while(lockView) Thread.sleep(1);
        lockView = true;
        for (viewTemp in views)
            if(id == viewTemp.id) {
                viewForDeleting = viewTemp
                result = true
            }
        if(result)
            views.remove(viewForDeleting)
        lockView = false;

        return result
    }

    fun getPixel(position: VectorLong): Pixel = getPixel(position.x, position.y)
    fun getPixel(x: Long, y: Long): Pixel = pixels[(y * size.x + x).toInt()]

    private fun moveCursor(positionInput: VectorLong){
        val position = this.position + positionInput
        if (cursorPosition.x > position.x) moveCursorLeft(cursorPosition.x - position.x)
        else if (cursorPosition.x < position.x) moveCursorRight(position.x - cursorPosition.x)
        if (cursorPosition.y > position.y) moveCursorUp(cursorPosition.y - position.y)
        else if (cursorPosition.y < position.y) moveCursorDown(position.y - cursorPosition.y)
//        cursorPosition += positionInput
        cursorPosition.x = position.x
        cursorPosition.y = position.y
    }

    fun getView(viewId: String): View? {
        var result: View? = null

        while(lockView) Thread.sleep(1);
        lockView = true;
        for (view in views)
            if(view.id == viewId)
                result  = view
        lockView = false;

        return result
    }
}