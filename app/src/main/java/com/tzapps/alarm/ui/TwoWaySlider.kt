package com.tzapps.alarm.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import com.tzapps.alarm.R


///inspired by https://github.com/arunk31/TwowaySlider/blob/master/app/src/main/java/in/arunkrishnamurthy/twowayslider/TwowaySliderView.java

class TwoWaySlider: View {

    interface TwoWaySliderListener {
        fun onSlideLeft()
        fun onSlideRight()
        fun onLongPress()
    }

    private var measuredLocalWidth = 0
    private var measuredLocalHeight = 0
    private var density = -1f
    var listener: TwoWaySliderListener? = null
    private lateinit var bgPaint: Paint
    private lateinit var sliderPaint: Paint
    private var rx = -1f
    private var ry = -1f //corner radius
    private lateinit var roundedRectPath: Path
    private var sliderImg = 0
    @DrawableRes var leftImg = 0
    @DrawableRes var rightImg = 0
    var imgTop = -1f
    var eventX = -1f
    var eventY = -1f
    var radius = -1f
    var X_MIN = Float.MIN_VALUE
    var X_MAX = Float.MAX_VALUE
    private var ignoreTouchEvent = true
    private var cancelOnYExit = false
    private var useDefaultCornerRadiusX= true
    private var useDefaultCornerRadiusY = true
    private var noSliderImage = false
    private var noLeftImage = false
    private var noRightImage = false

    var bgColor = (0xff807b7b).toInt()
    var sliderColor = (0xaa404040).toInt()
    var fillCircle = false


    constructor(context: Context): super(context) {
        init(null,0)
    }
    constructor(context: Context, attributeSet: AttributeSet): super(context,attributeSet,0) {
        init(attributeSet,0)
    }
    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr: Int): super(context,attributeSet,defStyleAttr) {
        init(attributeSet,defStyleAttr)
    }

    fun init(attrs: AttributeSet?, style: Int) {
        density = resources.displayMetrics.density
        val a = context.obtainStyledAttributes(attrs, R.styleable.TwoWaySlider)
        rx = a.getDimension(R.styleable.TwoWaySlider_cornerRadiusX,rx)
        useDefaultCornerRadiusX = rx==0f
        ry = a.getDimension(R.styleable.TwoWaySlider_cornerRadiusY,ry)
        useDefaultCornerRadiusY = ry==0f
        bgColor = a.getColor(R.styleable.TwoWaySlider_sliderBackgroundColor,bgColor)
        sliderColor = a.getColor(R.styleable.TwoWaySlider_sliderColor,sliderColor)
        fillCircle = a.getBoolean(R.styleable.TwoWaySlider_fillCircle,fillCircle)
        sliderImg = a.getResourceId(R.styleable.TwoWaySlider_sliderImage,sliderImg)
        noSliderImage = sliderImg==0
        leftImg = a.getResourceId(R.styleable.TwoWaySlider_leftImage,leftImg)
        noLeftImage = leftImg==0
        rightImg = a.getResourceId(R.styleable.TwoWaySlider_rightImage,rightImg)
        noRightImage = rightImg==0
        cancelOnYExit = a.getBoolean(R.styleable.TwoWaySlider_cancelOnYExit,cancelOnYExit)
        a.recycle()

        roundedRectPath = Path()
        bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bgPaint.style = Paint.Style.FILL
        bgPaint.color = bgColor
        sliderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        sliderPaint.style = if (fillCircle) Paint.Style.FILL_AND_STROKE else Paint.Style.STROKE
        sliderPaint.color = sliderColor
        sliderPaint.strokeWidth = 2*density
        if (!isInEditMode) {
            //val dir = floatArrayOf(0f,-1f,0.5f)
            sliderPaint.maskFilter = BlurMaskFilter(1f, BlurMaskFilter.Blur.OUTER)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measuredLocalHeight = getDefaultSize(suggestedMinimumHeight,heightMeasureSpec)
        measuredLocalWidth = getDefaultSize(suggestedMinimumWidth,widthMeasureSpec)
        if (useDefaultCornerRadiusX) rx = measuredLocalHeight*0.52f
        if (useDefaultCornerRadiusX) ry = measuredLocalHeight*0.52f
        radius = measuredLocalHeight*0.38f
        X_MIN = 1.2f*radius
        X_MAX = measuredWidth-X_MIN
        x = measuredLocalWidth*0.5f
        imgTop = measuredLocalHeight*0.25f
        setMeasuredDimension(measuredLocalWidth,measuredLocalHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (measuredLocalHeight<=0||measuredLocalWidth<=0) return
        canvas?.drawRoundRect(0f,0f,measuredLocalWidth.toFloat(),measuredLocalHeight.toFloat(),rx,ry,bgPaint)
        if (!noLeftImage) {
            var bitmap = BitmapFactory.decodeResource(resources,leftImg)
            bitmap = Bitmap.createScaledBitmap(bitmap, (measuredLocalHeight*0.6f).toInt(),(measuredLocalHeight*0.6f).toInt(),true)
            val cx = bitmap.width*0.25f
            val cy = (measuredLocalHeight-bitmap.height)*0.5f
            canvas?.drawBitmap(bitmap,cx,cy,null)
        }
        if (!noRightImage) {
            var bitmap = BitmapFactory.decodeResource(resources,rightImg)
            bitmap = Bitmap.createScaledBitmap(bitmap, (measuredLocalHeight*0.6f).toInt(),(measuredLocalHeight*0.6f).toInt(),true)
            val cx = bitmap.width*1.25f
            val cy = (measuredLocalHeight-bitmap.height)*0.5f
            canvas?.drawBitmap(bitmap,cx,cy,null)
        }
        canvas?.drawCircle(x,measuredLocalHeight*0.5f,radius*1.5f,sliderPaint)
        if (!noSliderImage) {
            var bitmap = BitmapFactory.decodeResource(resources,rightImg)
            bitmap = Bitmap.createScaledBitmap(bitmap, (radius*1.5f).toInt(),(radius*1.5f).toInt(),true)
            val cx = bitmap.width*0.75f
            val cy = (measuredLocalHeight-bitmap.height)*0.5f
            canvas?.drawBitmap(bitmap,cx,cy,null)
        }
    }

    private fun drawRoundedRect(c: Canvas) {
        roundedRectPath.apply {
            reset()
            moveTo(rx,0f)
            lineTo(measuredLocalWidth-rx,0f)
            quadTo(measuredLocalWidth.toFloat(),0f,measuredLocalWidth.toFloat(),ry)
            lineTo(measuredLocalWidth.toFloat(),measuredLocalHeight-ry)
            quadTo(measuredLocalWidth.toFloat(),measuredLocalHeight.toFloat(),measuredLocalWidth-rx,measuredLocalHeight.toFloat())
            lineTo(rx,measuredLocalHeight.toFloat())
            quadTo(0f,measuredLocalHeight.toFloat(),0f,measuredLocalHeight.toFloat()-ry)
            lineTo(0f,ry)
            quadTo(0f,0f,rx,0f)
        }
        c.drawPath(roundedRectPath,bgPaint)
    }

    private fun onSlideLeft() {
        listener?.onSlideLeft()
    }

    private fun onSlideRight() {
        listener?.onSlideRight()
    }

    private fun reset() {
        radius = measuredLocalHeight*0.3f
        x = measuredLocalHeight*0.5f
        invalidate()
    }

    private fun onLongPress() {
        listener?.onLongPress()
    }


}