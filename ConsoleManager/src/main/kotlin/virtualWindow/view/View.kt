package virtualWindow.view

import util.VectorLong
import virtualWindow.Pixel

abstract class View(
        val id: String,
        var position: VectorLong,
        var size: VectorLong,
        var visible: Boolean = true
) {
    @Deprecated("For VirtualWindow use getPixels()")
    abstract fun getPrintBuffer(): PrintBuffer
    abstract fun getPixels(): ArrayList<Pixel>
}