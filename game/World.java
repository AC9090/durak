package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import cards.Hand;
import cards.Suit;
import cards.Card;
import model.CardLocator;
import model.ViewBoard;
import model.ViewCard;

import engine.ResourceLoader;
import engine.Texture;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class World {

	private Game game;
	
	private long lastFrame;
	private static final int WD = 800, HT = 600;
	private static final float aspect = (float)WD / (float) HT;

	private boolean exit = false;
	private int numPlayers;

	private int vsId;
	private int fsId;
	private int pId;
	
	private float csx, csy;
	
	HashMap<Card, ViewCard> viewCards;
	HashMap<ViewCard, Card> gameCards;
	ViewCard selected;
	private ViewBoard board;
	ArrayList<CardLocator> viewLocators;
	
	private boolean cardSel = false;
	private boolean click = false;
	public World(int numPlayers){
		this.numPlayers = numPlayers;
		game = new Game(numPlayers);
		initDisplay();
		initGl();
		initModels();
	}
	



	public static void main(String[] args) {
		World world = new World(2);
		world.run();
	}

	private void run() {
		while (!Display.isCloseRequested() && !exit ){
			getInput();
			
			update();
			
			render();
			
			Display.update();
			Display.sync(60);

		}
		Display.destroy();
		Keyboard.destroy();
		
	}
	

	private void getInput() {
		
		//terrible implementation
		Keyboard.isKeyDown(Keyboard.KEY_RETURN);
		
		boolean leftButtonDown = Mouse.isButtonDown(0); // is left mouse button down.
		boolean rightButtonDown = Mouse.isButtonDown(1); // is right mouse button down.
		if (click){
			if (!leftButtonDown){
				click = false;
			}
		} else {
			if (leftButtonDown) {
				float m_x = (float) Mouse.getX() / (float) WD * 2 - 1;
				float m_y = (float) Mouse.getY() / (float) HT * 2 - 1;
				if (!cardSel) {
					for (ViewCard c : viewCards.values()) { //TODO: can't select cards that are not in a hand. Get all hands then check only view cards in hands
						if (c.isClicked(m_x, m_y)) {
							c.setHighlighted(true);
							cardSel = true;
							selected = c;
							System.out.println("highlighted card");
							break;
						}
					}
				} else {
					int inPlaySel = -1;
					int attDef;
					for (int i = 0; i < 6; i++){
						if ((board.posInPlayX()[i] + board.getCsX() >= m_x && m_x >= board.posInPlayX()[i])
								&&   board.posInPlayY()[0] + board.getCsY() >= m_y && m_y > board.posInPlayY()[0]){
							inPlaySel = i;
							attDef = 0;
							break;
						} else	if ((board.posInPlayX()[i] + board.getCsX() >= m_x && m_x >= board.posInPlayX()[i])
								&&   board.posInPlayY()[1] + board.getCsY() >= m_y && m_y > board.posInPlayY()[1] ){
							inPlaySel = i;
							attDef = 1;
							break;
						}
					}
					if (inPlaySel != -1){
						try{
							game.play(gameCards.get(selected), (int) inPlaySel);
						} catch (InvalidMove e) {
							System.out.println("You cant place this card here");
						} catch (InvalidPlayer e) {
							System.out.println("This player cand do this move");
						}
					}
					cardSel = false;
					selected.setHighlighted(false);
				}
				click = true;
			}
		}
			
	}

	private void update() {
		//top of deck
		{
			ViewCard c = viewCards.get(game.getDeckCards().get(0));
			c.setPos(-1f, 1f - c.getSY());
			c.setFaceDown();
			c.setVisible(true);
		}
		//cards on board
		{
		ArrayList<Card[]> ipc = game.getInPlayCards();
		for(int i = 0; i < ipc.size(); i ++){
			Card[] cp = ipc.get(i);
		
			if(cp[0] != null){
				ViewCard c = viewCards.get(cp[0]);
				c.setPos(board.posInPlayX()[i], board.posInPlayY()[0]);
				c.setFaceUp();
				c.setVisible(true);
			}
			if(cp[1] != null){
				ViewCard c = viewCards.get(cp[1]);
				c.setPos(board.posInPlayX()[i], board.posInPlayY()[1]);
				c.setFaceUp();
				c.setVisible(true);
			}
		}
		}
		// cards in hands
		
		ArrayList<Hand> hands = game.getHands();
		for (int i = 0; i < hands.size(); i++){	//TODO: If hands.size() is bigger than viewable hands and same with viewable cards.
			ArrayList<Card> ch = hands.get(i).getCards();
			for(int j = 0; j < ch.size(); j++){
				
				ViewCard c = viewCards.get(ch.get(j));
				c.setPos(board.posHandCardX()[j], board.posHandY()[i]);
				c.setFaceUp();
				c.setVisible(true);
			}
		}
		
		if (!game.getDiscard().isEmpty()){
			ViewCard c = viewCards.get(game.getDiscard().getCards().get(game.getDiscard().getCards().size() - 1));
			c.setPos(1.0f - c.getSX(), 1.0f - c.getSY());
			c.setFaceDown();
			c.setVisible(true);
		}
			
				
	}
	

	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		glClearColor(0.1f,0.1f,0.1f, 1.0f);

		glUseProgram(pId);
		glColor3f(1.0f, 0.5f, 0.5f);
	    
		for (CardLocator l: viewLocators) {
			l.render();
		}
		
		for (ViewCard c : viewCards.values()){
			c.render();
		}
		
//		viewCard.render();
//		viewCard2.render();
		//draw
		this.exitOnGLError("render");
	}

	private void initModels() {
		Texture tex = ResourceLoader.loadTexture("cards.png", GL_TEXTURE0);
		float cwth =  (326f/4f)/(800f/13f);
		csx = 0.17f;
		csy = csx * cwth * aspect;
		System.out.println(cwth);
		//viewCard2.flip();
		viewCards = new HashMap<Card, ViewCard>();
		gameCards = new HashMap<ViewCard, Card>();
		for(Card c : game.getDeckCards()){
			viewCards.put(c, new ViewCard(0,0,csx,csy,c.getSuit(), c.getValue(),tex));
			gameCards.put(viewCards.get(c), c);
			viewCards.get(c).setVisible(false);
		}
		board = new ViewBoard(csx, csy);
		viewLocators = new ArrayList<CardLocator>();
		for (int i = 0; i<2; i++) {
			for (int j = 0; j < 6; j++) {
				viewLocators.add(new CardLocator(board.posInPlayX()[j],board.posInPlayY()[i], csx, csy, tex));
			}
		}
		
		game.deal();
		
		switch (numPlayers){
		case 2:
			
		}
		
	}
	private void initGl(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WD, 0, HT, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		
		glDisable(GL_DEPTH_TEST);
		
		setupShaders();
		lastFrame = getTime();
		glViewport(0, 0, WD, HT);
		this.exitOnGLError("initGl");
		
	}
	
	private void initDisplay(){
		try {
			DisplayMode dm = new DisplayMode(WD, HT);
			System.out.println(dm.isFullscreenCapable());
			Display.setDisplayMode(dm);
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException ex){
			System.err.println("Error! Could not create the Display!");
			ex.printStackTrace(System.err);
		}
		
	}
	
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	private int getDelta(){
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = currentTime;
		return delta;
	}

	private void setupShaders() {       
	    // Load the vertex shader
	    vsId = this.loadShader("./res/vertexshader", GL_VERTEX_SHADER);
	    // Load the fragment shader
	    fsId = this.loadShader("./res/fragmentshader", GL_FRAGMENT_SHADER);
	     
	    // Create a new shader program that links both shaders
	    pId = glCreateProgram();
	    glAttachShader(pId, vsId);
	    glAttachShader(pId, fsId);
	
	    // Position information will be attribute 0
	    glBindAttribLocation(pId, 0, "in_Position");
	    // Color information will be attribute 1
	    glBindAttribLocation(pId, 1, "in_Color");
	    // Textute information will be attribute 2
	    glBindAttribLocation(pId, 2, "in_TextureCoord");
	     
	    glLinkProgram(pId);
	    glValidateProgram(pId);
	    
	    this.exitOnGLError("setupShaders");
	     
	}
	private int loadShader(String filename, int type) {
	    StringBuilder shaderSource = new StringBuilder();
	    int shaderID = 0;
	     
	    try {
	        BufferedReader reader = new BufferedReader(new FileReader(filename));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            shaderSource.append(line).append("\n");
	        }
	        reader.close();
	    } catch (IOException e) {
	        System.err.println("Could not read file.");
	        e.printStackTrace();
	        System.exit(-1);
	    }
	     
	    shaderID = glCreateShader(type);
	    glShaderSource(shaderID, shaderSource);
	    glCompileShader(shaderID);
	     
	    if (glGetShader(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
	        System.err.println("Could not compile shader.");
	        System.exit(-1);
	    }
	    this.exitOnGLError("loadShader");
	     
	    return shaderID;
	}



	private void exitOnGLError(String errorMessage) {
	    int errorValue = glGetError();
	     
	    if (errorValue != GL_NO_ERROR) {
	       // String errorString = gluErrorString(errorValue);
	        System.err.println("ERROR - " + errorMessage + ": ");
	         
	        if (Display.isCreated()) Display.destroy();
	        System.exit(-1);
	    }
	}
}