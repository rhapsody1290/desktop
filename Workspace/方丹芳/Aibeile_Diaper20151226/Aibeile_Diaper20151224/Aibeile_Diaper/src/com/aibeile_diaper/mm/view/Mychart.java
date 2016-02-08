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
	            Integer.parseInt("af", 16), Integer.parseInt("ea", 16));// ����ı���ɫ

  /* private int oneRowFillColor = Color.rgb(Integer.parseInt("e7", 16),
            Integer.parseInt("e7", 16), Integer.parseInt("e9", 16));// �����ı���ɫ--��ɫ
    private int twoRowFillColor = Color.rgb(Integer.parseInt("e7", 16),
            Integer.parseInt("e7", 16), Integer.parseInt("e9", 16));// �����ı���ɫ--��ɫ

    private int threeRowFillColor = Color.rgb(Integer.parseInt("4d", 16),
            Integer.parseInt("af", 16), Integer.parseInt("ea", 16));//��֢�ı���ɫ--��ɫ
  */
	 private int oneRowFillColor = Color.rgb(Integer.parseInt("81", 16),
	            Integer.parseInt("c3", 16), Integer.parseInt("45", 16));// �����ı���ɫ--��ɫ
	    private int twoRowFillColor = Color.rgb(Integer.parseInt("fa", 16),Integer.parseInt("dc", 16), Integer.parseInt("00", 16));// �����ı���ɫ--��ɫ

	    private int threeRowFillColor = Color.rgb(Integer.parseInt("ec", 16),
	            Integer.parseInt("2b", 16), Integer.parseInt("4a", 16));// ǳ��ɫ;//��֢�ı���ɫ--��ɫ 

    private int xyLineColor = Color.rgb(Integer.parseInt("a9", 16),
            Integer.parseInt("d8", 16), Integer.parseInt("f5", 16));// ��������ɫ

    private int chartLineColor = Color.WHITE;// ���������ߵ���ɫ

    private int shadowLineColor = Color.rgb(Integer.parseInt("1a", 16),
            Integer.parseInt("49", 16), Integer.parseInt("84", 16));// ��������Ӱ����ɫ

    private String yUnit = "mol/L";// Y�ᵥλ

    private boolean isDrawY = true;// �Ƿ����Y��

    private boolean isDrawX = true;// �Ƿ����X��

    private boolean isDrawInsideX = true;// �Ƿ�����ڲ���X��

    private boolean isDrawInsedeY = true;// �Ƿ�����ڲ���Y��

    private boolean isFillDown = false;// �Ƿ���������沿��

    private boolean isAppendX = true;// X���Ƿ�����ͻ��һ��

    private boolean isDemo = false;// �Ƿ�demo��������

    private int ScreenX;// view�Ŀ��

    private int ScreenY;// view�ĸ߶�

    private int numberOfX = 10;// Ĭ��X���6��ֵ

    private int numberOfY = 10;// Ĭ��Y���5��ֵ��Խ����ʾ��ֵԽ��ϸ��

    private int paddingTop = 30;// Ĭ���������ҵ�padding

    private int paddingLeft = 70;// Ĭ���������ҵ�padding

    private int paddingRight = 30;// Ĭ���������ҵ�padding

    private int paddingDown = 50;// Ĭ���������ҵ�padding

    private int appendXLength = 10;// ����X��ͻ���ĳ���

    private float maxNumber = 0;// Y�����ֵ

    private List<List<Float>> pointList;// ���������

    private List<Integer> bitmapList;// �������ɫֵ

    private List<Integer> lineColorList;

    private List<String> titleXList;// �����X�����

    private List<String> titleYList;// ����ó���Y�����
    private List<String> rangeYList;//�����Y�����ɫ

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
        numberOfX = 6;// Ĭ��X���6��ֵ
        numberOfY = 6;// Ĭ��Y���5��ֵ��Խ����ʾ��ֵԽ��ϸ��
        pointList = new ArrayList<List<Float>>();
        titleXList = new ArrayList<String>();
        titleYList = new ArrayList<String>();
    	lineColorList = new ArrayList<Integer>();
        lineColorList.add(Color.WHITE);
  //      lineColorList.add(Color.GREEN);
  //      lineColorList.add(Color.YELLOW);
        // TODO ����//titlexlist��point����һ�£����򱨴�,titleylist������numofy
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
     * ����ó�View�Ŀ��
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
     * �滭View����
     * 
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        maxNumber = Float.parseFloat(titleYList.get(titleYList.size()-1));
        List<Point> listX = initNumberOfX();// �����X��ƽ���������
        List<Point> listY = initNumberOfY(titleYList);// �����Y��ƽ���������
        canvas.drawColor(bgColor);// ����ɫ
        fillColor(listY,rangeYList, canvas);// �������󣬶�ÿһ��������ͬ�������ɫ

        // ���������
        Paint paint = new Paint();
        paint.setColor(xyLineColor);// //�������ɫ
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
        if (isDrawInsedeY) {// ���������
            for (Point point : listX) {
                if (!isDrawX) {
                    isDrawX = !isDrawX;
                    continue;
                }
                canvas.drawLine(point.x, paddingTop, point.x, point.y + paddingTop, paint);
            }
        }
        if (isDrawInsideX) {// ���ƺ����
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

        setYTitle(listY, canvas);// ������ͼY�ĵ�λ��ͬʱ���������Y��ֵ

        List<List<Point>> positionList = countListPosition(listX);// ��������λ��

        drawChart(canvas, positionList);// ������
        drawCicle(canvas, positionList);// ����

        setXTitle(listX, canvas);// ������ͼX�ĵ�λ

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
        paint.setStrokeWidth(3);// Ĭ���߿�Ϊ3����ʱ��������ȫ�ֱ�������������
        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(shadowLineColor);
        shadowPaint.setStrokeWidth(1);// Ĭ���߿�Ϊ3����ʱ��������ȫ�ֱ�������������
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
            if (rangeYList.get(i).toString().equals("����")) {
                paint.setColor(oneRowFillColor);
             //   paint.setAlpha(102);
            } else if(rangeYList.get(i).toString().equals("����")){
                paint.setColor(twoRowFillColor);
             //   paint.setAlpha(102);
            }
            else if(rangeYList.get(i).toString().equals("��֢")){
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

