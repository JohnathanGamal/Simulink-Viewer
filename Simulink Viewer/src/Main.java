import elements.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {
	static ArrayList<Block> blocks = new ArrayList<>();
	static ArrayList<LineMDL> lines = new ArrayList<>();
	static Block[] blocksArray;
	static Line[] linesArray;

	public static void main(String[] args) {
		System.out.println("File name: ");
		Scanner sc = new Scanner(System.in);
		String filename = sc.nextLine();
		// String filename = args[0];
		String mdlFileContent = "";

		try {
			// Checking if file has the correct format
			Checker.checkName(filename);
			File file = new File(filename);
			// Checking if file has content
			Scanner reader = new Scanner(file);
			Checker.checkContent(file.length());
			while (reader.hasNextLine()) {

				String tmp = reader.nextLine();
				if (tmp.contains("<System>")) {
					mdlFileContent += tmp;
					while (true) {
						tmp = reader.nextLine();
						mdlFileContent += '\n' + tmp;
						if (tmp.contains("</System"))
							break;
					}
				}
			}

		} catch (EmptyMDLFileException e) {
			System.out.println(e);

		} catch (NotValidMDLFileException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}


		Parser.fileParse(mdlFileContent, blocks,lines);

		launch(args);

	}

	public double getW(Point2D a, Point2D b) {

		double width;
		width = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		return width;
	}
	public double getL(Point2D a, Point2D b) {
		double length;
		length = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		return length;
	}

		@Override
		public void start(Stage primaryStage)  {
			Pane pane = new Pane() ;
			pane.setPadding(new Insets(15, 15, 15, 15));
			pane.setStyle("-fx-background-color: #1c1c1c");

			//C:\Users\dodoh\Downloads\Example.mdl

			for ( Block b : blocks ) {

				Rectangle r = new Rectangle();
				Label label = new Label(b.getName()) ;
				r.setX( b.getTopleft().getX());
				r.setY( b.getTopleft().getY());
				r.setWidth(getW(b.getTopleft(), b.getTopRight()));
				r.setHeight((getL(b.getTopRight(), b.getBottomright())));
				label.setLayoutX(b.getBottomLeft().getX() - 5);
				label.setLayoutY(b.getBottomLeft().getY()+5);
				label.setStyle("-fx-text-fill: white");

				r.setStyle("-fx-fill: #1c1c1c; -fx-stroke: white");
				pane.getChildren().addAll(r,label);
			}

			for( LineMDL l : lines){

				// case no branches and straight line
				if ( l.getPath1() == null && l.getPath2() == null ) {

					Line line = new Line() ;

					for (Block b : blocks) {
						if(l.getSrc() == b.getSID() ) {

							if(b.isBlockMirror() == true) {

								line.setStartX(b.getTopleft().getX());
								line.setStartY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);

								b.setBlockMirror(false);
							}
							else {
								line.setStartX(b.getTopRight().getX());
								line.setStartY((b.getTopRight().getY() + b.getBottomright().getY()) / 2);
							}

						}

						if(l.getDest() == b.getSID() ) {
							if (b.isBlockMirror() == true) {

								line.setEndX(b.getTopRight().getX());
								line.setEndY((b.getTopRight().getY() + b.getBottomright().getY()) / 2);

								b.setBlockMirror(false);
							}
							else {
								line.setEndX(b.getTopleft().getX());
								line.setEndY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);
							}
						}
					}
					line.setStyle("-fx-stroke: white");
					pane.getChildren().addAll(line) ;
				}

				//In case the line is branched
				else if( l.getPath1() != null && l.getPath2() == null ) {

					Line line = new Line() ;
					Point2D pointShift = l.getPath1();
					for (Block b : blocks) {
						if(l.getSrc() == b.getSID() ) {

							if(b.isBlockMirror() == true) {

								line.setStartX(b.getTopleft().getX());
								line.setStartY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);

								b.setBlockMirror(false);
							}
							else {
								line.setStartX(b.getTopRight().getX());
								line.setStartY((b.getTopRight().getY() + b.getBottomright().getY()) / 2);
							}
							
							//This part of the line stops at the branching point
							line.setEndX( line.getStartX() + pointShift.getX() );
							line.setEndY( line.getStartY() + pointShift.getY());
						}

					}
						Circle c = new Circle(line.getEndX(), line.getEndY(), 3) ;
						c.setStyle("-fx-fill: white");

						line.setStyle("-fx-stroke: white");
						pane.getChildren().addAll(line, c) ;
						Point2D branchPoint = new Point2D(line.getEndX(),line.getEndY());

						for (Branch branch : l.branches) {

							Line branchLine = new Line();

							if(branch.getBreakingPoint()!= null ) {
								Point2D shift = branch.getBreakingPoint();
								branchLine.setStartX(branchPoint.getX());
								branchLine.setStartY(branchPoint.getY());
								
								branchLine.setEndX( branchLine.getStartX() + shift.getX() );
								branchLine.setEndY( branchLine.getStartY() + shift.getY());		
								branchLine.setStyle("-fx-stroke: white");
								pane.getChildren().addAll(branchLine) ;
								
								//Drawing the rest of the line till the block
								
								Line branchLine2 = new Line();
								branchLine2.setStartX(branchLine.getEndX());
								branchLine2.setStartY(branchLine.getEndY());
								
								for(Block b : blocks) {
									if(branch.getDest() == b.getSID() ) {
										if (b.isBlockMirror() == true) {

											branchLine2.setEndX(b.getTopRight().getX());
											//if(b.getNoInputPorts() != 1) {

										//	branchLine2.setEndY( b.getTopRight().getY() + ((b.getBottomright().getY() - b.getTopRight().getY() - 8)*branch.getInputOrder()/ (b.getNoInputPorts()+1)) );
										//	}
										//	else
												branchLine2.setEndY((b.getTopRight().getY() + b.getBottomright().getY())/2 + 10*(branch.getInputOrder() - (b.getNoInputPorts()+1)/2));
											 
											b.setBlockMirror(false);
										}
										else {
											branchLine2.setEndX(b.getTopleft().getX());
											if(b.getNoInputPorts() != 1) {
												branchLine2.setEndY((b.getTopRight().getY() + b.getBottomright().getY())/2 + 10*(branch.getInputOrder() - (b.getNoInputPorts()+1)/2));
											}
											else
												branchLine2.setEndY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);

										}
									}
								}
								branchLine2.setStyle("-fx-stroke: white");
								pane.getChildren().addAll(branchLine2) ;
								
								
								
							}
							else {
								
								for(Block b : blocks) {
									if(branch.getDest() == b.getSID() ) {
										if (b.isBlockMirror() == true) {

											branchLine.setEndX(b.getTopRight().getX());
											if(b.getNoInputPorts() != 1) {

												branchLine.setEndY((b.getTopRight().getY() + b.getBottomright().getY())/2 + 10*(branch.getInputOrder() - (b.getNoInputPorts()+1)/2));
											}
											else
												branchLine.setEndY((b.getTopRight().getY() + b.getBottomright().getY())/2);
											 
											b.setBlockMirror(false);
										}
										else {
											branchLine.setEndX(b.getTopleft().getX());
											if(b.getNoInputPorts() != 1) {
												branchLine.setEndY((b.getTopRight().getY() + b.getBottomright().getY())/2 + 10*(branch.getInputOrder() - (b.getNoInputPorts()+1)/2));
											}
											else
												branchLine.setEndY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);

										}
									}
								}
								
								branchLine.setStartX(branchPoint.getX());
								branchLine.setStartY(branchPoint.getY());
								branchLine.setStyle("-fx-stroke: white");
								pane.getChildren().addAll(branchLine) ;
								
							}
						}
				}

				else {

					Line line = new Line() ;
					Line line2 = new Line() ;
					Line line3 = new Line() ;

					for(Block b : blocks) {

						Point2D firstShift = l.getPath1() ;
						Point2D secondShift = l.getPath2() ;

						if (l.getSrc() == b.getSID()) {

							if(b.isBlockMirror() != true) {
									line.setStartX(b.getTopleft().getX());
									line.setStartY((b.getTopleft().getY() + b.getBottomLeft().getY()) / 2);

									b.setBlockMirror(false);
							}

							else {
								line.setStartX(b.getTopRight().getX());
								line.setStartY((b.getTopRight().getY() + b.getBottomright().getY()) / 2);
							}

							line.setEndX(line.getStartX() + firstShift.getX());
							line.setEndY(line.getStartY() + firstShift.getY());
							line.setStyle("-fx-stroke: white");

							line2.setStartX(line.getEndX());
							line2.setStartY(line.getEndY());
							line2.setEndX(line2.getStartX()+ secondShift.getX());
							line2.setEndY(line2.getStartY() + secondShift.getY());
							line2.setStyle("-fx-stroke: white");

							pane.getChildren().addAll(line, line2) ;
						}

					}
					for(Block b :blocks ) {

						if(l.getDest() == b.getSID() ) {
							if (b.isBlockMirror() == true) {

								line3.setEndX(b.getTopRight().getX());
								line3.setEndY(line2.getEndY()) ;

								b.setBlockMirror(false);
							}
							else {
								line3.setEndX(b.getTopleft().getX());
								line3.setEndY(line2.getEndY());
							}
						}

						line3.setStartX(line2.getEndX());
						line3.setStartY(line2.getEndY());
						line3.setEndY(line2.getEndY());
						line3.setStyle("-fx-stroke: white");

					}
					pane.getChildren().add(line3) ;
				}

			}

			primaryStage.setTitle("AdvancedProject");
			primaryStage.setScene(new Scene(pane));
			primaryStage.show();
		}
}


