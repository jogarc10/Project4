package tp.pr5.logic;
 
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

import tp.pr5.Resources.Counter;
import tp.pr5.logic.Board;
import tp.pr5.logic.Move; 

public class Game implements Observable<GameObserver> {
	private Board board;
	private Counter turn;
	private Counter winner;
	private boolean finished;
	protected GameRules rules;
	private Collection<GameObserver> obsList; // ArrayList of Observers (En este proyecto, vamos a tener observadores en la vista)
	private Deque<Move> stack = new ArrayDeque<>();
	
	public Game(GameRules rules) { 
		obsList = new ArrayList<GameObserver>();
		this.rules = rules; 
		reset(rules);
	}

	public void reset(GameRules rules) { // Reset all the Game Rules
		this.rules = rules;
		board = rules.newBoard();		
		turn = rules.initialPlayer();
		winner = Counter.EMPTY;
		finished = false;
		stack.clear(); 
				
		for (GameObserver o : obsList)
			o.reset(board, turn, false);
	}
	
	public boolean executeMove(Move mov) throws InvalidMove {  
		boolean valid = false, draw; 
		Counter wonColor, currentPlayer = mov.getPlayer();
		
		if ((mov.getPlayer() == turn) && (!finished)) { // No puede permitir hacer movimientos fuera de turno o se ha terminado el juego
			winner = Counter.EMPTY;  
			
			for(GameObserver o : obsList) {
				o.moveExecStart(mov.getPlayer());
			}
			
			valid = mov.executeMove(board);
			
			if (valid) { 
 
				wonColor = rules.winningMove(mov, board); // Checks if there's a Counter Winner or not
				draw = rules.isDraw(mov.getPlayer(), board);
				
				if (draw) {
					finished = true; // hay empate, terminar
					this.winner = Counter.EMPTY;
				}
				else {
					if (wonColor == Counter.EMPTY) {
						increaseStack(mov); // Nobody wins, increase stack
						turn = rules.nextTurn(mov.getPlayer(), board); // Change turn

					}
					else {
						this.winner = wonColor;
						finished = true;
					} 
				}
			}
			else {
				// Si no hay movimiento disponibles para el color actual, cambiar
				for (GameObserver o : obsList) 
					o.onMoveError("This is an invalid movement, please try again");
			}
		}
		else {
			String err = "Invalid turn";
			for (GameObserver o : obsList)
				o.onMoveError(err);
			throw new InvalidMove(err);
		}

		// Notify all the observers that the move is finished
		for (GameObserver o : obsList) {
			o.moveExecFinished(board, currentPlayer, nextPlayer(currentPlayer));
		}
		if (finished) {
			for (GameObserver o : obsList)
				o.onGameOver(board, winner);
		}
		
		
		return valid;
	}
	
	// Create a window
	/* public void createWindow() {
		window = new MainWindow();
	}
	*/
	// Close and reset game

	public void closeGame() {
		for (GameObserver o : obsList) 
			o.onGameOver(board, winner);
	}

	public void resetGame() {
		for (GameObserver o : obsList) 
			o.reset(board, turn, false);
	}
	
	// Next Player
	
	private Counter nextPlayer(Counter turn) {
		Counter nextPlayer = null;
		
		if (turn == Counter.BLACK)
			nextPlayer = Counter.WHITE;
		else if (turn == Counter.WHITE)
			nextPlayer = Counter.BLACK;
		
		return nextPlayer;
	}
	public Counter getNextPlayer(){
		return nextPlayer(turn);
	}
	
	//  Undo and stack 
	
	public boolean undo() {
		boolean success = false;
		Move previousMove;
		
		if (!stack.isEmpty() && !finished) {
			success = true;
			previousMove = stack.getLast();
			stack.removeLast();		
			previousMove.undo(board); 
			turn = previousMove.getPlayer(); // Bug fixed!!! Actualizar el color del jugador!
			
			for (GameObserver o : obsList)
				o.onUndo(board, turn, success); // Avisar al observer que se ha modificado el undo			
		}
		else {
			for(GameObserver o : obsList)
				o.onUndoNotPossible();
		}
		return success;
	}
	
	public int undoTotal() {
		return this.stack.size();
	}
	
	public void increaseStack(Move movement) {
		stack.addLast(movement);
	}
	
	// Getters and setters 
	
	public boolean isFinished() {
		return this.finished;
	}
	
	public Counter getWinner() {
		return this.winner;
	}
	
	public Board getBoard() {
		return this.board;
	}

	public Counter getTurn() {
		return this.turn;
	}
	
	public GameRules getRules() {
		return rules;
	}
	
	public boolean getFinished(){
		return finished;
	}

	@Override
	public void addObserver(GameObserver o) {
		obsList.add(o);	
		o.reset(this.board, this.turn, !stack.isEmpty());
	}

	@Override
	public void removeObserver(GameObserver o) {
		obsList.remove(o);
	}

	public void setTurn(Counter c) {
		this.turn = c;
	}
	
}
