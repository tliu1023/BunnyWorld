package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Canvas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Page implements Serializable {
    public String name;
    public Shape selectedShape = null;
    public ArrayList<Shape> shapes;

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public HashSet<String> relatedShapes;
    public int backgroundId = 2131230816;


    public Page(String name) {
        this.name = name;
        shapes = new ArrayList<Shape> ();
        relatedShapes = new HashSet<> ();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape shape) {
        // unselect the previous one
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        if (shape != null) {
            shape.setSelected(true);
        }
        selectedShape = shape;
    }

    public Shape getShapeByLoc(float x, float y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape sh = shapes.get(i);
            if (x >= sh.xCoor && x <= sh.xCoor + sh.width && y >= sh.yCoor && y <= sh.yCoor + sh.height) {
                return sh;
            }
        }
        return null;
    }

    public void addShape(Shape shape) {
        if (!shapes.contains(shape)) {
            shapes.add(shape);
        }
    }

    public void removeShape(Shape shape) {
        for (Shape sh : shapes) {
            if (sh.equals(shape)) {
                shapes.remove(sh);
                return;
            }
        }
    }

    public Shape getShapeByName(String name) {
        for (Shape s : shapes) {
            if (s.id.equals(name)) {
                return s;
            }
        }
        return null;
    }

    public void drawPagePlay(Canvas canvas, Context context) {
        for (Shape shape : shapes) {
            shape.drawPicPlay(canvas, context);
        }
    }

    /*
    public void drawPage(Canvas canvas, Context context) {
        for (Shape shape : shapes) {
            shape.drawPic(canvas, context);
        }
    }
    */
}
