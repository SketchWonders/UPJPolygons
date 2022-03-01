import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.*;
import javafx.scene.shape.*; //imports .Polygon
import javafx.scene.paint.*;
import javafx.beans.value.ObservableValue;
import java.io.*;
import java.util.*;
import javafx.scene.transform.Translate;
/*
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
*/
import javafx.event.*;
import javafx.scene.input.MouseEvent;

public class Assign3Controller {
	private enum Mode {DRAW, SELECTED, NONE};
	private Mode mode = Mode.NONE;
	private Polygon selShape = null; //current selected shape
	private Polygon currentShape; //shape being drawn -- defined by array of pts
	//old and new points for transform
	public double oldX=0;
	public double oldY=0;
	public double newX;
	public double newY;
	public int clickCount;
	
	private int numberOfPoints = 0;
	private ArrayList<Double> points = new ArrayList<Double>();
	private double [] pointList;
	private double newPoints [];
	//private	ObservableList<Double> pointList = FXCollections.observableArrayList(points);
	
	
    private double X1, Y1;
	
	@FXML
    private TextField errorMessageTextField;

    @FXML
    private Button drawPolygonButton;

    @FXML
    private Button deletePolygonButton;

    @FXML
    private Button clearPolygonButton;

    @FXML
    private Pane paneToDisplay;

    @FXML
    void clearPolygonClicked(ActionEvent event) {
		System.out.println("Clear Polygon Button Pressed");
		//errorMessageTextField.setText("Shapes cleared");
		paneToDisplay.getChildren().clear();
		mode = Mode.NONE;
    }

    @FXML
    void deletePolygonClicked(ActionEvent event) {
		System.out.println("Delete Polygon Button Pressed");
		if(mode == Mode.SELECTED){
			System.out.println("Remove Successful: " + paneToDisplay.getChildren().remove(selShape));
		}
		mode = Mode.NONE;
    }

    @FXML
    void drawPolygonClicked(ActionEvent event) {
		System.out.println("Draw Polygon Button Pressed");
		mode = Mode.DRAW;
		pointList = new double [0];
		deletePolygonButton.setDisable(true);
		clearPolygonButton.setDisable(true);
		drawPolygonButton.setDisable(true);
		currentShape = new Polygon ();
    }

    @FXML
    void mouseClickedPane(MouseEvent event) {
		System.out.print("Mouse Clicked in Pane");
		clickCount++;
    }

    @FXML
    void mouseDraggedPane(MouseEvent event) {
		System.out.println("Mouse Dragged in Pane");
		try{
			if( selShape != null){
				System.out.println("to be dragged");
				selShape.setStroke(Color.RED);
				Translate t = new Translate();
				System.out.println("(" + oldX+ ", " + oldY+ ")");
				newX = event.getX();
				newY = event.getY();
				if(paneToDisplay.getBoundsInLocal().getMinX()> (selShape.getBoundsInParent().getMinX()+(newX-oldX))){
					System.out.println("You can't move it that far left.");
					throw (new OutofBoundsException ());
				}
				if(paneToDisplay.getBoundsInLocal().getMinY()> (selShape.getBoundsInParent().getMinY()+(newY-oldY))){
					System.out.println("You can't move it that far down.");
					throw (new OutofBoundsException ());
				}
				if(paneToDisplay.getBoundsInLocal().getMaxX()< (selShape.getBoundsInParent().getMaxX()+(newX-oldX))){
					System.out.println("You can't move it that far right.");
					throw (new OutofBoundsException ());
				}
				if(paneToDisplay.getBoundsInLocal().getMaxY()< (selShape.getBoundsInParent().getMaxY()+(newY-oldY))){
					System.out.println("You can't move it that far up.");
					throw (new OutofBoundsException ());
				}
				System.out.println(paneToDisplay.getBoundsInLocal().toString());
				System.out.println("(" + newX+ ", " + newY+ ")");
				t.setX(newX-oldX);
				t.setY(newY-oldY);
				selShape.getTransforms().addAll(t);
				oldX = newX;
				oldY = newY;
			}
		}
		catch (OutofBoundsException e){
			selShape.setStroke(Color.BLACK);
			mode = mode.NONE;
			drawPolygonButton.setDisable(false);
			selShape = null;
		}
    }

    @FXML
    void mousePressedPane(MouseEvent event) {
		
		System.out.println("Mouse Pressed in Pane");
		try{
			X1 = event.getX();
			Y1 = event.getY();
			if(mode == Mode.DRAW){
				numberOfPoints++;
				double [] temp = new double [pointList.length];
				for(int i = 0; i< pointList.length; i++){
					temp [i] = pointList[i];
				}
				pointList = new double [pointList.length + 2];
				for(int i = 0; i< pointList.length-2; i++){
					pointList [i] = temp[i];
				}
				pointList [pointList.length-2] = X1;
				pointList [pointList.length-1] = Y1;
				System.out.println("X: " + X1+  " Y: " + Y1 + " Length: " + pointList.length);

				if(numberOfPoints <3){
					if(event.getClickCount()>=2){
						System.out.println("Clicked twice");
						deletePolygonButton.setDisable(false);
						clearPolygonButton.setDisable(false);
						System.out.println("That is a line. Not a polygon...");
					}
				}
				else{
					if(event.getClickCount()==1){
						System.out.println("Clicked once");
						paneToDisplay.getChildren().remove(currentShape);
						currentShape = new Polygon (pointList);
						currentShape.setStroke(Color.RED);
						currentShape.setFill(new Color(1, 1, 1, 0));
						paneToDisplay.getChildren().add(currentShape);
					}
					else if(event.getClickCount()>=2){
						System.out.println("Clicked twice");
						currentShape.setStroke(Color.BLACK);
						deletePolygonButton.setDisable(false);
						clearPolygonButton.setDisable(false);
						drawPolygonButton.setDisable(false);
						mode = Mode.NONE;
					}
				}
				/*
				for (int i = 0; i< pointList.length; i++){
					System.out.print(pointList[i] + " ");
				}
				System.out.println("");
				*/
			}
			else if (mode != Mode.DRAW && paneToDisplay.getChildren().size() >0){
				System.out.println("Shape Selected");
				mode = Mode.SELECTED;
				if(selShape!= null){
					selShape.setStroke(Color.BLACK);
				}
				selShape = (Polygon)(getSelected(X1, Y1));
				oldX = event.getX();
				oldY = event.getY();
				selShape.setStroke(Color.RED);
			}
			else{
				System.out.println("Yo. There's nothing in the pane. Pls don't crash");
			}
		}
		catch (NullPointerException e){
			mode = Mode.NONE;
			System.out.println("If you were trying to select a shape, there isn't one there.");
			if(selShape !=null){
				selShape.setStroke(Color.BLACK);
				selShape = null;
			}
		}
    }

    @FXML
    void mouseReleasedPane(MouseEvent event) {
		System.out.println("Mouse Released in Pane");
		if(mode == Mode.NONE){
			for(Node n: paneToDisplay.getChildren()){
				((Polygon)n).setStroke(Color.BLACK);
			}
		}
		if(mode == Mode.SELECTED && !(selShape.getBoundsInParent().contains(event.getX(), event.getY()))){
			if(selShape != null){
				selShape.setStroke(Color.BLACK);
				selShape = null;
				mode = Mode.NONE;
				drawPolygonButton.setDisable(false);
			}
		}
    }
	
	Shape getSelected (double x, double y){
		Polygon p;
		int count = 0;
		Polygon n;
		for(int i = (clickCount%paneToDisplay.getChildren().size()); count< paneToDisplay.getChildren().size(); count++){
			n = (Polygon)(paneToDisplay.getChildren().get(i));
			if(n.getBoundsInParent().contains(x,y)){
				//p = (Polygon) n;
				//System.out.println("Yep, was selected.");
				//return p;
				return n;
			}
			i = (i+1)%paneToDisplay.getChildren().size();
		}
		return null;
	}
	
	public void initialize(){
		deletePolygonButton.setDisable(true);
		clearPolygonButton.setDisable(true);
	}
}

class OutofBoundsException extends IllegalStateException{
	public OutofBoundsException(){
		super("value is not in window");
	}
}