package gears;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * Contains all of the input listeners for the engine.
 * the keys in this class correspond to a standard QWERTY US keyboard.
 * @author Zack, bro
 */
public class GInput implements KeyListener, MouseWheelListener, MouseListener {

	//Large mass of constants.
    public static final int Backspace = 8;
    public static final int Enter = 10;
    public static final int Shift = 16;
    public static final int Control = 17;
    public static final int Alt = 18;
    public static final int PauseBrk = 19;
    public static final int CapsLock = 20;
    public static final int ESC = 27;
    public static final int PgUp = 33;
    public static final int PgDn = 34;
    public static final int End = 35;
    public static final int Home = 36;
    public static final int LeftArrow = 37;
    public static final int UpArrow = 38;
    public static final int RightArrow = 39;
    public static final int DownArrow = 40;
    public static final int Comma = 44;
    public static final int Minus = 45;
    public static final int Period = 46;
    public static final int FSlash = 47;
    public static final int K0 = 48;
    public static final int K1 = 49;
    public static final int K2 = 50;
    public static final int K3 = 51;
    public static final int K4 = 52;
    public static final int K5 = 53;
    public static final int K6 = 54;
    public static final int K7 = 55;
    public static final int K8 = 56;
    public static final int K9 = 57;
    public static final int Semicolon = 59;
    public static final int Plus = 61;
    public static final int A = 65;
    public static final int B = 66;
    public static final int C = 67;
    public static final int D = 68;
    public static final int E = 69;
    public static final int F = 70;
    public static final int G = 71;
    public static final int H = 72;
    public static final int I = 73;
    public static final int J = 74;
    public static final int K = 75;
    public static final int L = 76;
    public static final int M = 77;
    public static final int N = 78;
    public static final int O = 79;
    public static final int P = 80;
    public static final int Q = 81;
    public static final int R = 82;
    public static final int S = 83;
    public static final int T = 84;
    public static final int U = 85;
    public static final int V = 86;
    public static final int W = 87;
    public static final int X = 88;
    public static final int Y = 89;
    public static final int Z = 90;
    public static final int LBracket = 91;
    public static final int Backslash = 92;
    public static final int RBracket = 93;
    public static final int Numpad_0 = 96;
    public static final int Numpad_1 = 97;
    public static final int Numpad_2 = 98;
    public static final int Numpad_3 = 99;
    public static final int Numpad_4 = 100;
    public static final int Numpad_5 = 101;
    public static final int Numpad_6 = 102;
    public static final int Numpad_7 = 103;
    public static final int Numpad_8 = 104;
    public static final int Numpad_9 = 105;
    public static final int NumpadAsterisk = 106;
    public static final int NumpadPlus = 107;
    public static final int NumpadMinus = 109;
    public static final int NumpadPeriod = 110;
    public static final int NumpadFSlash = 111;
    public static final int F1 = 112;
    public static final int F2 = 113;
    public static final int F3 = 114;
    public static final int F4 = 115;
    public static final int F5 = 116;
    public static final int F6 = 117;
    public static final int F7 = 118;
    public static final int F8 = 119;
    public static final int F9 = 120;
    public static final int F10 = 121;
    public static final int F11 = 122;
    public static final int F12 = 123;
    public static final int Delete = 127;
    public static final int Numlock = 144;
    public static final int Insert = 155;
    public static final int Tilde = 192;
    public static final int Apostrophe = 222;

    private int currentKeyState[];
    public int mouseWheelMovement;
    private int mouseStatus;
    private ArrayList<GState> listeners;
    private GPoint mousePosition;
    private boolean inputReset = false;

    public GInput(){
        currentKeyState = new int[256];
        listeners = new ArrayList<>();
        mouseStatus = 0;
        mouseWheelMovement = 0;
        mousePosition = new GPoint(0,0);
    }
    
    public void resetInput(){
    	inputReset = true;
    }
    
    public void registerListener(GState state){
    	if(!listeners.contains(state)){
        	listeners.add(state);	
    	}
    }
  
    public void unRegisterListener(GState state){
    	listeners.remove(state);
    }

    /**
     * Updates this input instance. Since the class does not have access to the window, we have to figure out
     * the mouse pointer location in GGame which calls the update method on this directly, supplying the
     * mouse x and mouse y.
     * @param mouse_x x location of the mouse cursor in window coordinates.
     * @param mouse_y y location of the mouse cursor in window coordinates.
     */
    public void update(int mouse_x, int mouse_y){
    	inputReset = false;
        for(int i = 0; i < currentKeyState.length; i++){
            if(currentKeyState[i] == 2)
                currentKeyState[i] = 1;
            if(currentKeyState[i] == -1)
                currentKeyState[i] = 0;
        }
        if(mouseStatus == 2)
            mouseStatus = 1;
        if(mouseStatus == -1)
            mouseStatus = 0;
        mouseWheelMovement = 0;
        mousePosition.x = mouse_x;
        mousePosition.y = mouse_y;
    }
    
    public GPoint getMouseLocation(){
    	return mousePosition;
    }
    
    public double getMouseX(){
    	return mousePosition.x;
    }
    
    public double getMouseY(){
    	return mousePosition.y;
    }

    public boolean justPressed(int keyCode){
        return inputReset ? false : currentKeyState[keyCode] == 2;
    }

    public boolean justReleased(int keyCode){
        return inputReset ? false : currentKeyState[keyCode] == -1;
    }

    public boolean isDown(int keyCode){
        return inputReset ? false : currentKeyState[keyCode] > 0;
    }
    
    public boolean mouseJustPressed(){
    	return inputReset ? false : mouseStatus == 2;
    }
    
    public boolean mouseJustReleased(){
    	return inputReset ? false : mouseStatus == -1;
    }
    
    public boolean mouseDown(){
    	return inputReset ? false : (mouseStatus > 0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //We ignore this
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() < 256){
            if(currentKeyState[e.getKeyCode()] < 1){
                currentKeyState[e.getKeyCode()] = 2;
                for(GState s : listeners){
                	s.keyPress(e.getKeyCode());
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() < 256){
            currentKeyState[e.getKeyCode()] = -1;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelMovement = e.getWheelRotation();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //We also ignore this, we don't care about a quick click and releasem since our code
    	//handles that anyways
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseStatus = 2;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	mouseStatus = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //We only have a single component, so no one cares about this.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //This could be useful, but probably not, so we ignore it.
    }

}
