package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawableView extends View {

    private float x, y;
    private int viewWidth, viewHeight;
    private Paint currentPaint;
    private static Path path;
    private Bitmap newShapeBitmap;
    private static boolean isErase;
    private ArrayList<Path> paths;
    private ArrayList<Paint> paints;
    private String name;

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){

        paints = new ArrayList<>();

        currentPaint = new Paint();
        currentPaint.setColor(Color.RED);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeWidth(10.0f);

        paths = new ArrayList<>();
        path = new Path();

        name = "";
        isErase = false;

    }

    public Bitmap createImage(int width, int height) {
        CreatedPaint paint = CreatedPaint.getInstance();
        if(isErase == true){
            System.out.println(isErase);
            currentPaint.setColor(Color.WHITE);
            currentPaint.setStrokeWidth(paint.getEraseWidth());
            isErase = false;
        }else {
            System.out.println(isErase);
            currentPaint.setColor(paint.getColor());
            currentPaint.setStrokeWidth(paint.getLinewidth());
        }
        Canvas canvas = new Canvas(newShapeBitmap);
        for(int i = paths.size() - 1; i >= 0; i--){
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        return newShapeBitmap;
    }

    public void clearCanvas() {
        // reset canvas to blank
        newShapeBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
        path.reset();
        invalidate();
    }

    public void saveShape() {
        // save shape to singleton
        CreatedShapes shapes = CreatedShapes.getInstance();
        shapes.addShape(name, newShapeBitmap);

        // reset canvas to blank
        clearCanvas();
    }

    public static void setErase() {
        isErase = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        newShapeBitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = createImage(600, 600);
        canvas.drawBitmap(bitmap, 0, 0, null);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                path.lineTo(x, y);
                path.moveTo(x, y);
                paths.add(path);
                paints.add(currentPaint);
                path.reset();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                path.lineTo(x, y);
                path.moveTo(x, y);
                invalidate();
                break;
        }
        return true;
    }


}
