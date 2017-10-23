import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.vecmath.*;

public class A2Basic {

	public static void main(String[] args){
		JFrame frame = new JFrame("A2-basic");

		// create Model and initialize it
		DrawingModel model = new DrawingModel();
		// create View, tell it about model and controller
		ToolbarView tbview = new ToolbarView(model);
		StatusbarView sbview = new StatusbarView(model);
		CanvasView cvview = new CanvasView(model);
		// tell Model about View.
		model.addView(tbview);
		model.addView(cvview);
		model.addView(sbview);
		// create the window
		JPanel p = new JPanel(new BorderLayout(1, 1));
		frame.getContentPane().add(p);
		p.add(tbview, BorderLayout.PAGE_START);
		p.add(cvview);
		p.add(sbview, BorderLayout.PAGE_END);

		frame.setPreferredSize(new Dimension(800,600));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}