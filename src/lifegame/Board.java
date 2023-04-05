package lifegame;

public class Board {
	private int cols;
	private int rows;
	private boolean[][] cells;
	
	public Board(BoardModel b) {
		this.cols = b.getCols();
		this.rows = b.getRows();
		this.cells = b.getCells();
	}
	
	public int getCols() {
		return this.cols;
	}
	
	public int getRows() {
		return this.rows;
	}
	
	public boolean[][] getCells() {
		return this.cells;
	}
}
