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
    // private ArrayList<Shape> possessionList;
    private String pageName = mySingleton.getInstance().docStored.curPage.name;
    private Shape selectedShape = mySingleton.getInstance().docStored.curPage.getSelectedShape();
    private Canvas canvas;
    private Context context;
    private int bgId = mySingleton.getInstance().docStored.curPage.backgroundId;
    private Bitmap bgImage = getImage(bgId);


    private float x1, x2, y1, y2;
    private float pageWidth, pageHeight;
    // private float yLine;

    // private Paint outlinePaint;

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        pageMap = mySingleton.getInstance().docStored.pageDict;
        shapes = pageMap.get(pageName).shapes;
        // possessionList = new ArrayList<> ();
        init(context);
        x1 = 0;
        x2 = 0;
        y1 = 0;
        y2 = 0;
        pageWidth = 0;
        pageHeight = 0;
    }

    private void init(Context context) {
        // preload resources
        this.context = context;
        // // define paints
        // outlinePaint = new Paint();
        // outlinePaint.setStyle(Paint.Style.STROKE);
        // outlinePaint.setStrokeWidth(4.0f);
        // outlinePaint.setColor(Color.GRAY);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shapes = pageMap.get(pageName).shapes;

        // draw background
        canvas.drawBitmap(bgImage,null,new RectF(0, 0, pageWidth, pageHeight),null);
        for (Shape shape: shapes) {
            shape.drawPic(canvas, context);
        }
        // drawYLine(canvas);
        // for (Shape shape: possessionList) {
        //     shape.drawPic(canvas, context);
        // }
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

//    private void drawYLine(Canvas canvas) {
//        canvas.drawLine(0.0f, yLine, pageWidth, yLine, outlinePaint);
//    }

    private void onTouchDown(MotionEvent event) {
        x1 = event.getX();
        y1 = event.getY();
        if (selectedShape != null) {
            if (isSelected(selectedShape, x1, y1)) {
                return;
            }
            // deselect when clicking somewhere else
            else {
                selectedShape.setSelected(false);
                selectedShape = null;
                mySingleton.getInstance().docStored.curPage.setSelectedShape(null);
            }
        }
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (isSelected(shape, x1, y1)) {
                selectedShape = shape;
                shape.setSelected(true);
                mySingleton.getInstance().docStored.curPage.setSelectedShape(selectedShape);
                break;
            }
        }

        // if (selectedShape != null) {
        //     if (!selectedShape.isMovable()) {
        //         selectedShape = null;
        //         selectedShape.setSelected(false);
        //         mySingleton.getInstance().docStored.curPage.setSelectedShape(null);
        //     }
        // }

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

        // // if the shape is moved to the possession list
        // float yLine = 0.8f * pageHeight;
        // if (shapeBottom >= yLine && shapeUp <= yLine) {
        //     // keep it in the possession list if at least half of the height is in it
        //     if (shapeBottom - yLine >= 0.5f * selectedShape.getHeight()) {
        //         selectedShape.setyCoor(0.8f * pageHeight + 2.0f);
        //         // resize the shape to fit in
        //         selectedShape.setHeight(pageHeight - 2.0f - selectedShape.getyCoor());
        //     }
        //     else {
        //         selectedShape.setyCoor(0.8f * pageHeight - 2.0f - selectedShape.getHeight());
        //     }
        // }

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

        // // check possession list
        // shapeBottom = selectedShape.getyCoor() + selectedShape.getHeight();
        // ArrayList<Shape> pageShapes = pageMap.get(pageName).shapes;
        // if (!possessionList.contains(selectedShape)) {
        //     if (selectedShape.getyCoor() >= 0.8f * pageHeight) {
        //         for (Shape shape : pageShapes) {
        //             if (shape.getId() == selectedShape.getId()) {
        //                 pageShapes.remove(shape);
        //                 break;
        //             }
        //         }
        //         possessionList.add(selectedShape);
        //     }
        // }
        // else {
        //     if (shapeBottom <= 0.8f * pageHeight) {
        //         pageShapes.add(selectedShape);
        //         possessionList.remove(selectedShape);
        //     }
        // }
        // selectedShape.setSelected(true);
        // mySingleton.getInstance().docStored.curPage.setSelectedShape(selectedShape);
        invalidate();
    }

    private boolean isSelected(Shape shape, float x, float y) {
        // check whether the current location is in a shape
        if (x >= shape.getxCoor() && x <= shape.getxCoor()+shape.getWidth() && y >= shape.getyCoor() && y <= shape.getyCoor()+shape.getHeight()) {
            return true;
        }
        return false;
    }


    private boolean isDroppable(Shape shape) {
        if (shape.getActionMap().containsKey("ondrop")) {
            return true;
        }
        return false;
    }

    public void changePageName(String newPageName) {
        pageName = newPageName;
    }

    public Bitmap getImage(int bgId) {
//        String imgName = imgNames.get(bgId);
//        int resId = getResources().getIdentifier(imgName, "drawable", context.getPackageName());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(bgId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap;
    }
}