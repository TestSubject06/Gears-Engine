package gears;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * This controls the updating and rendering of the states on the stack.
 * @author Zack
 */
public class GStateManager {
    private ArrayList<GState> stateStack;
    private int drawIndex;
    private boolean checkRender;
    public GStateManager(GState initialState){
        newStack(initialState);
    }

    public void update(){
        for (int i = stateStack.size()-1; i >= 0; i--) {
            stateStack.get(i).update();
            if(i <= stateStack.size()-1 && !stateStack.get(i).passUpdate)
                break;
        }
    }

    public void render(Graphics2D g){
        //We need to draw from the bottom up, but the top-most states could refuse to allow drawing under them.
        //We need to start from the top, and work our way down to find the first that doesn't allow render to pass through.
        //Then draw backwards from there.
        if(checkRender){
            for (int i = stateStack.size()-1; i >= 0; i--) {
                if (!stateStack.get(i).passDraw || i == 0){
                    drawIndex = i;
                    checkRender = false;
                    break;
                }
            }
        }
        for (int i = drawIndex; i < stateStack.size(); i++) {
            stateStack.get(i).render(g);
        }
    }
    
    /**
     * Passes information backwards one level in the state stack.
     * @param message the message to send back
     */
    public void passBack(String message){
    	stateStack.get(stateStack.size()-2).passBack(message);
    }

    /**
     * Adds a new state to the top of the stack.
     * @param newState The state to be added.
     */
    public void addState(GState newState){
        stateStack.add(newState);
        checkRender = true;
        newState.create();
    }

    /**
     * Pops the top state off the stack.
     */
    public void popState(){
        stateStack.get(stateStack.size()-1).destroy();
        stateStack.remove(stateStack.size()-1);
        GBase.resetInput();
        checkRender = true;
    }

    /**
     * Dumps the old state and creates a new one. Useful for going from a main
     * menu into the game and things like that.
     * @param initialState The state the new stack will start at.
     */
    public void newStack(GState initialState){
        stateStack = new ArrayList<>();
        stateStack.add(initialState);
        initialState.create();
        checkRender = true;
    }

    /**
     * Gets the topmost state in the stack, or the currently active state.
     * Generally not useful but hey, it's here.
     * @return The topmost state.
     */
    public GState getTopState(){
        return stateStack.get(stateStack.size()-1);
    }
}
