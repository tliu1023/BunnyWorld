package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Shape implements Serializable {
    private final int UPPER_LEFT = 1;
    private final int UPPER_RIGHT = 2;
    private final int LOWER_LEFT = 3;
    private final int LOWER_RIGHT = 4;
    private final float minSize = 1;

    private boolean movable;
    private boolean visible;
    private String text;
    private float fontSize;
    private int fontColor;
    private String imgPath;
    private boolean clickable;
    private boolean active;
    private boolean selected;
    private String script;
    private Paint stokepaint = new Paint();
    private Paint greypaint = new Paint();
    private boolean toBeDroppedOn;
    private boolean canBeDroppedOn;
    private boolean isInPossession;
    Paint paintA = new Paint();
    Paint alphaTransparencyPaint = new Paint();
    public HashSet<String> relatedShapes;
    public String id;
    public float width;
    public float height;
    public float xCoor;
    public float yCoor;



    public Shape(String id, float width, float height, float x, float y) {
        stokepaint.setStyle(Paint.Style.STROKE);
        stokepaint.setStrokeWidth(5.0f);
        stokepaint.setColor(Color.BLUE);
        greypaint.setStyle(Paint.Style.STROKE);
        greypaint.setStrokeWidth(15.0f);
        greypaint.setColor(Color.argb(1,120,120,120));

        this.id = id;
        this.width = Math.max(width,minSize);
        this.height =  Math.max(height,minSize);
        this.xCoor = x;
        this.yCoor = y;

        movable = true;
        visible = true;
        fontSize = 14;
        fontColor = Color.BLACK;

        text = "";
        imgPath = "";
        selected = false;
        script = "";
        clickable = true;
        active = true;
        paintA.setAlpha(50);
        toBeDroppedOn = false;
        canBeDroppedOn = false;
        isInPossession = false;
        alphaTransparencyPaint.setAlpha(120);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = Math.max(width, minSize);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = Math.max(height, minSize);
    }

    public float getxCoor() {
        return xCoor;
    }

    public void setxCoor(float xCoor) {
        this.xCoor = xCoor;
    }

    public float getyCoor() {
        return yCoor;
    }

    public void setyCoor(float yCoor) {
        this.yCoor = yCoor;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isText() {
        return text.length() > 0;
    }

    public void setIsInPossesion(boolean isInPossession) {
        this.isInPossession = isInPossession;
    }

    public boolean getIsInPossession() {
        return isInPossession;
    }

    public void setDropAvailability(boolean dropAvailability) {
        this.toBeDroppedOn = dropAvailability;
    }

    public void setDropValidity(boolean dropValidity) {
        this.canBeDroppedOn = dropValidity;
    }

    public String getScript() {
        return this.script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public HashMap<String, ArrayList<String>> getActionMap() {
        HashMap<String, ArrayList<String>> actionMap = new HashMap<> ();
        String scripts = this.script;
        String[] ss = scripts.split(";");
        for (String s : ss) {
            String[] split = s.split(" ");
            String trigger = split[0] + split[1];
            ArrayList<String> actions = new ArrayList<String> ();
            if (split[1] == "drop") {
                trigger += " " + split[2];
                for (int i = 3; i < split.length; i++) {
                    if (i % 2 == 0) {
                        actions.add(split[i-1]+" "+split[i]);
                    }
                }
            }
            else {
                for (int i = 2; i < split.length; i++) {
                    if (i % 2 == 1) {
                        actions.add(split[i-1]+" "+split[i]);
                    }
                }
            }


            if (!actionMap.containsKey(trigger)) {
                actionMap.put(trigger, actions);
            }
            else {
                actionMap.get(trigger).addAll(actions);
            }
        }
        return actionMap;

    }

    public String getContext() {
        if (text.length()>0) return text;
        if (imgPath.length()>0) return imgPath;
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name:"+this.id);
        sb.append("|width:"+this.width);
        sb.append("|height:"+this.height);
        sb.append("|positionX:"+this.xCoor);
        sb.append("|positionY:"+this.yCoor);
        sb.append("|movable:"+movable);
        sb.append("|visable:"+visible);
        sb.append("|text:"+text);
        sb.append("|fontSize:"+fontSize);
        sb.append("|fontColor:"+this.fontColor);
        sb.append("|imagePath:"+imgPath);
        sb.append("|clickable:"+clickable);
        sb.append("|active:"+active);
        sb.append("|selected:"+selected);
        sb.append("|script:"+script);
        sb.append("|toBeDroppedOn"+toBeDroppedOn);
        sb.append("|canBeDroppedOn"+canBeDroppedOn);
        sb.append("|isInPossession"+isInPossession);

        sb.append("|dependShape:");
        for(String s : relatedShapes){
            sb.append(s);
            sb.append("^");
        }
        String s = sb.toString();
        if(!relatedShapes.isEmpty()){
            return s.substring(0, s.length()-1);
        }
        return s;
    }

    public void changeCoordinate(float x, float y) {
        if (!movable && !mySingleton.getInstance().docStored.isEdit) return;
        setxCoor(x);
        setyCoor(y);
    }

    public void drawPicPlay(Canvas canvas, Context context) {
        // if shape is not visible
        if ((!visible) && !mySingleton.getInstance().docStored.isEdit) {
            setVisible(false);
            return;
        }
        // if shape is text
        if (text.length() != 0) {
            Paint paint = new Paint();
            if (!visible) {
                paint.setColor(Color.LTGRAY);
            } else {
                paint.setColor(fontColor);
            }
            paint.setTextSize(fontSize);
            // draw text
            if (selected) {
                Rect rect = new Rect();
                paint.getTextBounds(text, 0, text.length(), rect);
                width = rect.width();
                height = rect.height();
                //draw Bounding Box
                RectF rf = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
                canvas.drawRect(rf, stokepaint);
            }
            canvas.drawText(text, xCoor, yCoor + height, paint);
            return;
        }

        if (imgPath.length() != 0) {
            BitmapDrawable shapeDrawable;
            int id = context.getResources().getIdentifier(imgPath, "drawable", context.getPackageName());
            System.out.println("JustStart");
            System.out.println(imgPath);
            if(id > 0) {
                shapeDrawable = (BitmapDrawable) context.getResources().getDrawable(id);
            }else{
                CreatedShapes cs = CreatedShapes.getInstance();
                LinkedList<String> imageNames_created = cs.getNames();
                HashMap<String, Bitmap> images_created = cs.getShapes();
                String thisName = imgPath;

                Bitmap bt = images_created.get(thisName);

                shapeDrawable = new BitmapDrawable(context.getResources(), bt);
            }
            //draw bitmap
            Bitmap bunnyBitmap = shapeDrawable.getBitmap();
            if (visible) {
                canvas.drawBitmap(bunnyBitmap, null, new RectF(xCoor, yCoor, xCoor + width, yCoor + height), null);
            } else {
                canvas.drawBitmap(bunnyBitmap, null, new RectF(xCoor, yCoor, xCoor + width, yCoor + height), alphaTransparencyPaint);
            }
            if (selected) {
                // draw bounding box
                RectF rf = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
                canvas.drawRect(rf, stokepaint);
            }
            return;
        }
        // fill in background within bounding box
        RectF rfin = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
        canvas.drawRect(rfin, greypaint);
        return;
    }

    // show on canvas
    public void drawPic(Canvas canvas, Context context) {
//        // if shape is not visible
//        if ((!visible) && !mySingleton.getInstance().docStored.isEdit) {
////        if (!mySingleton.getInstance().docStored.isEdit) {
//            System.out.println("visible==false");
//            hideShapeHelper(canvas, context);
//            return;
//        }
        // if shape is text
        if (text.length() != 0) {
            Paint paint = new Paint();
            if (!visible) {
                paint.setColor(Color.LTGRAY);
            } else {
                paint.setColor(fontColor);
            }
            paint.setTextSize(fontSize);
            // draw text
            if (selected) {
                Rect rect = new Rect();
                paint.getTextBounds(text, 0, text.length(), rect);
                width = rect.width();
                height = rect.height();
                //draw Bounding Box
                RectF rf = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
                canvas.drawRect(rf, stokepaint);
            }
            canvas.drawText(text, xCoor, yCoor + height, paint);
            return;
        }

        if (imgPath.length() != 0) {
            BitmapDrawable shapeDrawable;
            int id = context.getResources().getIdentifier(imgPath, "drawable", context.getPackageName());
            if(id > 0) {
                shapeDrawable = (BitmapDrawable) context.getResources().getDrawable(id);
            }else{
                CreatedShapes cs = CreatedShapes.getInstance();
                LinkedList<String> imageNames_created = cs.getNames();
                HashMap<String, Bitmap> images_created = cs.getShapes();
                String thisName = imgPath;

                Bitmap bt = images_created.get(thisName);

                shapeDrawable = new BitmapDrawable(context.getResources(), bt);
            }
            //draw bitmap
            Bitmap bunnyBitmap = shapeDrawable.getBitmap();
            if (visible) {
                canvas.drawBitmap(bunnyBitmap, null, new RectF(xCoor, yCoor, xCoor + width, yCoor + height), null);
            } else {
                canvas.drawBitmap(bunnyBitmap, null, new RectF(xCoor, yCoor, xCoor + width, yCoor + height), alphaTransparencyPaint);
            }
            if (selected) {
                // draw bounding box
                RectF rf = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
                canvas.drawRect(rf, stokepaint);
            }
            return;
        }
        // fill in background within bounding box
        RectF rfin = new RectF(xCoor, yCoor, xCoor + width, yCoor + height);
        canvas.drawRect(rfin, greypaint);
        return;
    }


    /*
    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow((x1-x2), 2)+Math.pow((y1-y2), 2));
    }



    public void goTo(String pageId){
//        if (!(clickable)) return;
//        Page nextPage = mySingleton.getInstance().docStored.pageDictionary.get(pageId);
//        if(nextPage == null) return;
//        mySingleton.getInstance().currentPageView.setPageAndDraw(nextPage);
//        mySingleton.getInstance().currentPageView.onEnter();
    }

    public void playSound(Context context, String soundId){
        int id = context.getResources().getIdentifier(soundId, "raw", context.getPackageName());
        MediaPlayer mp = MediaPlayer.create(context,id);
        mp.start();
    }

    public void hideShape(String shapeName){
        if(mySingleton.getInstance().docStored.shapeDict.get(shapeName) == null) return;
        if(shapeName.equals(id)){
            this.movable=false;
            this.active=false;
            this.visible=false;
            this.clickable=false;
        }
        else {
            mySingleton.getInstance().docStored.shapeDict.get(shapeName).movable=false;
            mySingleton.getInstance().docStored.shapeDict.get(shapeName).active=false;
            mySingleton.getInstance().docStored.shapeDict.get(shapeName).clickable=false;
            mySingleton.getInstance().docStored.shapeDict.get(shapeName).visible=false;
        }
//        mySingleton.getInstance().currentPageView.draw();
    }

    public void showShape(String shapeName){
        if(mySingleton.getInstance().docStored.shapeDict.get(shapeName) == null) return;
        setClickable(true);
        Shape nextShape = mySingleton.getInstance().docStored.shapeDict.get(shapeName);
        nextShape.setActive(true);
        nextShape.setVisible(true);
//        mySingleton.getInstance().currentPageView.draw();
    }

    public interface moveAction{
        void move(Context context);
    }

    public boolean isActive() {
        return active;
    }

    public void parseMotionEvent(MotionEvent event, float dx, float dy) {
        setSelected(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                changeCoordinate(event.getX()-dx, event.getY()-dy);
                break;
        }
    }

    public moveAction moveShapeAction(final String[] moveStr, final int index) {
        moveAction currentMove = new moveAction() {
            @Override
            public void move(Context context) {
                if (moveStr[index].equals("goto")) {
                    goTo(moveStr[index+1]);
                } else if (moveStr[index].equals("play")) {
                    playSound(context, moveStr[index+1]);
                } else if (moveStr[index].equals("hide")) {
                    hideShape(moveStr[index+1]);
                } else if (moveStr[index].equals("show")){
                    showShape(moveStr[index+1]);
                }
            }
        };
        return currentMove;
    }

    public void shapeSizeEditor(MotionEvent event, int mode) {
        if (((!visible) && (!mySingleton.getInstance().docStored.isEdit))) return;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x_c = event.getX();
                float y_c = event.getY();
                float x2 = 0;
                float y2 = 0;
                switch (mode) {
                    case UPPER_LEFT:
                        x2 = xCoor+width;
                        y2 = yCoor+height;
                        break;
                    case UPPER_RIGHT:
                        x2 = xCoor;
                        y2 = yCoor+height;
                        break;
                    case LOWER_LEFT:
                        x2 = xCoor+width;
                        y2 = yCoor;
                        break;
                    case LOWER_RIGHT:
                        x2 = xCoor;
                        y2 = yCoor;
                        break;
                }
                setxCoor(Math.min(x_c, x2));
                setyCoor(Math.min(y_c, y2));
                setWidth(Math.abs(x_c-x2));
                setHeight(Math.abs(y_c-y2));
                break;
        }
    }

    public int validationCheckOnEdit(float x, float y, float threshold) {
        // check if the length of each side is bigger than the threshold
        ArrayList<Float> dists = new ArrayList<Float>();
        dists.add(getDistance(x, y, xCoor, yCoor));
        dists.add(getDistance(x, y, xCoor+width, yCoor));
        dists.add(getDistance(x, y, xCoor, yCoor+height));
        dists.add(getDistance(x, y, xCoor+width, yCoor+height));
        int minIndex = dists.indexOf (Collections.min(dists));
        if (dists.get(minIndex)<=threshold) return minIndex+1;
        return 0;
    }

    public boolean isInBound(MotionEvent event) {
        if (!(clickable && !((!visible) && !mySingleton.getInstance().docStored.isEdit))) return false;
        float x = event.getX();
        float y = event.getY();
        return (x>xCoor && x<xCoor+width && y>yCoor && y<yCoor+height);
    }

    public void mouseRelease() { setSelected(true);}

    public void onCancel() { setSelected(false);}

    public boolean getDropValidity() {
        return canBeDroppedOn;
    }

    public String getTextOrImage() {
        if (text.length()>0) {
            return text;
        }
        if (imgPath.length()>0) {
            return imgPath;
        } else {
            return null;
        }
    }

    private void hideShapeHelper(Canvas canvas, Context context) {
        System.out.println("In Shape hide");
        setMovable(false);
        setClickable(false);
        setVisible(false);
    }

    public boolean getDropAvailability() {
        return toBeDroppedOn;
    }

    public boolean isImage() {
        return !isText() && imgPath.length()>0;
    }

    public boolean isSelected() {
        return selected;
    }
     */
}
