package lifegame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class LifeFrame extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private static final int cell_width = 20;
	private BoardModel model;
	private boolean is_running = false;
	
	public LifeFrame(BoardModel model, String title) {
		super(title);
		this.model = model;
		setSize((model.getRows() + 1) * cell_width + 5, (model.getCols() + 1) * cell_width + 5);	
	}
	
	public void setBoard() {
		setVisible(true);
		Insets insets = getInsets(); 
		setVisible(false);
		
		JPanel cell_panel = new BoardView(model);
		cell_panel.setLayout(null);
		cell_panel.setBounds(5, 5, model.getRows() * cell_width, model.getCols() * cell_width);
		cell_panel.setBackground(new Color(45, 47, 53));
		
		JPanel tool_panel = new JPanel();
		tool_panel.setLayout(null);
		tool_panel.setBounds(5, cell_panel.getBounds().height + 10, cell_panel.getBounds().width, 35);
		tool_panel.setBackground(new Color(32, 34, 37));
		
		addButtons(tool_panel, cell_panel);
		
		int insets_height = insets.top + insets.bottom;
		setResizable(true);
		setSize((model.getRows() + 1) * cell_width + 5, insets_height + (model.getCols() + 1) * cell_width + 5 + tool_panel.getBounds().height);
		setMinimumSize(new Dimension((model.getRows() + 1) * cell_width + 5, insets_height + (model.getCols() + 1) * cell_width + 5 + tool_panel.getBounds().height));
		
		Container content = getContentPane();
		content.add(cell_panel, BorderLayout.CENTER);
		content.add(tool_panel, BorderLayout.CENTER);
	}
	
	private void addButtons(JPanel button_panel, JPanel cell_panel) {
		ActionListener new_game_button_action = new ActionListener() {
			public void actionPerformed(ActionEvent event){
				BoardModel model = new BoardModel(20, 30);
				LifeFrame frame = new LifeFrame(model, "lifegame");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setBoard();
				frame.setVisible(true);
				Thread thread = new Thread(frame);
				thread.start();
				
				frame.addWindowListener(new WindowListener() {
					@Override
					public void windowOpened(WindowEvent e) {}

					@Override
					public void windowClosing(WindowEvent e) {
						thread.interrupt();
					}

					@Override
					public void windowClosed(WindowEvent e) {}

					@Override
					public void windowIconified(WindowEvent e) {}

					@Override
					public void windowDeiconified(WindowEvent e) {}

					@Override
					public void windowActivated(WindowEvent e) {}

					@Override
					public void windowDeactivated(WindowEvent e) {}
				});
			}
		};
		ActionListener next_button_action = new ActionListener() {
			public void actionPerformed(ActionEvent event){
				model.next();
				if (model.isUndoable())
					button_panel.getComponents()[2].setEnabled(true);
				cell_panel.repaint();
			}
		};
		ActionListener undo_button_action = new ActionListener() {
			public void actionPerformed(ActionEvent event){
				JButton button = (JButton)event.getSource();
				model.undo();
				cell_panel.repaint();
				if (!model.isUndoable())
					button.setEnabled(false);
			}
		};
		ActionListener auto_button_action = new ActionListener() {
			public void actionPerformed(ActionEvent event){
				is_running = !is_running;
				JButton button = (JButton)event.getSource();
				if (is_running) 
					button.setText("Stop");
				else
					button.setText("Auto");
				model.next();
				if (model.isUndoable())
					button_panel.getComponents()[2].setEnabled(true);
			}
		};
		
		LinkedHashMap<String, ActionListener> button_sources = new LinkedHashMap<String, ActionListener>();
		button_sources.put("New Game", new_game_button_action);
		button_sources.put("next", next_button_action);
		button_sources.put("undo", undo_button_action);
		button_sources.put("Auto", auto_button_action);
		
		int i = 0;
		for (Map.Entry<String, ActionListener> button_source : button_sources.entrySet()) {
			JButton button = new JButton(button_source.getKey());
			if (button_source.getKey() == "undo") button.setEnabled(false);
			button.setBackground(new Color(94, 124, 226));
			button.setForeground(new Color(255, 255, 255));
			button.setBounds(10 + i * (this.getWidth() / 4 - 5), button_panel.getBounds().y + 5, (this.getWidth() / 4 - 20), 30);
			button.addActionListener(button_source.getValue());
			button_panel.add(button);
			i++;
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (!is_running) {
					Thread.sleep(1000);
					continue;
				}
				Thread.sleep(500);
				model.next();
				this.repaint();
			} catch (InterruptedException e) {
				break;
			} 
		}
	}
}
