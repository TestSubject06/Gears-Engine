package gears;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This is the base of all game states. For example a Main Menu is a state, or perhaps
 * a splash screen state, and a main play state.
 * @author Zack
 */
public class GState {

    //A list of all of the objects that have registered themselves to the state.
    public ArrayList<GObject> members;
    public boolean passUpdate = false;
    public boolean passDraw = true;
    
    public GState(){
    	this(true, false);
    }
    
    public GState(boolean passDraw, boolean passUpdate){
    	this.passDraw = passDraw;
    	this.passUpdate = passUpdate;
    }

    /**
     * Updates the state of the Game State.
     * Call super.update when you feel the objects need to be updated.
     */
    public void update(){
        for(GObject a : members){
            if(a.active && a.exists){
                a.update();
            }
        }
    }

    /**
     * Renders the current state of the Game State onto the graphic g.
     * Call Super.render when you feel you need the objects to be rendered
     * @param g the image the state will draw to
     */
    public void render(Graphics2D g){
        for(GObject a : members){
            if(a.visible && a.exists){
                a.render(g);
            }
        }
    }
    
    /**
     * Destroys the state. Call super.destroy AFTER your stuff.
     */
    public void destroy(){
    	GBase.input.unRegisterListener(this);
    	for(GObject a : members){
    		a.destroy();
    	}
    }

    /**
     * Called when the game is ready to create the state. Don't do too much
     * in the state constructor since the game is not yet ready for the state
     * to start doing stuff.
     */
    public void create(){
        members = new ArrayList<>(16);
    }

    /**
     * Registers an object to be included in the game's update and render loop.
     * @param object the object to be added
     */
    public void add(GObject object){
        members.add(object);
    }
    
    public void remove(GObject object){
    	members.remove(object);
    }
    
    public void keyPress(int keyCode){
    	//Does nothing on it's own. Implement this if you need it.
    }
    
    public void passBack(String message){
    	//Does nothing on it's own.
    }
}
