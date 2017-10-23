import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

import javax.vecmath.*;
import java.util.ArrayList;

class CanvasView extends JComponent implements IView {
	//fields
	private DrawingModel model;
	Shape shape;//store the stoke that is being drawed
	int dragged = 0;
	int pressed_on_stroke = 0;
	//int startPoint_x = 0;
	//int startPoint_y = 0;
	//private Point M = new Point(); // mouse point
    //private Point C = new Point(150, 150); // click point

    public CanvasView (DrawingModel model) {
    	//init model
    	this.model = model;
    	setBackground(Color.WHITE);
    	//add mouse listener
    	this.addMouseListener(new MouseAdapter(){
    		public void mousePressed(MouseEvent e){
    			//remove selected in model wherever clicked
    			model.removeSelected();
    			Shape selected = model.whatShouldHighlighted(e.getX(), e.getY());
    			if(selected==null){
    				//means it doesn't click 5 pixels around a stroke
    				shape = new Shape();
    				shape.setBorder(e.getX(), e.getY());//init border
    				repaint();
    			}else{
    				model.setSelected(selected);
    				model.setEndPoint(e.getX(), e.getY());
    				model.setStartPoint();
    				model.setStableStartPoint();
    				model.setStableEndPoint();
    				pressed_on_stroke = 1;
    				repaint();
    			}
    		}
    		public void mouseReleased(MouseEvent e) {
    			if(dragged == 1){
       				model.addStroke(shape);
       				shape.setCentre();
       				shape = null;
       			}
       			dragged = 0;
       			pressed_on_stroke = 0;
       			model.setEndPoint(e.getX(), e.getY());
       			model.stableEndPointAddOn();
       			model.setStartPoint();
       			model.setStableEndPoint();//once for all
       			repaint();
    		}
    	});
    	this.addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e) {
            	if(pressed_on_stroke == 1){
            		model.setEndPoint(e.getX(), e.getY());
            	}else{
	                shape.addPoint(e.getX(), e.getY());
	                dragged = 1;
	            }
                repaint();      
            }
        }); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                            RenderingHints.VALUE_ANTIALIAS_ON);
        model.drawAll(g2);
        if (shape != null)
            shape.draw(g2);
    }
    public void updateView(){
    	repaint();
	}
	public void counter(int num){

	}
	public void updateValues(float scale, int rotate){}
	public void setSelected(Shape s){}
}