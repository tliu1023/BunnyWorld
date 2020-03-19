package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Shape implements Serializable {
    private final float minSize = 1;

    private boolean movable;
    private boolean visible;
    private String text;
    private float fontSize;
    private int fontColor;
    private String imgPath;
    private boolean clickable;
    public boolean selected;
    private String script;
    private Paint stokepaint = new Paint();
    private Paint greypaint = new Paint();
    private boolean toBeDroppedOn;
    private boolean canBeDroppedOn;
    private boolean isInPossession;
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
        fontSize = 80;
        fontColor = Color.BLACK;

        text = "";
        imgPath = "";
        selected = false;
        script = "";
        clickable = true;
        this.toBeDroppedOn = false;
        this.canBeDroppedOn = false;
        isInPossession = false;
        alphaTransparencyPaint.setAlpha(120);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Shape)) {
            return false;
        }
        Shape temp = (Shape) obj;
        return temp.hashCode() == this.hashCode() &&
                temp.width == this.width &&
                temp.height == this.height &&
                temp.xCoor == this.xCoor &&
                temp.yCoor == this.yCoor;
    }

    @Override
    public String toString() {
        return "Shape{" + "id='" + id + '\'' + '}';
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

    public String getContext() {
        if (text.length()>0) return text;
        if (imgPath.length()>0) return imgPath;
        return null;
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
}