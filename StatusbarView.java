import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.LayoutManager;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.*;

class StatusbarView extends JPanel implements IView {
	//fields
	private DrawingModel model;
	private JLabel label = new JLabel("0 stroke");
	private int stroke_num;
	/*private int point_num;
	private float scale_value;
	private int rotate_value;*/
	private Shape selected;
	//constructor
	public StatusbarView(DrawingModel model){
		this.model = model;
		this.stroke_num = 0;
		//initialize label
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(this.label);

	}
	public void updateView(){
		if(stroke_num == 1||stroke_num == 0){
			if(selected!=null){
				int point_num = selected.points.size();
				float scale_value = selected.getScale();
				int rotate_value = selected.getRotate();
				this.label.setText(String.valueOf(this.stroke_num) 
					+ " stroke, Selection( " + point_num 
					+ " points, scale: " + scale_value 
					+ ", rotation: " + rotate_value + ")");
			}else{
				this.label.setText(String.valueOf(this.stroke_num) + " stroke");
			}
		}else{
			if(selected!=null){
				int point_num = selected.points.size();
				float scale_value = selected.getScale();
				int rotate_value = selected.getRotate();
				this.label.setText(String.valueOf(this.stroke_num) 
					+ " strokes, Selection( " + point_num 
					+ " points, scale: " + scale_value 
					+ ", rotation: " + rotate_value + ")");
			}else{
				this.label.setText(String.valueOf(this.stroke_num) + " strokes");
			}
		}
	}
	public void counter(int num){
		this.stroke_num+=num;
	}
	public void setSelected(Shape s){
		selected = s;
	}
	public void updateValues(float scale, int rotate){}
}