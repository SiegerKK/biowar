package virtualWindow.view

import util.Color
import util.VectorLong
import virtualWindow.Pixel

class TableColumnView(
        id: String, position: VectorLong, /*size: VectorLong,*/
        var height: Int = 0,
        var cellSize: VectorLong = VectorLong(5,3),
        val cells: ArrayList<TextView> = ArrayList()
): View(id, position, VectorLong(cellSize.x, cellSize.y * height)) {
    init {
        for (i in 0 until height)
            cells.add(TextView("$id/idCell$i", VectorLong(0, cellSize.y * i), cellSize, "XYU"))
    }

    fun setPadding(paddingVertical: Int, paddingHorizontal: Int){
        for (textView in cells) {
            textView.paddingVertical = paddingVertical
            textView.paddingHorizontal = paddingHorizontal
        }
    }

    override fun getPrintBuffer(): PrintBuffer {
        TODO("Not yet implemented")
    }

    override fun getPixels(): ArrayList<Pixel> {
        val pixels = ArrayList<Pixel>()

        for (i in 0 until size.y){
            for (j in 0 until size.x){
                // Init Pixel
                val pixelPosition = VectorLong(j, i)
                val pixelValue = ' '

                // Add Pixel to list
                pixels.add(Pixel(
                        pixelPosition,
                        pixelValue,
                        Color(),
                        Color(0.5, 0.5, 0.7)
                ))
            }
        }
        for (i in 0 until height){
            val cellPixels = cells[i].getPixels()
            for (pixel in cellPixels) {
                val y = cells[i].position.y + pixel.position.y
                val id = (y * size.x + pixel.position.x + cells[i].position.x).toInt()
                if (id >= pixels.size) break
                pixels[id].value = pixel.value
                pixels[id].colorBackground = pixel.colorBackground
                pixels[id].colorForeground = pixel.colorForeground
            }
        }

        return pixels
    }
}