package com.aibeile_diaper.mm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aibeile_diaper.mm.activity.R;

public class Mychart extends View {

	 private int bgColor = Color.rgb(Integer.parseInt("4d", 16),
	            Integer.parseInt("af", 16), Integer.parseInt("ea", 16));// 整体的背景色

  /* private int oneRowFillColor = Color.rgb(Integer.parseInt("e7", 16),
            Integer.parseInt("e7", 16), Integer.parseInt("e9", 16));// 正常的背景色--绿色
    private int twoRowFillColor = Color.rgb(Integer.parseInt("e7", 16),
            Integer.parseInt("e7", 16), Integer.parseInt("e9", 16));// 低量的背景色--黄色

    private int threeRowFillColor = Color.rgb(Integer.parseInt("4d", 16),
            Integer.parseInt("af", 16), Integer.parseInt("ea", 16));//重症的背景色--红色
  */
	 private int oneRowFillColor = Color.rgb(Integer.parseInt("81", 16),
	            Integer.parseInt("c3", 16), Integer.parseInt("45", 16));// 正常的背景色--绿色
	    private int twoRowFillColor = Color.rgb(Integer.parseInt("fa", 16),Integer.parseInt("dc", 16), Integer.parseInt("00", 16));// 低量的背景色--黄色

	    private int threeRowFillColor = Color.rgb(Integer.parseInt("ec", 16),
	            Integer.parseInt("2b", 16), Integer.parseInt("4a", 16));// 浅红色;//重症的背景色--红色 

    private int xyLineColor = Color.rgb(Integer.parseInt("a9", 16),
            Integer.parseInt("d8", 16), Integer.parseInt("f5", 16));// 表格的线颜色

    private int chartLineColor = Color.WHITE;// 绘制趋势线的颜色

    private int shadowLineColor = Color.rgb(Integer.parseInt("1a", 16),
            Integer.parseInt("49", 16), Integer.parseInt("84", 16));// 趋势线阴影的颜色

    private String yUnit = "mol/L";// Y轴单位

    private boolean isDrawY = true;// 是否绘制Y轴

    private boolean isDrawX = true;// 是否绘制X轴

    private boolean isDrawInsideX = true;// 是否绘制内部的X轴

    private boolean isDrawInsedeY = true;// 是否绘制内部的Y轴

    private boolean isFillDown = false;// 是否填充点的下面部分

    private boolean isAppendX = true;// X轴是否向左突出一点

    private boolean isDemo = false;// 是否demo测试数据

    private int ScreenX;// view的宽度

    private int ScreenY;// view的高度

    private int numberOfX = 10;// 默认X轴放6个值

    private int numberOfY = 10;// 默认Y轴放5个值（越多显示的值越精细）

    private int paddingTop = 30;// 默认上下左右的padding

    private int paddingLeft = 70;// 默认上下左右的padding

    private int paddingRight = 30;// 默认上下左右的padding

    private int paddingDown = 50;// 默认上下左右的padding

    private int appendXLength = 10;// 向左X轴突出的长度

    private float maxNumber = 0;// Y轴最大值

    private List<List<Float>> pointList;// 传入的数据

    private List<Integer> bitmapList;// 传入的颜色值

    private List<Integer> lineColorList;

    private List<String> titleXList;// 传入的X轴标题

    private List<String> titleYList;// 计算得出的Y轴标题
    private List<String> rangeYList;//传入的Y轴的颜色

    public Mychart(Context context) {
        super(context);
   //     demo();

    }

    public Mychart(Context context, AttributeSet attr) {
        super(context, attr);
    //    demo();
    }

    private void demo() {
        if (!isDemo) {
            return;
        }
        numberOfX = 6;// 默认X轴放6个值
        numberOfY = 6;// 默认Y轴放5个值（越多显示的值越精细）
        pointList = new ArrayList<List<Float>>();
        titleXList = new ArrayList<String>();
        titleYList = new ArrayList<String>();
    	lineColorList = new ArrayList<Integer>();
        lineColorList.add(Color.WHITE);
  //      lineColorList.add(Color.GREEN);
  //      lineColorList.add(Color.YELLOW);
        // TODO 测试//titlexlist与point数量一致，否则报错,titleylist数量与numofy
            List<Float> pointInList = new ArrayList<Float>();
            for (int j = 0; j < 6; j++) { 
                titleXList.add("12." + j + "1");
            }
            pointInList.add(0f);
            pointInList.add(0.5f);
            pointInList.add(1.5f);
            pointInList.add(3.0f);
            pointInList.add(4.0f);
            pointInList.add(8.0f);
            
            titleYList.add("0");
            titleYList.add("0.5");
            titleYList.add("1.5");
            titleYList.add("3.0");
            titleYList.add("4.0");
            titleYList.add("8.0");
            pointList.add(pointInList);
    }

    /**
     * 计算得出View的宽高
     * 
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredHeight = measureHeight(heightMeasureSpec);

        int measuredWidth = measureWidth(widthMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);

        ScreenX = measuredWidth;

        ScreenY = measuredHeight;

    }

    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 300;
        if (specMode == MeasureSpec.AT_MOST) {

            result = specSize;
        }
        else if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;
        }

        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 450;
        if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        }

        else if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;
        }

        return result;
    }

    /**
     * 绘画View方法
     * 
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        maxNumber = Float.parseFloat(titleYList.get(titleYList.size()-1));
        List<Point> listX = initNumberOfX();// 计算出X轴平均后的坐标
        List<Point> listY = initNumberOfY(titleYList);// 计算出Y轴平均后的坐标
        canvas.drawColor(bgColor);// 背景色
        fillColor(listY,rangeYList, canvas);// 根据需求，对每一个框做不同的填充颜色

        // 画背景表格
        Paint paint = new Paint();
        paint.setColor(xyLineColor);// //表格线颜色
        if (isDrawX) {
            int appendX = 0;
            if (isAppendX) {
                appendX = appendXLength;
            }
            canvas.drawLine(paddingLeft - appendX, paddingTop + listY.get(0).y, listY.get(0).x
                    + paddingLeft,
                    paddingTop + listY.get(0).y, paint);
        }
        if (isDrawY) {
            canvas.drawLine(listX.get(0).x, paddingTop, listX.get(0).x, listX.get(0).y + paddingTop
                    , paint);
        }
        if (isDrawInsedeY) {// 绘制纵向的
            for (Point point : listX) {
                if (!isDrawX) {
                    isDrawX = !isDrawX;
                    continue;
                }
                canvas.drawLine(point.x, paddingTop, point.x, point.y + paddingTop, paint);
            }
        }
        if (isDrawInsideX) {// 绘制横向的
            for (Point point : listY) {
                if (!isDrawY) {
                    isDrawY = !isDrawY;
                    continue;
                }
                int appendX = 0;
                if (isAppendX) {
                    appendX = appendXLength;
                }
                canvas.drawLine(paddingLeft - appendX, paddingTop + point.y, point.x + paddingLeft,
                        paddingTop + point.y, paint);
            }
        }

        setYTitle(listY, canvas);// 画折线图Y的单位，同时计算出最大的Y轴值

        List<List<Point>> positionList = countListPosition(listX);// 计算像素位置

        drawChart(canvas, positionList);// 画折线
        drawCicle(canvas, positionList);// 画点

        setXTitle(listX, canvas);// 画折线图X的单位

    }


    private void drawCicle(Canvas canvas, List<List<Point>> positionList) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
        // R.drawable.comm_chart_point);
        int resouceId;
        for (int i = 0; i < positionList.size(); i++) {
            // canvas.drawCircle(positionList.get(i).x, positionList.get(i).y,
            // 7, paint);

            if (bitmapList != null && bitmapList.get(i) != null) {
                resouceId = bitmapList.get(i);
            } else {
                resouceId = R.drawable.point;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    resouceId);
            for (int j = 0; j < positionList.get(i).size(); j++) {
                canvas.drawBitmap(bitmap, positionList.get(i).get(j).x + 0.5f - bitmap.getWidth()
                        / 2,
                        positionList.get(i).get(j).y + 0.5f - bitmap.getHeight() / 2, paint);
            }
        }
    }

    private List<List<Point>> countListPosition(List<Point> listX) {
        List<List<Point>> positionList = new ArrayList<List<Point>>();
        if (pointList == null) {
            pointList = new ArrayList<List<Float>>();
            List<Float> pointInList = new ArrayList<Float>();
            for (int i = 0; i < numberOfX; i++) {
                pointInList.add(0f);
            }
            pointList.add(pointInList);
        }
        for (int i = 0; i < pointList.size(); i++) {
            List<Point> positionInList = new ArrayList<Point>();
            for (int j = 0; j < pointList.get(i).size(); j++) {
                Point point = new Point();
                Float z = pointList.get(i).get(j);
                point.x = listX.get(j).x;
                point.y = listX.get(j).y + paddingTop
                        - (int) ((listX.get(j).y) * (float) z / (float) maxNumber);
                positionInList.add(point);
            }
            positionList.add(positionInList);
        }
        return positionList;
    }

    private void drawChart(Canvas canvas, List<List<Point>> positionList) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(chartLineColor);
        paint.setStrokeWidth(3);// 默认线宽为3，到时候提升到全局变量，用于设置
        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(shadowLineColor);
        shadowPaint.setStrokeWidth(1);// 默认线宽为3，到时候提升到全局变量，用于设置
        shadowPaint.setAlpha(178);
        for (int i = 0; i < positionList.size(); i++) {
            if (lineColorList != null && lineColorList.get(i) != null) {
                paint.setColor(lineColorList.get(i));
            }
            for (int j = 0; j < positionList.get(i).size() - 1; j++) {
                canvas.drawLine(positionList.get(i).get(j).x, positionList.get(i).get(j).y + 2,
                        positionList.get(i).get(j + 1).x, positionList.get(i).get(j + 1).y + 2,
                        shadowPaint);
                canvas.drawLine(positionList.get(i).get(j).x, positionList.get(i).get(j).y,
                        positionList.get(i).get(j + 1).x, positionList.get(i).get(j + 1).y, paint);
            }
        }
    }

    private void fillColor(List<Point> listY,List<String> rangeYList, Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        for (int i = 1; i < numberOfY - 1; i++) {
            if (rangeYList.get(i).toString().equals("正常")) {
                paint.setColor(oneRowFillColor);
             //   paint.setAlpha(102);
            } else if(rangeYList.get(i).toString().equals("低量")){
                paint.setColor(twoRowFillColor);
             //   paint.setAlpha(102);
            }
            else if(rangeYList.get(i).toString().equals("重症")){
                paint.setColor(threeRowFillColor);
             //   paint.setAlpha(102);
            }
            canvas.drawRect(paddingLeft,listY.get(i+1).y+paddingTop, ScreenX - paddingRight,listY.get(i).y+paddingTop,
                    paint);
        }
    }

    private void setYTitle(List<Point> listY, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
  /*      if (pointList == null) {
            titleYList = new ArrayList<String>();
            for (int i = 1; i <= numberOfY; i++) {
                titleYList.add(String.valueOf(100 / i));
            }
        } else {
            for (int i = 0; i < pointList.size(); i++) {
                for (int j = 0; j < pointList.get(i).size(); j++) {

                    if (pointList.get(i).get(j) > maxNumber) {
                        maxNumber = pointList.get(i).get(j);
                    }
                }
            }
            maxNumber = maxNumber + maxNumber / 3;
            titleYList = new ArrayList<String>();
            for (int i = 0; i < numberOfY; i++) {
                titleYList.add(String.valueOf((int) (0 + i * (maxNumber / (numberOfY - 1)))));
            }
        }*/
        for (int i = 0; i < numberOfY; i++) {
            int appendX = 0;
            if (isAppendX) {
                appendX = appendXLength;
            }
            if (i != numberOfY-1) {
                canvas.drawText(titleYList.get(i), paddingLeft - appendX - paddingLeft / 3,
                        paddingTop
                                + listY.get(i).y, paint);
            } else {
                canvas.drawText(titleYList.get(i) + yUnit,
                        paddingLeft - appendX - paddingLeft / 3, paddingTop
                                + listY.get(i).y, paint);
            }
        }
    }

    private void setXTitle(List<Point> listX, Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        if (titleXList == null) {
            titleXList = new ArrayList<String>();
            for (int i = 1; i <= numberOfX; i++) {
                titleXList.add("title" + i);
            }
        }
        for (int i = 0; i < numberOfX; i++) {
            canvas.save();
            canvas.rotate(30, listX.get(i).x,
                    listX.get(i).y + paddingTop + paddingDown / 2);
            canvas.drawText(titleXList.get(i), listX.get(i).x,
                    listX.get(i).y + paddingTop + paddingDown / 2
                    , paint);
            canvas.restore();
        }
    }

    private List<Point> initNumberOfX() {
        int num = (ScreenX - paddingLeft - paddingRight) / (numberOfX - 1);
        List<Point> list = new ArrayList<Point>();
        for (int i = 0; i < numberOfX; i++) {
            Point point = new Point();
            point.y = ScreenY - paddingDown - paddingTop;
            point.x = paddingLeft + num * i;
            list.add(point);
        }
        return list;
    }

    private List<Point> initNumberOfY(List<String> titleYList) {
    	 List<Point> list = new ArrayList<Point>();
        for (int j = 0; j < titleYList.size(); j++) {
            Point point = new Point();
            Float z = Float.parseFloat(titleYList.get(j));
            point.x = ScreenX - paddingLeft - paddingRight;
            point.y = (int) (ScreenY - paddingDown - paddingTop 
                    -  ((ScreenY - paddingDown - paddingTop) * (float) z / (float) maxNumber));
            list.add(point);
        }     
        return list;
    }

    public boolean isDrawY() {
        return isDrawY;
    }

    public void setDrawY(boolean isDrawY) {
        this.isDrawY = isDrawY;
    }

    public boolean isDrawX() {
        return isDrawX;
    }

    public void setDrawX(boolean isDrawX) {
        this.isDrawX = isDrawX;
    }

  
    public int getScreenX() {
        return ScreenX;
    }

    public void setScreenX(int screenX) {
        ScreenX = screenX;
    }

    public int getScreenY() {
        return ScreenY;
    }

    public void setScreenY(int screenY) {
        ScreenY = screenY;
    }

    public int getNumberOfX() {
        return numberOfX;
    }

    public void setNumberOfX(int numberOfX) {
        this.numberOfX = numberOfX;
    }

    public int getNumberOfY() {
        return numberOfY;
    }

    public void setNumberOfY(int numberOfY) {
        this.numberOfY = numberOfY;
    }

    public boolean isDrawInsideX() {
        return isDrawInsideX;
    }

    public void setDrawInsideX(boolean isDrawInsideX) {
        this.isDrawInsideX = isDrawInsideX;
    }

    public boolean isDrawInsedeY() {
        return isDrawInsedeY;
    }

    public void setDrawInsedeY(boolean isDrawInsedeY) {
        this.isDrawInsedeY = isDrawInsedeY;
    }

    public boolean isAppendX() {
        return isAppendX;
    }

    public void setAppendX(boolean isAppendX) {
        this.isAppendX = isAppendX;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingDown() {
        return paddingDown;
    }

    public void setPaddingDown(int paddingDown) {
        this.paddingDown = paddingDown;
    }

    public int getAppendXLength() {
        return appendXLength;
    }

    public void setAppendXLength(int appendXLength) {
        this.appendXLength = appendXLength;
    }

    public float getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(float maxNumber) {
        this.maxNumber = maxNumber;
    }

    public List<String> getTitleXList() {
        return titleXList;
    }

    public void setTitleXList(List<String> titleXList) {
        this.titleXList = titleXList;
    }

    public List<String> getRangeYList() {
        return rangeYList;
    }

    public void setRangeYList(List<String> rangeYList) {
        this.rangeYList = rangeYList;
    }
    public List<String> getTitleYList() {
        return titleYList;
    }

    public void setTitleYList(List<String> titleYList) {
        this.titleYList = titleYList;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getXyLineColor() {
        return xyLineColor;
    }

    public void setXyLineColor(int xyLineColor) {
        this.xyLineColor = xyLineColor;
    }

    public int getChartLineColor() {
        return chartLineColor;
    }

    public void setChartLineColor(int chartLineColor) {
        this.chartLineColor = chartLineColor;
    }

    public String getyUnit() {
        return yUnit;
    }

    public void setyUnit(String yUnit) {
        this.yUnit = yUnit;
    }

    public List<List<Float>> getPointList() {
        return pointList;
    }

    public void setPointList(List<List<Float>> pointList) {
        this.pointList = pointList;
    }

    public List<Integer> getBitmapList() {
        return bitmapList;
    }

    public void setBitmapList(List<Integer> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public List<Integer> getLineColorList() {
        return lineColorList;
    }

    public void setLineColorList(List<Integer> lineColorList) {
        this.lineColorList = lineColorList;
    }

}

