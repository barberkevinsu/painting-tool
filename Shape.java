/*
*  Shape: See ShapeDemo for an example how to use this class.
*
*/
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import javax.vecmath.*;

// simple shape model class
class Shape {

    // shape points
    ArrayList<Point2d> points;
    double west, east, north, south;
    Point2d centre;
    double stablestart_x = 0;
    double stablestart_y = 0;
    double stableend_x = 0;
    double stableend_y = 0;
    int signal = 0;
    double start_x = 0;
    double start_y = 0;
    double end_x = 0;
    double end_y = 0;

    public void clearPoints() {
        points = new ArrayList<Point2d>();
        pointsChanged = true;
    }

    public void setBorder(double x, double y){
        this.west = x;
        this.east = x;
        this.north = y;
        this.south = y;
    }

    public void setCentre(){
        this.centre = new Point2d((west + east)/2, (south + north)/2);
    }
    public Point2d getCentre(){
        return this.centre;
    }

    public void updateBorder(double x, double y){
        if(x<west){
            west = x;
        }
        if(x>east){
            east = x;
        }
        if(y<south){
            south = y;
        }
        if(y>north){
            north = y;
        }
    }
  
    // add a point to end of shape
    public void addPoint(Point2d p) {
        if (points == null) clearPoints();
        points.add(p);
        updateBorder(p.x, p.y);
        pointsChanged = true;
    }    

    // add a point to end of shape
    public void addPoint(double x, double y) {
        addPoint(new Point2d(x, y)); 
        updateBorder(x, y); 
    }

    public int npoints() {
        return points.size();
    }

    // shape is polyline or polygon
    Boolean isClosed = false; 

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }    

    // if polygon is filled or not
    Boolean isFilled = false; 

    public Boolean getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Boolean isFilled) {
        this.isFilled = isFilled;
    }    

    // drawing attributes
    Color colour = Color.BLACK;
    float strokeThickness = 2.0f;

    public Color getColour() {
        return colour;
    }

    public void setColour(Color colour) {
        this.colour = colour;
    }

    public float getStrokeThickness() {
        return strokeThickness;
    }

    public void setStrokeThickness(float strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    public void setScale(float new_scale){
        this.scale = new_scale;
    }
    public float getScale(){
        return this.scale;
    }
    public void setRotate(int new_rotate){
        this.rotate = new_rotate;
    }
    public int getRotate(){
        return this.rotate;
    }
    public void setStableStartPoint(){
        if(stablestart_x == 0){//the very start starting point
            this.stablestart_x = this.start_x;
            this.stablestart_y = this.start_y;
        }
    }
    public void setStableEndPoint(){
        if(this.signal<=1){
            this.stableend_x = this.end_x;
            this.stableend_y = this.end_y;
            signal ++;
        }
    }
    public void stableEndPointAddOn(){
        this.stableend_x += (end_x - start_x);
        this.stableend_y += (end_y - start_y);
    }
    public void setStartPoint(){
        this.start_x = this.end_x;
        this.start_y = this.end_y;
    }
    public void setEndPoint(double x, double y){
        this.end_x = x;
        this.end_y = y;
    }
    // shape's transform

    // quick hack, get and set would be better
    float scale = 1.0f;
    int rotate = 0;
    double translate_x = 0;
    double translate_y = 0;
    // some optimization to cache points for drawing
    Boolean pointsChanged = false; // dirty bit
    int[] xpoints, ypoints;
    int npoints = 0;

    void cachePointsArray() {
        xpoints = new int[points.size()];
        ypoints = new int[points.size()];
        for (int i=0; i < points.size(); i++) {
            xpoints[i] = (int)points.get(i).x;
            ypoints[i] = (int)points.get(i).y;
        }
        npoints = points.size();
        pointsChanged = false;
    }
    
    
    // let the shape draw itself
    // (note this isn't good separation of shape View from shape Model)
    public void draw(Graphics2D g2) {

        // don't draw if points are empty (not shape)
        if (points == null) return;

        // see if we need to update the cache
        if (pointsChanged) cachePointsArray();

        // save the current g2 transform matrix 
        AffineTransform M = g2.getTransform();
        // multiply in this shape's transform
        // (uniform scale)
        if(centre!=null){
            g2.translate(centre.x, centre.y);
        }
        g2.translate(stableend_x + (end_x - start_x) - stablestart_x, stableend_y + (end_y - start_y) - stablestart_y);
        double rad = Math.toRadians(rotate);
        g2.rotate(rad);
        g2.scale(scale, scale);
        if(centre!=null){
            g2.translate(-centre.x, -centre.y);
        }
        // call drawing functions
        g2.setColor(colour);            
        if (isFilled) {
            g2.fillPolygon(xpoints, ypoints, npoints);
        } else {
            // can adjust stroke size using scale
            g2.setStroke(new BasicStroke(strokeThickness / scale)); 
            if (isClosed)
                g2.drawPolygon(xpoints, ypoints, npoints);
            else
                g2.drawPolyline(xpoints, ypoints, npoints);
        }

        // reset the transform to what it was before we drew the shape
        g2.setTransform(M);            
    }
   
    // let shape handle its own hit testing
    // (x,y) is the point to test against
    // (x,y) needs to be in same coordinate frame as shape, you could add
    // a panel-to-shape transform as an extra parameter to this function
    // (note this isn't good separation of shape Controller from shape Model)    
    public boolean hittest(double px, double py)
    {   
        if (points != null) {
            if (centre!=null){
                px -= centre.x;
                py -= centre.y;
                px -= (stableend_x  - stablestart_x);
                py -= (stableend_y  - stablestart_y);
                double x = px; double y = py;
                double rad = Math.toRadians(-rotate);
                px = (double)x * Math.cos(rad) - y * Math.sin(rad);
                py = (double)x * Math.sin(rad) + y * Math.cos(rad);
                px *= 1/scale;
                py *= 1/scale;
                px += centre.x;
                py += centre.y;
                for(int i=1; i < points.size(); i++){
                    double x1 = points.get(i-1).x;
                    double y1 = points.get(i-1).y;
                    double x2 = points.get(i).x;
                    double y2 = points.get(i).y;
                    double dis = Line2D.ptSegDist(x1, y1, x2, y2, px, py);
                    if(dis <= 5){
                        return true;
                    }
                }
            }

        }
        return false;
    }
}
