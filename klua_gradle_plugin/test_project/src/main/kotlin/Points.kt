import java.awt.Point
import java.awt.event.MouseEvent


data class ARGBColor(
    val color: Int
) : Comparable<ARGBColor?> {
    val alpha: Int
        get() = color shr 24 and 0xFF

    val red: Int
        get() = color shr 16 and 0xFF

    val green: Int
        get() = color shr 8 and 0xFF

    val blue: Int
        get() = color and 0xFF

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return color == (other as ARGBColor).color
    }

    override fun hashCode(): Int {
        return color
    }

    override fun toString(): String {
        return "ARGBColor(a=$alpha, r=$red, g=$green, b=$blue)"
    }

    override fun compareTo(other: ARGBColor?): Int {
        return Integer.compare(this.color, other?.color ?: 0)
    }

    companion object {
        fun ofTransparent(color: Int): ARGBColor {
            return ARGBColor(color)
        }

        fun ofOpaque(color: Int): ARGBColor {
            return ARGBColor(-0x1000000 or color)
        }

        fun ofRGB(r: Float, g: Float, b: Float): ARGBColor {
            return ofRGBA(r, g, b, 1f)
        }

        fun ofRGB(r: Int, g: Int, b: Int): ARGBColor {
            return ofRGBA(r, g, b, 255)
        }

        fun ofRGBA(r: Float, g: Float, b: Float, a: Float): ARGBColor {
            return ofRGBA(
                (r * 255 + 0.5).toInt(),
                (g * 255 + 0.5).toInt(),
                (b * 255 + 0.5).toInt(),
                (a * 255 + 0.5).toInt()
            )
        }

        fun ofRGBA(r: Int, g: Int, b: Int, a: Int): ARGBColor {
            return ARGBColor(
                ((a and 0xFF) shl 24) or
                        ((r and 0xFF) shl 16) or
                        ((g and 0xFF) shl 8) or
                        (b and 0xFF)
            )
        }
    }
}

data class ColoredPoint(val color: ARGBColor,val point: Point)

class Points {
    val points = mutableMapOf<Int, ColoredPoint>()
    var draggedCornerIndex: Int = -1
    var HANDLE_SIZE: Int = 12
    var onSet: (Int,ColoredPoint) -> Unit = { _, _ -> }

    fun mmousePressed(e: MouseEvent) {
        for (i in points.values.indices) {
            if (e.point.distance(points[i]!!.point) < HANDLE_SIZE * 2) {
                draggedCornerIndex = i
                break
            }
        }
    }

    fun mmouseReleased(e: MouseEvent) {
        draggedCornerIndex = -1
    }

    fun mmouseDragged(e: MouseEvent) {
        if (draggedCornerIndex != -1) {
            points[draggedCornerIndex]!!.point.location = e.point
            onSet(draggedCornerIndex,points[draggedCornerIndex]!!)
        }
    }
}