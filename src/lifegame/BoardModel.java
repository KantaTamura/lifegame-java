package lifegame;

import java.util.ArrayDeque;

public class BoardModel {
	private int cols;
	private int rows;
	private int current_save_board_num;
	private boolean[][] cells;
	public ArrayDeque<Board> previous_boards;
	
	public BoardModel(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		this.current_save_board_num = 0;
		this.cells = new boolean[rows + 2][cols + 2];
		this.previous_boards = new ArrayDeque<Board>();
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
	
	public boolean isAlive(int row, int col) {
		return this.cells[row + 1][col + 1];
	}
	
	synchronized public void printForDebug() {
		for (int row = 1; row < this.rows - 1; row++) {
			for (int col = 1; col < this.cols - 1;col++) {
				System.out.print(this.cells[row][col] ? "*" : ".");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	synchronized public void changeCellState(int row, int col) {
		this.cells[row + 1][col + 1] = !this.cells[row + 1][col + 1];
	}

	synchronized public void saveBoard() {
		this.previous_boards.addLast(new Board(this));
		if (this.current_save_board_num == 32) {
			this.previous_boards.pollFirst();
			return;
		}
		this.current_save_board_num++;
	}
	
	synchronized public void next() {
		this.saveBoard();
		boolean[][] buf_cells = new boolean[this.rows + 2][this.cols + 2];
		for (int row = 1; row < this.rows + 1; row++) {
			for (int col = 1; col < this.cols + 1; col++) {
				int count = 0;
				count += cells[row - 1][col - 1] ? 1 : 0;
			    count += cells[row    ][col - 1] ? 1 : 0;
			    count += cells[row + 1][col - 1] ? 1 : 0;
			    count += cells[row - 1][col    ] ? 1 : 0;
			    count += cells[row + 1][col    ] ? 1 : 0;
			    count += cells[row - 1][col + 1] ? 1 : 0;
			    count += cells[row    ][col + 1] ? 1 : 0;
			    count += cells[row + 1][col + 1] ? 1 : 0;
			    if(cells[row][col] && count <= 1) {
			    	buf_cells[row][col] = false;
			    } else if(cells[row][col] && (count == 2 || count == 3)) {
			    	buf_cells[row][col] = true;
			    } else if(cells[row][col] && count >= 4) {
			    	buf_cells[row][col] = false;
			    } else if(!cells[row][col] && count == 3) {
			    	buf_cells[row][col] = true;
			    }
			}
		}
		this.cells = buf_cells;
	}
	
	synchronized public boolean isUndoable() {
		return current_save_board_num != 0;
	}
	
	synchronized public void undo() {
		if (!this.isUndoable()) return;
		Board previous_board = this.previous_boards.pollLast();
		this.current_save_board_num--;
		this.rows = previous_board.getRows();
		this.cols = previous_board.getCols();
		this.cells = previous_board.getCells();
	}
}
