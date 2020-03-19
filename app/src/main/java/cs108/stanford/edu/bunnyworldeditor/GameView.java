package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class GameView extends View {
    GameActivity gameActivity;
    private MediaPlayer mediaPlayer;

    private Map<String, Page> pageMap;
    private Map<String, Integer> soundMap;

    private Page currPage;

    private Shape selectedShape;
    private Shape currOnDropShape;
    private Shape prevOnDropShape;

    // diffX and diffY represent the difference between coordinates of the actual click and shape's upper left corner
    private float diffX;
    private float diffY;

    private boolean shapeIsSelected;
    private boolean shapeIsDragged;


    private Paint selectExterior;
    private Paint dropExterior;
    private Paint possessionPaint;

    private Possessions possessions;

    protected static boolean isWin;



    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameActivity = new GameActivity();

        pageMap = new HashMap<>();
        soundMap = new HashMap<>();


        shapeIsSelected = false;
        shapeIsDragged = false;


        selectExterior = new Paint();
        selectExterior.setColor(Color.rgb(0,0,255));
        selectExterior.setStyle(Paint.Style.STROKE);
        selectExterior.setStrokeWidth(2.0f);

        dropExterior = new Paint();
        dropExterior.setColor(Color.rgb(0,255,0));
        dropExterior.setStyle(Paint.Style.STROKE);
        dropExterior.setStrokeWidth(2.0f);


        possessionPaint = new Paint();
        possessionPaint.setColor(Color.WHITE);
        possessionPaint.setStyle(Paint.Style.FILL);

        isWin = false;
        initializeSounds();

        possessions = new Possessions(700, 900, 0, 2000);

    }



    public boolean getIsWin() {
        if (currPage != null) {
            Shape winShape = currPage.getShapeByName("win");
            if (winShape != null && winShape.isVisible()) {
                gameActivity.stopTimer();
                return true;
            }
        }
        return false;
    }


    public void drawPossessionArea(Canvas canvas, Context context) {
        canvas.drawRect(possessions.getLeft(), possessions.getTop(), possessions.getRight(), possessions.getBottom(), possessionPaint);

        float upperLeftX = possessions.getLeft();
        float upperLeftY = possessions.getTop();
        for (int i = 0; i < possessions.getShapeList().size(); i++) {

            Shape shape = possessions.getShapeList().get(i);

            if (i > 0) {
                Shape prevShape = possessions.getShapeList().get(i - 1);
                upperLeftX += prevShape.getWidth();
            }

            shape.changeCoordinate(upperLeftX, upperLeftY);


            shape.drawPicPlay(canvas, context);
        }
    }


    private void drawSelectExterior(Canvas canvas) {
        if (selectedShape != null) {
            float left = selectedShape.getxCoor();
            float top = selectedShape.getyCoor();
            float right = left + selectedShape.getWidth();
            float bottom = top + selectedShape.getHeight();
            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawRect(rectF, selectExterior);
        }
    }




    private void drawDropExteriors(Canvas canvas) {
        if (selectedShape != null) {
            for (Shape shape : currPage.shapes) {
                if (shape.isVisible() && checkDropValidity(selectedShape, shape)) {
                    float left = shape.getxCoor();
                    float top = shape.getyCoor();
                    float right = left + shape.getWidth();
                    float bottom = top + shape.getHeight();
                    RectF rectF = new RectF(left, top, right, bottom);
                    canvas.drawRect(rectF, dropExterior);
                }
            }

            for (Shape shape : possessions.getShapeList()) {
                if (shape.isVisible() && checkDropValidity(selectedShape, shape)) {
                    float left = shape.getxCoor();
                    float top = shape.getyCoor();
                    float right = left + shape.getWidth();
                    float bottom = top + shape.getHeight();
                    RectF rectF = new RectF(left, top, right, bottom);
                    canvas.drawRect(rectF, dropExterior);
                }
            }
        }
    }


    public Bitmap getImage(int bgId) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(bgId);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currPage.name.equals("losePage")) {
            Bitmap loseBitMapDrawabe = BitmapFactory.decodeResource(getResources(),R.drawable.lose);
            canvas.drawBitmap(loseBitMapDrawabe,null,new RectF(0, 0, 1800, 700),null);
            return;
        }


        Bitmap bgImage = getImage(currPage.backgroundId);
        canvas.drawBitmap(bgImage,null,new RectF(0, 0, 1800, 700),null);

        currPage.drawPagePlay(canvas, getContext());
        drawPossessionArea(canvas, getContext());

        if (selectedShape != null) {
            selectedShape.drawPicPlay(canvas, getContext());
            drawSelectExterior(canvas);
            drawDropExteriors(canvas);
        }
    }

    public void initializePages(List<Page> pagesList) {
        if (pagesList != null && pagesList.size() > 0) {
            for (Page page : pagesList) {
                pageMap.put(page.getName(), page);

                if (page.getName().equals("Page1") || page.getName().equals("page1")) {
                    currPage = page;
                    performEnterAction();
                    isWin = getIsWin();
                }
            }
        }

        Page losePage = new Page("losePage");
        pageMap.put(losePage.getName(), losePage);


        invalidate();
    }


    private void initializeSounds() {
        soundMap.put("carrotcarrotcarrot", R.raw.carrotcarrotcarrot);
        soundMap.put("evillaugh", R.raw.evillaugh);
        soundMap.put("fire", R.raw.fire);
        soundMap.put("hooray", R.raw.hooray);
        soundMap.put("munch", R.raw.munch);
        soundMap.put("munching", R.raw.munching);
        soundMap.put("woof", R.raw.woof);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                actionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
        }
        return true;
    }


    private void actionDown(MotionEvent event) {
        // Get the coordinate of this action down
        float currX = event.getX();
        float currY = event.getY();


        // If this coordinate is in Page, get the shape from page. Otherwise, get the shape from possessions
        // If the shape is not null, remove it from the area it belongs
        if(!possessions.isInsidePossessionsArea(currX, currY)) {
            selectedShape = currPage.getShapeByLoc(currX, currY);
            if (selectedShape != null && selectedShape.isClickable()) {

                // Highlight the exteriors of all possible shapes that the selectedShape can be dropped on
                for (Shape receiverShape : currPage.shapes) {
                    receiverShape.setDropValidity(checkDropValidity(selectedShape, receiverShape));
                }
                for (Shape receiverShape : possessions.getShapeList()) {
                    receiverShape.setDropValidity(checkDropValidity(selectedShape, receiverShape));
                }


                diffX = currX - selectedShape.getxCoor();
                diffY = currY - selectedShape.getyCoor();
                shapeIsSelected = true;
                currPage.removeShape(selectedShape);
            }
        } else {
            selectedShape = possessions.getShapeByLoc(currX, currY);
            if (selectedShape != null) {

                // Highlight the exteriors of all possible shapes that the selectedShape can be dropped on
                for (Shape receiverShape : currPage.shapes) {
                    receiverShape.setDropValidity(checkDropValidity(selectedShape, receiverShape));
                }
                for (Shape receiverShape : possessions.getShapeList()) {
                    receiverShape.setDropValidity(checkDropValidity(selectedShape, receiverShape));
                }


                diffX = currX - selectedShape.getxCoor();
                diffY = currY - selectedShape.getyCoor();
                shapeIsSelected = true;

                possessions.removeFromPossessions(selectedShape);
            }
        }

    }

    private boolean checkDropValidity(Shape from, Shape to) {
        String selectedScript = from.getScript();
        for(String script : selectedScript.split(";")) {
            String[] scriptArray = script.split(" ");
            // scriptArray[0] is always "on"
            if (scriptArray.length >= 4) {
                if (scriptArray[1].equals("drop") && scriptArray[2].equals(to.getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void actionMove(MotionEvent event) {
        if (shapeIsSelected) {
            shapeIsDragged = true;
            if (selectedShape.isMovable()) {

                selectedShape.changeCoordinate(event.getX() - diffX, event.getY() - diffY);

                // Based on the coordinate, get the corresponding shape at currPage. This shape is what the selected shape will drop onto.
                currOnDropShape = currPage.getShapeByLoc(event.getX(), event.getY());
                if (currOnDropShape == null) {
                    currOnDropShape = possessions.getShapeByLoc(event.getX(), event.getY());
                }


                // Check whether we can drop selectedShape onto currOnDropShape
                if(currOnDropShape != null) {
                    boolean dropValidity = checkDropValidity(selectedShape, currOnDropShape);
                    currOnDropShape.setDropAvailability(true);
                    currOnDropShape.setDropValidity(dropValidity);
                }


                // Set the drop availability of prevOnDropShape to be false;
                if(prevOnDropShape != null) {
                    prevOnDropShape.setDropAvailability(false);
                }


                // Record the currOnDropShape as prevOnDropShape
                prevOnDropShape = currOnDropShape;


                invalidate();
            }
        }
    }


    private void actionUp(MotionEvent event) {
        if (shapeIsSelected) {
            if(shapeIsDragged && selectedShape.isMovable()) {
                if (currOnDropShape != null && currOnDropShape.isVisible() && checkDropValidity(selectedShape, currOnDropShape)) {

                    if (!currOnDropShape.getIsInPossession()) {
                        currPage.addShape(selectedShape);
                        selectedShape.setIsInPossesion(false);
                    } else {
                        possessions.addToPossessions(selectedShape);
                        selectedShape.setIsInPossesion(true);
                    }

                    // Perform "on drop" actions
                    String selectedScript = selectedShape.getScript();
                    for (String script : selectedScript.split(";")) {
                        String[] scriptArray = script.split(" ");
                        if (scriptArray.length >= 4) {
                            if (scriptArray[1].equals("drop") && scriptArray[2].equals(currOnDropShape.getId())) {
                                for (int i = 3; i < scriptArray.length - 1; i += 2) {
                                    String action = scriptArray[i];
                                    String target = scriptArray[i + 1];
                                    performAction(action, target);
                                }
                            }
                        }
                    }


                } else {

                    // If the lower bound of the shape is below possession area, put this shape into possession area.
                    // Otherwise, put this shape into page area.
                    if (event.getY() - diffY + selectedShape.getHeight() > possessions.getTop()) {
                        possessions.addToPossessions(selectedShape);
                        selectedShape.setIsInPossesion(true);
                    } else {
                        currPage.addShape(selectedShape);
                        selectedShape.setIsInPossesion(false);
                    }
                }

            } else if(!shapeIsDragged) {
                // Perform "on click" actions
                if (!selectedShape.getIsInPossession()) {
                    currPage.addShape(selectedShape);
                    selectedShape.setIsInPossesion(false);
                    String selectedScript = selectedShape.getScript();
                    for(String script : selectedScript.split(";")) {
                        String[] scriptArray = script.split(" ");
                        if (scriptArray.length >= 4) {
                            if (scriptArray[1].equals("click")) {
                                for (int i = 2; i < scriptArray.length - 1; i += 2) {
                                    String action = scriptArray[i];
                                    String target = scriptArray[i + 1];
                                    performAction(action, target);
                                }
                            }
                        }

                    }
                } else {
                    possessions.addToPossessions(selectedShape);
                    selectedShape.setIsInPossesion(true);
                }
            } else {
                // Shape is not movable, it is still where it was in the page.
                if (!selectedShape.getIsInPossession()) {
                    currPage.addShape(selectedShape);
                    selectedShape.setIsInPossesion(false);
                } else {
                    possessions.addToPossessions(selectedShape);
                    selectedShape.setIsInPossesion(true);
                }
            }

        }

        shapeIsDragged = false;
        shapeIsSelected = false;

        selectedShape = null;
        currOnDropShape = null;
        prevOnDropShape = null;

        for (Shape shape : currPage.shapes) {
            shape.setDropValidity(false);
        }
        for (Shape shape : possessions.getShapeList()) {
            shape.setDropValidity(false);
        }

        diffX = 0;
        diffY = 0;

        invalidate();
    }


    public void performEnterAction() {
        if (currPage != null) {

            if (currPage.name.equals("losePage")) {
                return;
            }


            List<Shape> shapes = currPage.shapes;
            for(Shape shape : shapes) {
                String selectedScript = shape.getScript();
                for (String script : selectedScript.split(";")) {
                    String[] scriptArray = script.split(" ");
                    if (scriptArray.length >= 4) {
                        if (scriptArray[1].equals("enter")) {
                            for (int i = 2; i < scriptArray.length - 1; i += 2) {
                                String action = scriptArray[i];
                                String target = scriptArray[i + 1];
                                performAction(action, target);
                            }
                        }
                    }
                }
            }
        }
    }


    public void performAction(String action, String target) {

        switch (action) {
            case "goto":
                if (pageMap.containsKey(target)) {
                    currPage = pageMap.get(target);
                    isWin = getIsWin();
                    performEnterAction();
                    invalidate();
                }
                break;
            case "play":
                if (soundMap.containsKey(target)) {
                    mediaPlayer = MediaPlayer.create(getContext(), soundMap.get(target));
                    mediaPlayer.start();
                }
                break;
            case "hide":

                Shape shapeToHide = currPage.getShapeByName(target);

                if (shapeToHide == null) {
                    for (Page page : pageMap.values()) {
                        if (!page.equals(currPage) && page.getShapeByName(target) != null) {
                            shapeToHide = page.getShapeByName(target);
                            break;
                        }
                    }
                }


                if (shapeToHide == null) {
                    shapeToHide = possessions.getShapeByName(target);
                }
                if (shapeToHide != null) {
                    shapeToHide.setVisible(false);
                    shapeToHide.setClickable(false);
                    invalidate();
                }
                break;
            case "show":
                Shape shapeToShow = currPage.getShapeByName(target);

                if (shapeToShow == null) {
                    for (Page page : pageMap.values()) {
                        if (!page.equals(currPage) && page.getShapeByName(target) != null) {
                            shapeToShow = page.getShapeByName(target);
                            break;
                        }
                    }
                }

                if (shapeToShow == null) {
                    shapeToShow = possessions.getShapeByName(target);
                }
                if (shapeToShow != null) {
                    isWin = getIsWin();
                    shapeToShow.setVisible(true);
                    shapeToShow.setClickable(true);
                    invalidate();
                }
                break;
        }
    }

}


