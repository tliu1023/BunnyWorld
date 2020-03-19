package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class PageView extends View {

    private ArrayList<Shape> shapes;
    private HashMap<String, Page> pageMap;
    private String pageName;
    private Shape selectedShape;
    private Context context;
    private int bgId;
    private Bitmap bgImage;
    private float x1, x2, y1, y2;
    private float pageWidth, pageHeight;

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println(mySingleton.getInstance().getDocStored().toString());
        this.pageName = mySingleton.getInstance().getDocStored().getCurPage().getName();
        this.selectedShape = mySingleton.getInstance().getDocStored().getCurPage().getSelectedShape();
        this.pageMap = mySingleton.getInstance().getDocStored().getPageDict();
        this.shapes = pageMap.get(pageName).getShapes();
        this.bgId = mySingleton.getInstance().getDocStored().getCurPage().getBackgroundId();
        this.context = getContext();
        this.bgImage = getImage(bgId);

        x1 = 0;
        x2 = 0;
        y1 = 0;
        y2 = 0;
        pageWidth = 0;
        pageHeight = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shapes = pageMap.get(pageName).getShapes();

        // draw background
        canvas.drawBitmap(bgImage,null,new RectF(0, 0, pageWidth, pageHeight),null);
        // canvas.setBitmap(bgImage);
        for (Shape shape: shapes) {
            shape.drawPic(canvas, context);
        }
    }


    @Override
    protected void onSizeChanged(int newW, int newH, int oldW, int oldH) {
        super.onSizeChanged(newW, newH, oldW, oldH);
        pageWidth = newW;
        pageHeight = newH;
        // yLine = 0.8f * pageHeight;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(event);
                break;
        }
        return true;
    }

    private void onTouchDown(MotionEvent event) {
        x1 = event.getX();
        y1 = event.getY();
        Docs d = mySingleton.getInstance().getDocStored();
        Page p = d.getCurPage();
        if (selectedShape != null) {
            if (isSelected(selectedShape, x1, y1)) {
                return;
            }
            // deselect when clicking somewhere else
            else {
                selectedShape.setSelected(false);
                selectedShape = null;
                p.setSelectedShape(null);
                d.setCurPage(p);
                mySingleton.getInstance().setDocStored(d);
            }
        }
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (isSelected(shape, x1, y1)) {
                selectedShape = shape;
                selectedShape.setSelected(true);
                p.setSelectedShape(selectedShape);
                d.setCurPage(p);
                mySingleton.getInstance().setDocStored(d);
                break;
            }
        }

        invalidate();
    }

    private void onTouchMove(MotionEvent event) {
        if (selectedShape == null) {
            return;
        }
        x2 = event.getX();
        y2 = event.getY();

        shapes.remove(selectedShape);

        selectedShape.setxCoor(selectedShape.getxCoor() + x2 - x1);
        selectedShape.setyCoor(selectedShape.getyCoor() + y2 - y1);
        x1 = x2;
        y1 = y2;

        shapes.add(selectedShape);
        invalidate();
    }

    private void onTouchUp(MotionEvent event) {
        if (selectedShape == null) {
            return;
        }
        x2 = event.getX();
        y2 = event.getY();

        selectedShape.setxCoor(selectedShape.getxCoor() + x2 - x1);
        selectedShape.setyCoor(selectedShape.getyCoor() + y2 - y1);

        x1 = x2;
        y1 = y2;

        float shapeLeft = selectedShape.getxCoor();
        float shapeUp = selectedShape.getyCoor();
        float shapeRight = selectedShape.getxCoor() + selectedShape.getWidth();
        float shapeBottom = selectedShape.getyCoor() + selectedShape.getHeight();

        // if the shape is moved outside of the boundary
        if (shapeUp < 0) {
            selectedShape.setyCoor(0);
        }
        if (shapeLeft < 0) {
            selectedShape.setxCoor(0);
        }
        if (shapeBottom > pageHeight) {
            selectedShape.setyCoor(pageHeight - selectedShape.getHeight());
        }
        if (shapeRight > pageWidth) {
            selectedShape.setyCoor(pageWidth - selectedShape.getWidth());
        }
        invalidate();
    }

    private boolean isSelected(Shape shape, float x, float y) {
        // check whether the current location is in a shape
        if (x >= shape.getxCoor() && x <= shape.getxCoor()+shape.getWidth() && y >= shape.getyCoor() && y <= shape.getyCoor()+shape.getHeight()) {
            return true;
        }
        return false;
    }

    public Bitmap getImage(int bgId) {
//        String imgName = imgNames.get(bgId);
//        int resId = getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getContext().getResources().getDrawable(bgId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap;
    }
}