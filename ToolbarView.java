import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class ToolbarView extends JPanel implements IView {
	//fields
	//delete button
	private JButton delete_button;
	//scale label, scale value, scale slidder
	private JLabel scale_label;
	private int scale_value_num;
	private JLabel scale_value;
	private JSlider scale_slider;
	//rotate label, rotate value, rotate slider
	private JLabel rotate_label;
	private int rotate_value_deg;
	private JLabel rotate_value;
	private JSlider rotate_slider;
	//the model that this ciew is showing
	private DrawingModel model;

	//constructor
	public ToolbarView(DrawingModel model){
		this.model = model;
		this.counter(0);

		//initialize delete button
		delete_button = new JButton("delete");
		delete_button.setMaximumSize(new Dimension(70, 20));
		delete_button.setPreferredSize(new Dimension(70, 20));
		delete_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				model.removeSelectedStroke();
				model.notifyObservers();
			}
		});
		//initialize scale slider
		this.scale_label = new JLabel("Scale");
		this.scale_value_num = 100;
		this.scale_value = new JLabel(String.valueOf(this.scale_value_num/100.0));
		this.scale_slider = new JSlider(50, 200, this.scale_value_num);
		scale_slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider)e.getSource();
				scale_value_num = s.getValue();
				scale_value.setText(String.valueOf(scale_value_num/100.0));
				model.selected_setScale((float)(scale_value_num/100.0));
			}
		});
		//initialize rotate slider
		this.rotate_label = new JLabel("Rotate");
		this.rotate_value_deg = 0;
		this.rotate_value = new JLabel(String.valueOf(this.rotate_value_deg));
		this.rotate_slider = new JSlider(-180, 180, this.rotate_value_deg);
		rotate_slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				JSlider s = (JSlider)e.getSource();
				rotate_value_deg = s.getValue();
				rotate_value.setText(String.valueOf(rotate_value_deg));
				model.selected_setRotate((int)(rotate_value_deg));
			}
		});
		//set layout
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(delete_button);
		this.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(scale_label);
		this.add(scale_slider);
		this.add(scale_value);
		this.add(Box.createRigidArea(new Dimension(10,0)));
		this.add(rotate_label);
		this.add(rotate_slider);
		this.add(rotate_value);
	}
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                            RenderingHints.VALUE_ANTIALIAS_ON);
        model.drawAll(g2);
	}

	public void updateView(){}
	
	public void counter(int num){
		if(num==1){//1 means enable
			for (Component cp : this.getComponents() ){
			    cp.setEnabled(true);
			}
		}else{
			for (Component cp : this.getComponents() ){
			    cp.setEnabled(false);
			}
		}
	}
	public void updateValues(float scale, int rotate){
		this.scale_value_num = (int)(scale * 100.0);
		this.scale_slider.setValue(scale_value_num);
		this.scale_value.setText(String.valueOf(scale_value_num/100.0));
		this.rotate_value_deg = rotate;
		this.rotate_slider.setValue(rotate);
		this.rotate_value.setText(String.valueOf(rotate_value_deg));
	}
	public void setSelected(Shape s){}
}