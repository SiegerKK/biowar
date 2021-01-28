package virtualWindow.view

import util.Color
import util.VectorLong
import virtualWindow.Pixel

class TableView(
        id: String, position: VectorLong,
        var tableSize: VectorLong,
        var cellSize: VectorLong,
        val columns: ArrayList<TableColumnView> = ArrayList(),
        var randomColor: Boolean = false
): View(id, position, VectorLong(cellSize.x * tableSize.x, cellSize.y * tableSize.y)) {
    init{
        for (i in 0 until tableSize.x)
            columns.add(TableColumnView("$id/idColumn$i", VectorLong(cellSize.x * i, 0), tableSize.y.toInt(), cellSize))
    }


    fun setPadding(paddingVertical: Int, paddingHorizontal: Int){
        for (column in columns)
            column.setPadding(paddingVertical, paddingHorizontal)
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
                        Color(0.4, 0.4, 0.8)
                ))
            }
        }
        for (i in 0 until tableSize.x){
            val cellPixels = columns[i.toInt()].getPixels()
            for (pixel in cellPixels) {
                val y = columns[i.toInt()].position.y + pixel.position.y
                val id = (y * size.x + pixel.position.x + columns[i.toInt()].position.x).toInt()
                if (id >= pixels.size) break
                pixels[id].value = pixel.value
                pixels[id].colorBackground = pixel.colorBackground
                pixels[id].colorForeground = pixel.colorForeground
            }
        }

        return pixels
    }
}