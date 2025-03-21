package tp.pr5.control;

import java.util.Scanner;

import tp.pr5.Resources.Counter;
import tp.pr5.Resources.GameType;
import tp.pr5.logic.Game;

public abstract class Controller {
	protected Game game;
	private Scanner in;
	private Player[] players; 
	private int currentPlayer;
	private GameTypeFactory gameType;
	private Counter[] c = { Counter.WHITE, Counter.BLACK };
	
	public Controller(GameTypeFactory f, Game g, java.util.Scanner in) {
		this.gameType = f;
		this.game = g;
		this.in = in;
		this.players = new Player[2]; // Create players array
		this.gameType = f;
		initGame(); // initialize the rest of atributes
	}

	public void initGame() {
		game.reset(gameType.createRules()); // Reset a new game
		players[0] = gameType.createHumanPlayerAtConsole(in);
		players[1] = gameType.createHumanPlayerAtConsole(in);
		if (game.getTurn() == Counter.WHITE) {
			currentPlayer = 0;
		}
		else {
			currentPlayer = 1;
		}
	}
	
	void changeG(GameType gameType, int x, int y) {
		if (gameType == GameType.connect4){
			this.gameType = new Connect4Factory();
		}
		else if(gameType == GameType.complica){
			this.gameType = new ComplicaFactory();
		}
		else if(gameType == GameType.gravity){
			this.gameType = new GravityFactory(x, y);
		}
		else if(gameType == GameType.reversi){
			this.gameType = new ReversiFactory();
		}
		initGame();
	}
	
	public void changePlayer() {
		if (currentPlayer == 0) {
			currentPlayer = 1;
		}
		else if (currentPlayer == 1) {
			currentPlayer = 0;
		}
	}
	
	abstract public void run();

	// Getters and setters
	
	public Game getGame() { return this.game; }
	public Player[] getPlayers() { return players; }
	public Counter[] getCounter() { return this.c; }
	public int getCurrentPlayer() { return this.currentPlayer; }
	public GameTypeFactory getGameTypeFactory() { return gameType; }
	public Counter[] getC(){return c;}
	public GameTypeFactory getGameType(){return gameType;}
	public void setGameType(GameTypeFactory g){gameType = g;}
	public void setPlayerInPosition(Player player,int pos){players[pos] = player;}

}
