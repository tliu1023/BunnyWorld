package cs108.stanford.edu.bunnyworldeditor;

//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Possessions {
//    private List<Shape> shapeList;
//    private float top, bottom, left, right;
//    private Paint paint;
//
//    public Possessions(float top, float bottom, float left, float right) {
//        this.shapeList = new ArrayList<>();
//        this.top = top;
//        this.bottom = bottom;
//        this.left = left;
//        this.right = right;
//        this.paint = new Paint();
//        this.paint.setColor(Color.WHITE);
//        this.paint.setStyle(Paint.Style.FILL);
//    }
//
//    public List<Shape> getShapeList() {
//        return shapeList;
//    }
//
//    public float getTop() {
//        return top;
//    }
//
//    public float getBottom() {
//        return bottom;
//    }
//
//    public float getLeft() {
//        return left;
//    }
//
//    public float getRight() {
//        return right;
//    }
//
//    public boolean isInsidePossessionsArea(float x, float y) {
//        // top's coordinate is smaller than bottoms coordinate
//        return x >= left && x <= right && y >= top && y <= bottom;
//    }
//
//    public void addToPossessions(Shape shapeToAdd) {
//        shapeList.add(shapeToAdd);
//    }
//
//    public void removeFromPossessions(Shape shapeToRemove) {
//        shapeList.remove(shapeToRemove);
//    }
//
//
//
//    public Shape getShapeByLoc(float x, float y) {
//        for(Shape shape : shapeList) {
//            if (x >= shape.getxCoor() && x <= shape.getxCoor() + shape.getWidth() && y >= shape.getyCoor() && y <= shape.getyCoor() + shape.getHeight()) {
//                return shape;
//            }
//        }
//        return null;
//    }
//}



import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class Possessions {
    private List<Shape> shapeList;
    private float top, bottom, left, right;
    private Paint paint;

    public Possessions(float top, float bottom, float left, float right) {
        this.shapeList = new ArrayList<>();
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public List<Shape> getShapeList() {
        return shapeList;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public boolean isInsidePossessionsArea(float x, float y) {
        // top's coordinate is smaller than bottoms coordinate
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public void addToPossessions(Shape shapeToAdd) {
        shapeList.add(shapeToAdd);
    }

    public void removeFromPossessions(Shape shapeToRemove) {
        shapeList.remove(shapeToRemove);
    }



    public Shape getShapeByLoc(float x, float y) {
        for(int i = shapeList.size() - 1; i >= 0; i--) {
            Shape shape = shapeList.get(i);
            if (x >= shape.getxCoor() && x <= shape.getxCoor() + shape.getWidth() && y >= shape.getyCoor() && y <= shape.getyCoor() + shape.getHeight()) {
                return shape;
            }
        }
        return null;
    }

    public Shape getShapeByName(String name) {
        for (Shape s : shapeList) {
            if (s.id.equals(name)) {
                return s;
            }
        }
        return null;
    }
}
