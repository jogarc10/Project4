package tp.pr5.logic;

import tp.pr5.Resources.Counter;

public class Board implements ReadOnlyBoard {
	private int width, 
				height, 
				occupiedCells;
	@SuppressWarnings("unused")
	private boolean full;
	private Counter [][] board;
	
	// Constructor with two parameters for Board
	
	public Board(int tx, int ty) {
		this.width = tx; // Row
		this.height = ty;  // column
		
		if ((tx < 1) || (ty < 1)) { /* Creates a 1x1 board if dimensions are wrong */
			width = 1;
			height = 1;
		}
		board = new Counter[height][width];
		emptyCells(); // restart the board
		occupiedCells = 0;
	}

	// Getters and setters for private attributes
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; } 
	
	public Counter getPosition(int x, int y) {
		Counter color = Counter.EMPTY; 
		if ((x >= 1 && x <= width) && (y >= 1 && y <= height)) {
			color = board[y - 1][x - 1];
		}
		return color;		
	}
	
	// Setters for private attributes

	public void setFull(Boolean full) {
		this.full = full; // indicates if a board is full or not.
	}
	
	public void emptyCells() {
		for (int y = 0; y < height; y++) 
			for (int x = 0; x < width; x++) 
				board[y][x] = Counter.EMPTY; // Reset the board with empty Cells
	}
		
	public void setPosition(int tx, int ty, Counter counter) {
		
		if ((tx >= 1 && tx <= width) && (ty >= 1 && ty <= height)) 
			if (board[ty-1][tx-1] == Counter.EMPTY && counter != Counter.EMPTY) {
				occupiedCells++;
			}
			else if (board[ty-1][tx-1] != Counter.EMPTY && counter == Counter.EMPTY) {
				occupiedCells--;
			}
		board[ty - 1][tx - 1] = counter;//cambiado x por y
	}
	
	// Methods for checking if a board neither column is full or not.
	
	public boolean isFull() {
		int boardDim = width * height; 
		return occupiedCells == boardDim;
	}
	
	public boolean isFullColumn(int Column) {
		int i = 1, j = Column;
		boolean fullColumn = true;

		while ((i <= height) && (fullColumn)) {
			if (getPosition(i, j) == Counter.EMPTY) {
				fullColumn = false;
			}			
			i++;
		}
		return fullColumn;
	}
	
	// Printers for the Board
	
	/*
	 * 
	 * This methos is not used in the fourth assigment
	 */
	public void printBoard() {
		String line = "";
		
		
		for (int y = 1; y <= height; y++) {
			line = tabLines(y);	
			System.out.println(line);
		}	

		line = "";
		for (int i = 0; i <= width; i++) {
			if (i == 0) line += "+";
			else if ((i > 0) && ( i <= width)) line += "-"; 
			
			if (i == width) line += "+";
		}
		System.out.println(line);
		line = "";
		
		for (int i = 0; i <= width + 1; i++) {
			if (i == 0) {
				line += " ";
			}
			else if ((i > 0) && ( i <= width))
			line += i; 
			
		}
		System.out.println(line);
		System.out.println("");
	}
		
	public String tabLines(int y) {
		String line = "";
		
		line += "|"; 
		
		for (int x = 1; x <= width; x++) 
		{	
			if (getPosition(x, y) == Counter.EMPTY)  {
				line += " ";
			}
			else if (getPosition(x, y) == Counter.BLACK) {
				line +=  "B";
			}
			else if (getPosition(x, y) == Counter.WHITE) {
				line += "W";
			}			 
		}
		line += "|";
		
		return line;
	}

	
}
