package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BasicCanvas extends View {

    private float top, left, bottom, right;
    private float x1, x2, y1, y2;
    private Paint redOutlinePaint;
    public static Paint blueOutlinePaint;
    private Paint whiteFillPaint;
    private int viewWidth, viewHeight;
    //public static LinkedList<GObject> shapeLists;
    //public static GObject currentShape;
    //public static GObject currentbackground;
    private int originalID;
    private boolean plotCurrent;

    public BasicCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        redOutlinePaint = new Paint();
        redOutlinePaint.setColor(Color.RED);
        redOutlinePaint.setStyle(Paint.Style.STROKE);
        redOutlinePaint.setStrokeWidth(5.0f);

        blueOutlinePaint = new Paint();
        blueOutlinePaint.setColor(Color.BLUE);
        blueOutlinePaint.setStyle(Paint.Style.STROKE);
        blueOutlinePaint.setStrokeWidth(15.0f);

        whiteFillPaint = new Paint();
        whiteFillPaint.setColor(Color.WHITE);


        originalID = -1;
        plotCurrent = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


    /*
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw all the shapes on canvas
        if(shapeLists.size() > 0){
            System.out.println(shapeLists.toString());
            for(int i = 0; i < shapeLists.size(); i++) {
                GObject e = shapeLists.get(i);
                switch (e.getShape()){
                    case R.id.RadioButton_Rect:
                        canvas.drawRect(e.getLeft(), e.getTop(),
                                e.getRight(), e.getBottom(), e.getPaint());
                        break;
                    case R.id.RadioButton_Oval:
                        RectF rectf = new RectF(e.getLeft(), e.getTop(),
                                e.getRight(), e.getBottom());
                        canvas.drawOval(rectf, e.getPaint());
                        break;
                }
            }
            canvas.save();

            // draw current boundary
            if(plotCurrent){
                if(currentShape.getShape() == R.id.RadioButton_Oval){
                    RectF rectf = new RectF(currentShape.getLeft(), currentShape.getTop(),
                            currentShape.getRight(), currentShape.getBottom());
                    canvas.drawOval(rectf, currentShape.getPaint());
                    canvas.drawOval(rectf, currentbackground.getPaint());
                }else{
                    canvas.drawRect(currentShape.getLeft(), currentShape.getTop(),
                            currentShape.getRight(), currentShape.getBottom(), currentShape.getPaint());
                    canvas.drawRect(currentShape.getLeft(), currentShape.getTop(),
                            currentShape.getRight(), currentShape.getBottom(), currentbackground.getPaint());
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                if (x1>x2) {
                    left = x2;
                    right = x1;
                } else {
                    left = x1;
                    right = x2;
                }

                if (y1>y2) {
                    top = y2;
                    bottom = y1;
                } else {
                    top = y1;
                    bottom = y2;
                }

                // get oval or rect
                CanvasOption option = CanvasOption.getInstance();
                int opt = option.getChoice();
                if(opt == R.id.RadioButton_Oval || opt == R.id.RadioButton_Rect){
                    plotCurrent = true;
                    if(left != right && top != bottom){
                        if(originalID == -1){
                            originalID = 0;
                        }else{
                            originalID = shapeLists.getLast().getId() + 1;
                        }
                        // push to the arrayList
                        shapeLists.add(new GObject(left, top, right, bottom,
                                redOutlinePaint, opt, originalID));
                        shapeLists.add(new GObject(left, top, right, bottom,
                                whiteFillPaint, opt, originalID));

                        currentShape.setTop(top);
                        currentShape.setBottom(bottom);
                        currentShape.setLeft(left);
                        currentShape.setRight(right);
                        currentShape.setShape(opt);
                        currentShape.setPaint(blueOutlinePaint);
                    }
                    // if select a shape
                }else if(opt == R.id.RadioButton_Select){
                    plotCurrent = true;
                    int potentialID = -1;
                    GObject currentObj = null;
                    if(left == right && top == bottom){
                        for(GObject e: shapeLists){
                            if(e.getLeft() <= left && e.getRight() >= left &&
                                    e.getBottom() >= top && e.getTop() <= top){
                                if(e.getId() > potentialID){
                                    potentialID = e.getId();
                                    currentObj = e;
                                }
                            }
                        }
                        if(currentObj == null){
                            currentShape.setPaint(redOutlinePaint);
                        }else{
                            // currentShape
                            currentShape.setTop(currentObj.getTop());
                            currentShape.setBottom(currentObj.getBottom());
                            currentShape.setLeft(currentObj.getLeft());
                            currentShape.setRight(currentObj.getRight());
                            currentShape.setId(currentObj.getId());
                            currentShape.setShape(currentObj.getShape());
                            currentShape.setPaint(blueOutlinePaint);

                            // background
                            currentbackground.setTop(currentObj.getTop());
                            currentbackground.setBottom(currentObj.getBottom());
                            currentbackground.setLeft(currentObj.getLeft());
                            currentbackground.setRight(currentObj.getRight());
                            currentbackground.setId(currentObj.getId());
                            currentbackground.setShape(currentObj.getShape());
                            currentbackground.setPaint(whiteFillPaint);

                            MovedShape ms = MovedShape.getInstance();
                            ms.setId(currentObj.getId());
                            ms.setShape(currentObj.getShape());

                            // set the editText
                            EditText text_x = (EditText) ((EditMain)
                                    getContext()).findViewById(R.id.EditText_x);
                            text_x.setText(Float.toString(left));
                            EditText text_y = (EditText) ((EditMain)
                                    getContext()).findViewById(R.id.EditText_y);
                            text_y.setText(Float.toString(bottom));
                            EditText text_width = (EditText) ((EditMain)
                                    getContext()).findViewById(R.id.EditText_width);
                            text_width.setText(Float.toString(currentObj.getRight()-currentObj.getLeft()));
                            EditText text_height = (EditText) ((EditMain)
                                    getContext()).findViewById(R.id.EditText_height);
                            text_height.setText(Float.toString(currentObj.getBottom()-currentObj.getTop()));
                        }
                    }
                }else if(opt == R.id.RadioButton_Erase){
                    int potentialID = -1;
                    GObject currentObj = null;
                    if(left == right && top == bottom){
                        for(GObject e: shapeLists){
                            if(e.getLeft() <= left && e.getRight() >= left &&
                                    e.getBottom() >= top && e.getTop() <= top){
                                if(e.getId() > potentialID){
                                    potentialID = e.getId();
                                    currentObj = e;
                                }
                            }
                        }
                        if(currentObj == null){
                            currentShape.setPaint(redOutlinePaint);
                        }else{
                            MovedShape ms = MovedShape.getInstance();
                            ms.setId(currentObj.getId());
                            ms.setShape(currentObj.getShape());

                            int deletedID = -1;

                            Iterator<GObject> itr = shapeLists.iterator();
                            while(itr.hasNext()){
                                GObject toDelete = itr.next();
                                if(toDelete.getId() == ms.getId()){
                                    deletedID = toDelete.getId();
                                    itr.remove();
                                }
                            }
                            if(deletedID == currentShape.getId()){
                                plotCurrent = false;
                            }
                            currentShape.setPaint(redOutlinePaint);

                            // reset originalID if the list is blank
                            if(shapeLists.size() == 0){
                                originalID = -1;
                            }
                        }
                    }
                }
                // update the canvas
                invalidate();
        }
        return true;
    }
    */
}
