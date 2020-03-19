package cs108.stanford.edu.bunnyworldeditor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void JsonTest1(){
        Shape shape1 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape2 = new Shape("shape2", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape3 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);
        HashMap<String, Shape> hm = new HashMap<>();
        hm.put("shape1", shape1);
        hm.put("shape2", shape2);

        assertEquals(shape1, shape3);

        Gson gson = new Gson();
        String json = gson.toJson(hm);
        HashMap<String, Shape> newMap = gson.fromJson(json, new TypeToken<HashMap<String, Shape>>() {}.getType());

        assertEquals(newMap.get("shape1"), shape1);
    }

    @Test
    public void JsonTest2(){
        Shape shape1 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape2 = new Shape("shape2", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape3 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);


        Page page1 = new Page("page1");
        page1.addShape(shape1);
        page1.addShape(shape2);
        page1.relatedShapes.add("shape1");
        page1.relatedShapes.add("shape2");

        Page page2 = new Page("page2");
        page2.addShape(shape2);
        page2.addShape(shape3);
        page2.relatedShapes.add("shape2");
        page2.relatedShapes.add("shape3");


        Page page3 = new Page("page1");
        page3.addShape(shape1);
        page3.addShape(shape2);
        page3.relatedShapes.add("shape1");
        page3.relatedShapes.add("shape2");

        HashMap<String, Page> hm = new HashMap<>();
        hm.put("page1", page1);
        hm.put("page2", page2);
        hm.put("page3", page3);

//        System.out.println(page1.name.equals(page3.name));
//        System.out.println(page1.relatedShapes.equals(page3.relatedShapes));
//        System.out.println(page1.shapes.equals(page3.shapes));
//        System.out.println(page1.backgroundId == page3.backgroundId);
//        System.out.println(page1.name.equals(page3.name) && page1.relatedShapes.equals(page3.relatedShapes) && page1.shapes.equals(page3.shapes) && page1.backgroundId == page3.backgroundId);

        Gson gson = new Gson();
        String json = gson.toJson(hm);
        HashMap<String, Page> newMap = gson.fromJson(json, new TypeToken<HashMap<String, Page>>() {}.getType());

        assertEquals(newMap.get("page1"), page1);
        assertEquals(newMap.get("page1"), newMap.get("page3"));
    }

    @Test
    public void JsonTest3(){
        Shape shape1 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape2 = new Shape("shape2", 10.0f, 10.0f, 10.0f, 10.0f);
        Shape shape3 = new Shape("shape1", 10.0f, 10.0f, 10.0f, 10.0f);


        Page page1 = new Page("page1");
        page1.addShape(shape1);
        page1.addShape(shape2);
        page1.relatedShapes.add("shape1");
        page1.relatedShapes.add("shape2");

        Page page2 = new Page("page2");
        page2.addShape(shape2);
        page2.addShape(shape3);
        page2.relatedShapes.add("shape2");
        page2.relatedShapes.add("shape3");


        Page page3 = new Page("page1");
        page3.addShape(shape1);
        page3.addShape(shape2);
        page3.relatedShapes.add("shape1");
        page3.relatedShapes.add("shape2");


        Gson gson = new Gson();
        String json1 = gson.toJson(page1);
        Page t1 = gson.fromJson(json1, new TypeToken<Page>() {}.getType());

        String json2 = gson.toJson(page2);
        Page t2 = gson.fromJson(json2, new TypeToken<Page>() {}.getType());

        String json3= gson.toJson(page3);
        Page t3 = gson.fromJson(json3, new TypeToken<Page>() {}.getType());

        assertFalse(t1.equals(t2));
        assertTrue(t1.equals(t3));
    }

}