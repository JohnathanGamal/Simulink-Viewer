package elements;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class LineMDL { 
	private int zOrder;
    private int src;
    private int dest;
    private int inputOrder =1;
    private int outputOrder =1;
    private Point2D path1;
    private Point2D path2;
    public ArrayList<Branch> branches = new ArrayList<>() ;    
    
   public Point2D getPath1() {
		return path1;
	}


	public void setPath1(Point2D path1) {
		this.path1 =  path1;
	}


	public Point2D getPath2() {
		return path2;
	}


	public void setPath2(Point2D path2) {
		this.path2 =  path2;
	}


public double getPointX (Point2D n) {
        return n.getX() ;
   }

   public double getPointY(Point2D n) {
        return n.getY() ;
   }


    
   public LineMDL() {


    }
    public LineMDL(int src, int dest) {
         this.src = src;
         this.dest = dest;
        }


    public int getZOrder() {
     return zOrder;
    }

    public void setZOrder(int zOrder) {
     this.zOrder = zOrder;
    }

    public int getSrc() {
     return src;
    }

    public void setSrc(int src) {
     this.src = src;
    }

    public int getDest() {
     return dest;
    }

    public void setDest(int dest) {
     this.dest = dest;
    }

    public int getInputOrder() {
     return inputOrder;
    }

    public void setInputOrder(int inputOrder) {
     this.inputOrder = inputOrder;
    }

    public int getOutputOrder() {
     return outputOrder;
    }

    public void setOutputOrder(int outputOrder) {
     this.outputOrder = outputOrder;
    }


   }

