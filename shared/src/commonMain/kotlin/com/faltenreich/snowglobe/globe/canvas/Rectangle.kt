@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

@Immutable
@kotlin.jvm.JvmInline
value class Rectangle(private val values: FloatArray) {

    val left: Float get() = values[0]
    val top: Float get() = values[1]
    val right: Float get() = values[2]
    val bottom: Float get() = values[3]

    val width: Float get() = right - left
    val height: Float get() = bottom - top

    val size: Size get() = Size(width, height)

    val topLeft: Offset get() = Offset(left, top)
    val topCenter: Offset get() = Offset(left + width / 2.0f, top)
    val topRight: Offset get() = Offset(right, top)
    val centerLeft: Offset get() = Offset(left, top + height / 2.0f)
    val center: Offset get() = Offset(left + width / 2.0f, top + height / 2.0f)
    val centerRight: Offset get() = Offset(right, top + height / 2.0f)
    val bottomLeft: Offset get() = Offset(left, bottom)
    val bottomCenter: Offset get() = Offset(left + width / 2.0f, bottom)
    val bottomRight: Offset get() = Offset(right, bottom)

    constructor(
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
    ) : this(
        values = floatArrayOf(left, top, right, bottom),
    )

    constructor(
        offset: Offset,
        size: Size,
    ) : this(
        left = offset.x,
        top = offset.y,
        right = offset.x +  size.width,
        bottom = offset.y + size.height,
    )

    fun overlaps(other: Rectangle): Boolean {
        return (left < other.right) and
            (other.left < right) and
            (top < other.bottom) and
            (other.top < bottom)
    }
}