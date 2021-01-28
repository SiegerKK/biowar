package virtualWindow.view

import util.Color
import util.RandomManager
import util.VectorLong
import virtualWindow.Pixel

class NoiseView(
    id: String, position: VectorLong, size: VectorLong,
) : View(id, position, size) {
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
                    Color(RandomManager.randomDouble(), RandomManager.randomDouble(), RandomManager.randomDouble())
                ))
            }
        }

        return pixels
    }
}