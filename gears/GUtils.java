package gears;

import java.awt.Color;

/**
 * Contains lots of useful functions and math utilities.
 * Populate as needed.
 * @author Zack
 */
public class GUtils {

    /**
     * Gets the distance between two points
     * @param p1 the first point
     * @param p2 the second point
     * @return the distance between the two as a double
     */
    public static double distance(GPoint p1, GPoint p2){
        double dx = Math.abs(p1.x - p2.x);
        double dy = Math.abs(p1.y - p2.y);
        return Math.sqrt(dx*dx + dy*dy);
    }

    /**
     * Takes in the components of the color as integers, and the alpha as a float.
     * @param r the red value   (0-255)
     * @param g the green value (0-255)
     * @param b the blue value  (0-255)
     * @param a the alpha value (0.0f-1.0f)
     * @return the color you wanted
     */
    public static Color RGBAf(int r, int g, int b, float a){
        int alpha = Math.round(a*255);
        return new Color(r, g, b, alpha);
    }
    
    public static Color RGBAfPre(int color, float a){
    	color = color * Math.round(a*255);
    	return new Color(color, true);
    }
    
    /**
	 * Assumes the arrays are the same length. If the two arrays differ in size, only the length of a1 is taken into account, 
	 * and any data that was in a2 will not be returned by this function.
	 * @param a1 the first array
	 * @param a2 the second array
	 * @return returns a1 + a2 as the sum of the elements.
	 */
	public int[] addArrays(int[] a1, int[] a2){
		int[] a = new int[a1.length];
		for(int i = 0; i<a1.length; i++){
			a[i] = a1[i] + a2[i];
		}
		return a;
	}
}
