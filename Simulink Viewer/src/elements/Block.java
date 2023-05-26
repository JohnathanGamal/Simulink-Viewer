package elements;

import javafx.geometry.Point2D;

public class Block {
	private String blockType;
	private String name;
	private int SID;
    private Point2D topLeft;
    private Point2D bottomRight;
	private Point2D topRight;
    private Point2D bottomLeft;
    private int noInputPorts  = 1;
    private int noOutputPorts = 1;
    private int zOrder;
    private boolean blockMirror = false;
    
    public Block() {
    	
    	
    }

	public Block(String blockType, String name, int sID) {
		this.blockType = blockType;
		this.name = name;
		SID = sID;
	}
	
	
	public boolean isBlockMirror() {
		return blockMirror;
	}

	public void setBlockMirror(boolean blockMirror) {
		this.blockMirror = blockMirror;
	}

	public int getSID() {
		return SID;
	}
	public void setSID(int sID) {
		SID = sID;
	}
	public int getNoInputPorts() {
		return noInputPorts;
	}
	public void setNoInputPorts(int no_InputPorts) {
		this.noInputPorts = no_InputPorts;
	}
	public int getNoOutputPorts() {
		return noOutputPorts;
	}
	public void setNoOutputPorts(int no_OutputPorts) {
		this.noOutputPorts = no_OutputPorts;
	}

	public String getBlockType() {
		return blockType;
	}
	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPosition(Point2D topLeft,Point2D bottomRight) {
		this.topLeft = MDLSystem.modifyPoint(topLeft);
		this.bottomRight = MDLSystem.modifyPoint(bottomRight);
		this.topRight = MDLSystem.modifyPoint(new Point2D(bottomRight.getX(),topLeft.getY()));
		this.bottomLeft =MDLSystem.modifyPoint( new Point2D(topLeft.getX(),bottomRight.getY()));
		
	}

    public Point2D getTopRight() {
		return topRight;
	}
	public Point2D getBottomLeft() {
		return bottomLeft;
	}
	
	public Point2D getTopleft() {
		return topLeft;
	}

	public Point2D getBottomright() {
		return bottomRight;
	}

	public int getZOrder() {
		return zOrder;
	}
	public void setZOrder(int z_Order) {
		this.zOrder = z_Order;
	}
	

}