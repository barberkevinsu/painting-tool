import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.vecmath.*;

//view interface
interface IView {
	public void updateView();
	public void counter(int num);
	public void updateValues(float scale, int rotate);
	public void setSelected(Shape s);
}

public class DrawingModel {
	//fields
	////a list of 3 views
	private ArrayList<IView> views = new ArrayList<IView>();
	private ArrayList<Shape> strokes = new ArrayList<Shape>();//store a list of strokes
	private Shape selected = null;
	//methods
	public void addView(IView view){
		views.add(view);
		view.updateView();
	}
	public void addStroke(Shape shape){
		strokes.add(shape);
		views.get(2).counter(1);
		views.get(2).updateView();
	}
	public Shape whatShouldHighlighted(double x, double y) {
		for(int i = strokes.size() - 1;i >= 0; i--){
			if(strokes.get(i).hittest(x,y)){
				views.get(0).counter(1);//enable toolbar
				//let toolbar read the selected stroke's scale and rotate value
				float my_scale_value = strokes.get(i).getScale();
				int my_rotate_value = strokes.get(i).getRotate();
				views.get(0).updateValues(my_scale_value, my_rotate_value);
				return strokes.get(i);
			}
		}
		views.get(0).counter(0);//disable toolbar
		return null;
	}
	//notify 3 view obeservers
	public void notifyObservers() {
		for(IView view: this.views){
			view.updateView();
		}
	}
	public void drawAll(Graphics2D g2){
		//System.out.println("Drawing all: " + strokes.size());
		//System.out.println(selected);
		for(Shape s: this.strokes){
			if(s==selected){
				s.setColour(Color.YELLOW);
				s.setStrokeThickness(6.0f);
			}

			s.draw(g2);

			if(s==selected){
				s.setColour(Color.BLACK);
				s.setStrokeThickness(2.0f);
				s.draw(g2);
			}

		}
	}
	public void setSelected(Shape selected){
		this.selected = selected;
		views.get(2).setSelected(selected);
		notifyObservers();
	}
	public void setStableStartPoint(){
		if(selected!=null){
			selected.setStableStartPoint();
		}
	}
	public void setStableEndPoint(){
		if(selected!=null){
			selected.setStableEndPoint();
		}
	}
	public void stableEndPointAddOn(){
		if(selected!=null){
			selected.stableEndPointAddOn();
		}
	}
	public void setStartPoint(){
		if(selected!=null){
			selected.setStartPoint();
		}
	}
	public void setEndPoint(double x, double y){
		if(selected!=null){
			selected.setEndPoint(x, y);
		}
	}
	public Shape selectedStatus(){
		return this.selected;
	}
	public void removeSelectedStroke(){
		if(selected!=null){
			strokes.remove(selected);
			views.get(2).counter(-1);
			removeSelected();
		}
	}
	public void removeSelected(){
		this.selected = null;
		views.get(2).setSelected(null);
		notifyObservers();
	}
	public void selected_setScale(float new_scale){
		if(selected!=null){
			selected.setScale(new_scale);
			notifyObservers();
		}
	}
	public void selected_setRotate(int new_rotate){
		if(selected!=null){
			selected.setRotate(new_rotate);
			notifyObservers();
		}
	}






}