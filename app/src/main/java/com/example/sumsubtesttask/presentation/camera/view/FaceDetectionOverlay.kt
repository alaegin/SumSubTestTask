package com.example.sumsubtesttask.presentation.camera.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.toRectF
import com.example.sumsubtesttask.R
import com.example.sumsubtesttask.domain.face_detection.model.DetectedFace

class FaceDetectionOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private var faceRectangles: List<RectF> = emptyList()

    private val boxPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
    }

    private var boxColor: Int = Color.WHITE
    private var cornerRadiusPx: Float = dpToPx(10F)
    private var strokeWidthPx: Float = dpToPx(2F)

    init {
        context.withStyledAttributes(
            attrs,
            R.styleable.FaceDetectionOverlay,
            defStyleAttr,
            0,
        ) {
            cornerRadiusPx = getDimensionPixelSize(
                R.styleable.FaceDetectionOverlay_boxCornerRadius,
                cornerRadiusPx.toInt(),
            ).toFloat()

            strokeWidthPx = getDimensionPixelSize(
                R.styleable.FaceDetectionOverlay_boxStrokeWidth,
                strokeWidthPx.toInt(),
            ).toFloat()

            boxColor = getColor(R.styleable.FaceDetectionOverlay_boxColor, boxColor)
        }

        updateValues()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        faceRectangles.forEach { rect ->
            canvas.drawRoundRect(rect, cornerRadiusPx, cornerRadiusPx, boxPaint)
        }
    }

    fun setFaces(faces: List<DetectedFace>) {
        val facesChanged = faces != this.faceRectangles
        this.faceRectangles = faces.map { it.rect.toRectF() }

        if (facesChanged && faces.isNotEmpty()) {
            invalidate()
        }
    }

    private fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private fun updateValues() {
        boxPaint.strokeWidth = strokeWidthPx
        boxPaint.color = boxColor
    }
}
