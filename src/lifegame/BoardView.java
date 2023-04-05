package lifegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class BoardView extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private static final int cell_width = 20;
	private ArrayList<MouseEvent> previous_events;
	private BoardModel model;
	
	public BoardView(BoardModel model) {
		this.model = model;
		this.previous_events = new ArrayList<MouseEvent>();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(70, 74, 83));
		for (int row = 0; row <= model.getRows(); row++) {
			g.drawLine(row * cell_width, 0, row * cell_width, model.getCols() * cell_width);
		}
		for (int col = 0; col <= model.getCols(); col++) {
			g.drawLine(0, col * cell_width, model.getRows() * cell_width, col * cell_width);
		}
		g.setColor(new Color(188, 188, 188));
		for (int row = 0; row < model.getRows(); row++) {
			for (int col = 0; col < model.getCols(); col++) {
				if (model.isAlive(row, col))
					g.fillRect(row * cell_width, col * cell_width, cell_width, cell_width);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		boolean is_reverse = false;
		for (MouseEvent event : previous_events) {
			if (event.getX() / cell_width == e.getX() / cell_width && event.getY() / cell_width == e.getY() / cell_width) 
				is_reverse = true;
		}
		if (!is_reverse) {
			if (e.getX() / cell_width < model.getRows() && e.getY() / cell_width < model.getCols() &&
				e.getX() / cell_width >= 0 && e.getY() / cell_width >= 0)
				model.changeCellState(e.getX() / cell_width, e.getY() / cell_width);
			this.repaint();
		}
		previous_events.add(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		previous_events.add(e);
		model.changeCellState(e.getX() / cell_width, e.getY() / cell_width);
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		previous_events.clear();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		previous_events.clear();
	}
}
