package com.example.android.clinicmanagement.barChart


import android.content.Context
import android.graphics.*
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.android.clinicmanagement.R
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.highlight.Range
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.ceil
import kotlin.math.roundToInt


class RoundedBarChart : BarChart {


    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    init {
        setRadius(15f)
        styleChart()
    }


    private fun styleChart() {
        val comfortaTypeFace: Typeface? = ResourcesCompat.getFont(context, R.font.nunito)
        val paint: Paint = getPaint(Chart.PAINT_INFO)

        paint.fontFeatureSettings
        paint.textSize = 25f
        paint.color = ContextCompat.getColor(context, R.color.blue_sky)
        paint.typeface = comfortaTypeFace

        setNoDataText(context.getString(R.string.chart_no_data_message))

        axisRight.isEnabled = false
        xAxis.apply {
            isGranularityEnabled = true
            granularity = 1f
            setDrawAxisLine(true)
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            typeface = comfortaTypeFace
        }
        axisLeft.apply {

            axisMinimum = 0f
            isEnabled = true
            setDrawAxisLine(true)
            setDrawGridLines(true)
            typeface = comfortaTypeFace
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value != axisMinimum) "${value.roundToInt()} DH" else ""

                }
            }
        }
        setDrawBarShadow(true)
        setFitBars(true)// make the x-axis fit exactly all bars
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        description = null
        legend.isEnabled = false
    }


    private fun setRadius(radius: Float) {
        renderer = RoundedBarChartRenderer(
            this,
            animator, viewPortHandler, radius
        )
    }

    private inner class RoundedBarChartRenderer internal constructor(
        chart: BarDataProvider?,
        animator: ChartAnimator?,
        viewPortHandler: ViewPortHandler?,
        private val mRadius: Float
    ) :
        BarChartRenderer(chart, animator, viewPortHandler) {
        private val mBarShadowRectBuffer: RectF = RectF()

        override fun drawHighlighted(c: Canvas, indices: Array<Highlight>) {
            val barData = mChart.barData
            for (high in indices) {
                val set = barData.getDataSetByIndex(high.dataSetIndex)
                if (set == null || !set.isHighlightEnabled) continue
                val e = set.getEntryForXValue(high.x, high.y)
                if (!isInBoundsX(e, set)) continue
                val trans: Transformer = mChart.getTransformer(set.axisDependency)
                mHighlightPaint.color = set.highLightColor
                mHighlightPaint.alpha = set.highLightAlpha
                val isStack = high.stackIndex >= 0 && e.isStacked
                val y1: Float
                val y2: Float
                if (isStack) {
                    if (mChart.isHighlightFullBarEnabled) {
                        y1 = e.positiveSum
                        y2 = -e.negativeSum
                    } else {
                        val range: Range = e.ranges[high.stackIndex]
                        y1 = range.from
                        y2 = range.to
                    }
                } else {
                    y1 = e.y
                    y2 = 0f
                }
                prepareBarHighlight(e.x, y1, y2, barData.barWidth / 2f, trans)
                setHighlightDrawPos(high, mBarRect)
                c.drawRoundRect(mBarRect, mRadius, mRadius, mHighlightPaint)
            }
        }


        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans: Transformer = mChart.getTransformer(dataSet.getAxisDependency())
            mBarBorderPaint.setColor(dataSet.getBarBorderColor())
            mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getBarBorderWidth()))
            mShadowPaint.color = dataSet.barShadowColor
            val drawBorder: Boolean = dataSet.getBarBorderWidth() > 0f
            val phaseX: Float = mAnimator.getPhaseX()
            val phaseY: Float = mAnimator.getPhaseY()

            // draw the bar shadow before the values
            if (mChart.isDrawBarShadowEnabled) {
                mShadowPaint.color = dataSet.barShadowColor
                val barData = mChart.barData
                val barWidth = barData.barWidth
                val barWidthHalf = barWidth / 2.0f
                var x: Float
                var i = 0
                val count = Math.min(
                    Math.ceil((dataSet.entryCount.toFloat() * phaseX).toDouble()).toInt(),
                    dataSet.entryCount
                )
                while (i < count) {
                    val e = dataSet.getEntryForIndex(i)
                    x = e.x
                    mBarShadowRectBuffer.left = x - barWidthHalf
                    mBarShadowRectBuffer.right = x + barWidthHalf
                    trans.rectValueToPixel(mBarShadowRectBuffer)
                    if (!mViewPortHandler.isInBoundsLeft(mBarShadowRectBuffer.right)) {
                        i++
                        continue
                    }
                    if (!mViewPortHandler.isInBoundsRight(mBarShadowRectBuffer.left)) break
                    mBarShadowRectBuffer.top = mViewPortHandler.contentTop()
                    mBarShadowRectBuffer.bottom = mViewPortHandler.contentBottom()

                    val barShadowPath: Path = roundRect(
                        RectF(
                            mBarShadowRectBuffer.left,
                            mBarShadowRectBuffer.top,
                            mBarShadowRectBuffer.right,
                            mBarShadowRectBuffer.bottom
                        ), mRadius, mRadius, true, true, false, false
                    )
                    c.drawPath(barShadowPath, mShadowPaint)
                    i++
                }
            }
            // initialize the buffer
            val buffer: BarBuffer = mBarBuffers.get(index)
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()))
            buffer.setBarWidth(mChart.getBarData().getBarWidth())
            buffer.feed(dataSet)
            trans.pointValuesToPixel(buffer.buffer)
            val isSingleColor = dataSet.getColors().size == 1
            if (isSingleColor) {
                mRenderPaint.setColor(dataSet.getColor())
            }
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer.get(j + 2))) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer.get(j))) break
                if (!isSingleColor) {
                    // Set the color for the currently drawn value. If the index
                    // is out of bounds, reuse colors.
                    mRenderPaint.setColor(dataSet.getColor(j / 4))
                }
                if (dataSet.getGradientColor() != null) {
                    val gradientColor: GradientColor = dataSet.getGradientColor()
                    mRenderPaint.setShader(
                        LinearGradient(
                            buffer.buffer.get(j),
                            buffer.buffer.get(j + 3),
                            buffer.buffer.get(j),
                            buffer.buffer.get(j + 1),
                            gradientColor.getStartColor(),
                            gradientColor.getEndColor(),
                            Shader.TileMode.MIRROR
                        )
                    )
                }
                if (dataSet.getGradientColors() != null) {
                    mRenderPaint.setShader(
                        LinearGradient(
                            buffer.buffer.get(j),
                            buffer.buffer.get(j + 3),
                            buffer.buffer.get(j),
                            buffer.buffer.get(j + 1),
                            dataSet.getGradientColor(j / 4).getStartColor(),
                            dataSet.getGradientColor(j / 4).getEndColor(),
                            Shader.TileMode.MIRROR
                        )
                    )
                }
                val path2: Path = roundRect(
                    RectF(
                        buffer.buffer.get(j), buffer.buffer.get(j + 1), buffer.buffer.get(j + 2),
                        buffer.buffer.get(j + 3)
                    ), mRadius, mRadius, true, true, false, false
                )

                c.drawPath(path2, mRenderPaint)

                if (drawBorder) {
                    val path: Path = roundRect(
                        RectF(
                            buffer.buffer.get(j),
                            buffer.buffer.get(j + 1),
                            buffer.buffer.get(j + 2),
                            buffer.buffer.get(j + 3)
                        ), mRadius, mRadius, true, true, false, false
                    )
                    c.drawPath(path, mBarBorderPaint)
                }
                j += 4
            }
        }

        private fun roundRect(
            rect: RectF,
            rx: Float,
            ry: Float,
            tl: Boolean,
            tr: Boolean,
            br: Boolean,
            bl: Boolean
        ): Path {
            var rx = rx
            var ry = ry
            val top: Float = rect.top
            val left: Float = rect.left
            val right: Float = rect.right
            val bottom: Float = rect.bottom
            val path = Path()
            if (rx < 0) rx = 0f
            if (ry < 0) ry = 0f
            val width = right - left
            val height = bottom - top
            if (rx > width / 2) rx = width / 2
            if (ry > height / 2) ry = height / 2
            val widthMinusCorners = width - 2 * rx
            val heightMinusCorners = height - 2 * ry
            path.moveTo(right, top + ry)
            if (tr) path.rQuadTo(0F, -ry, -rx, -ry) //top-right corner
            else {
                path.rLineTo(0F, -ry)
                path.rLineTo(-rx, 0F)
            }
            path.rLineTo(-widthMinusCorners, 0F)
            if (tl) path.rQuadTo(-rx, 0F, -rx, ry) //top-left corner
            else {
                path.rLineTo(-rx, 0F)
                path.rLineTo(0F, ry)
            }
            path.rLineTo(0F, heightMinusCorners)
            if (bl) path.rQuadTo(0F, ry, rx, ry) //bottom-left corner
            else {
                path.rLineTo(0F, ry)
                path.rLineTo(rx, 0F)
            }
            path.rLineTo(widthMinusCorners, 0F)
            if (br) path.rQuadTo(rx, 0F, rx, -ry) //bottom-right corner
            else {
                path.rLineTo(rx, 0F)
                path.rLineTo(0F, -ry)
            }
            path.rLineTo(0F, -heightMinusCorners)
            path.close() //Given close, last lineto can be removed.
            return path
        }
    }
}