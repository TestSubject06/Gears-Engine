package gears;

import java.awt.Graphics2D;

/**
 * This is the most basic object in the engine. Try to not use this for anything
 * other than abstraction. It does receive an update and a render call from the state
 * if it has been registered, but it doesn't really do anything with it. Use only
 * under special cases where you know what you're doing.
 * @author Zack
 */
public class GObject {
    public static int numRenders = 0;
    public static int numUpdates = 0;

    public boolean visible;
    public boolean active;
    public boolean exists;
    public double x;
    public double y;
    public int width;
    public int height;

    public GObject(double x, double y){
        this(x, y, 0, 0);
    }
    
    public GObject(double x, double y, int width, int height){
    	exists = true;
        active = true;
        visible = true;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(){
        numUpdates++;
    }

    public void render(Graphics2D g){
        numRenders++;
    }
    
    public void destroy(){
    	
    }
    
    public boolean checkPointInBounds(GPoint p){
    	if(p.x < x)
    		return false;
    	if(p.x > x + width)
    		return false;
    	if(p.y < y)
    		return false;
    	if(p.y > y + height)
    		return false;
    	return true;
    }
    
    public boolean checkIntersection(GObject o){
		  return (Math.abs(x - o.x) * 2 < (width + o.width)) && (Math.abs(y - o.y) * 2 < (height + o.height));
    }
    
    public boolean checkIntersection(GRect r){
    	return (Math.abs(x - r.x) * 2 < (width + r.width)) && (Math.abs(y - r.y) * 2 < (height + r.height));
    }
}
