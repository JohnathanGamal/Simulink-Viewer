package elements;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.geometry.Point2D;

public class Parser {

	public static void fileParse(String inputData, ArrayList<Block> blocks, ArrayList<LineMDL> lines)
	{
		Scanner read = new Scanner(inputData);
		String tmp = read.nextLine();
		String data = "";

		while (read.hasNextLine()) {
			if (tmp.contains("Location")) {

				String dimensionsString = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));
				String[] tokens = dimensionsString.split(",");
				Point2D topLeft = new Point2D(Double.parseDouble(tokens[0].trim()),
						Double.parseDouble(tokens[1].trim()));
				Point2D bottomRight = new Point2D(Double.parseDouble(tokens[2].trim()),
						Double.parseDouble(tokens[3].trim()));

				MDLSystem.setDimensions(topLeft, bottomRight);

			}

			else if (tmp.contains("<Block")) {
				data += tmp;
				while (true) {
					tmp = read.nextLine();
					data += '\n' + tmp;
					if (tmp.contains("</Block>")) {
						break;
					}
				}
				blocks.add(blockParser(data));
				data = "";

			} else if (tmp.contains("<Line>")) {
				data += tmp;
				while (true) {
					tmp = read.nextLine();
					data += '\n' + tmp;
					if (tmp.contains("</Line>")) {
						break;
					}
				}

				lines.add(lineParser(data));
				data = "";

			}  

				tmp = read.nextLine();

		}

	}

	private static Block blockParser(String data) {
		Scanner read = new Scanner(data);
		String tmp = read.nextLine();
		Block block = new Block();
		while (read.hasNextLine()) {
			if (tmp.contains("<Block BlockType")) {
				// Parse the String to get Blocktype,name,SID
				String typeTarget = "BlockType=\"";
				String nameTarget = "Name=\"";
				String SIDTarget = "SID=\"";
				String blockType = tmp.substring(tmp.indexOf(typeTarget) + typeTarget.length(), tmp.indexOf("\" Name"));
				String name = tmp.substring(tmp.indexOf(nameTarget) + nameTarget.length(), tmp.indexOf("\" SID"));
				String SID_tmp = tmp.substring(tmp.indexOf("SID=\"") + SIDTarget.length(), tmp.indexOf("\">"));
				int SID = Integer.parseInt(SID_tmp);
				SID_tmp = null;
				// Assign the data found to the block
				block.setName(name);
				block.setBlockType(blockType);
				block.setSID(SID);

			} else if (tmp.contains("\"Position\"")) {
				String positionString = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));
				String[] tokens = positionString.split(",");
				Point2D topLeft = new Point2D(Double.parseDouble(tokens[0]), Double.parseDouble(tokens[1]));
				Point2D bottomRight = new Point2D(Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));

				block.setPosition(topLeft, bottomRight);

			} else if (tmp.contains("ZOrder")) {
				String Ztarget = "\"ZOrder\">";
				String ZOrderString = tmp.substring(tmp.indexOf(Ztarget) + Ztarget.length(), tmp.indexOf("</P>"));
				int ZOrder = Integer.parseInt(ZOrderString);
				block.setZOrder(ZOrder);

			} else if (tmp.contains("\"Ports\"")) {
				String portString = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));
				if (portString.length() >= 3) {
					String[] tokens = portString.split(",");
					block.setNoInputPorts(Integer.parseInt(tokens[0].trim()));
					block.setNoOutputPorts(Integer.parseInt(tokens[1].trim()));

				} else if (portString.length() == 1) {
					block.setNoInputPorts(Integer.parseInt(portString.trim()));
					block.setNoOutputPorts(0);
				}
			}
			else if(tmp.contains("\"BlockMirror\"")){
				String blockTarget = "\"BlockMirror\">";
				String blockMirrorStatus = tmp.substring(tmp.indexOf(blockTarget) + blockTarget.length(), tmp.indexOf("</P>")).toLowerCase();;
				if(blockMirrorStatus.equals("on")) {
					block.setBlockMirror(true);
					
				}
			}
			tmp = read.nextLine();

		}
		return block;
	}

	private static LineMDL lineParser(String data) {
		Scanner read = new Scanner(data);
		String tmp = read.nextLine();
		LineMDL line = new LineMDL();
		while (read.hasNextLine()) {

			if (tmp.contains("ZOrder")) {
				String Ztarget = "\"ZOrder\">";
				String ZOrderString = tmp.substring(tmp.indexOf(Ztarget) + Ztarget.length(), tmp.indexOf("</P>"));
				int ZOrder = Integer.parseInt(ZOrderString);
				line.setZOrder(ZOrder);

			} else if (tmp.contains("Src")) {
				String srctarget = "\"Src\">";
				String srcString = tmp.substring(tmp.indexOf(srctarget) + srctarget.length(), tmp.indexOf("#"));
				int src = Integer.parseInt(srcString);
				line.setSrc(src);

				String outputOrderTarget = "out:";
				String outputOrderString = tmp.substring(tmp.indexOf("out:") + outputOrderTarget.length(),
						tmp.indexOf("</P>"));
				int outputOrder = Integer.parseInt(outputOrderString);
				line.setOutputOrder(outputOrder);

			} else if (tmp.contains("Dst")) {
				String dstTarget = "\"Dst\">";
				String dstString = tmp.substring(tmp.indexOf(dstTarget) + dstTarget.length(), tmp.indexOf("#"));
				int dst = Integer.parseInt(dstString);
				line.setDest(dst);

				String inputOrderTarget = "in:";
				String inputOrderString = tmp.substring(tmp.indexOf("in:") + inputOrderTarget.length(),
						tmp.indexOf("</P>"));
				int inputOrder = Integer.parseInt(inputOrderString);
				line.setInputOrder(inputOrder);

			} else if (tmp.contains("Points")) {
				String pointsString = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));
				if (pointsString.contains(";")) {
					String[] coordinates = pointsString.split(";");
					String[] startCo = coordinates[0].split(",");
					String[] endCo = coordinates[1].split(",");
					Point2D point1 = new Point2D(Double.parseDouble(startCo[0]), Double.parseDouble(startCo[1]));
					Point2D point2 = new Point2D(Double.parseDouble(endCo[0]), Double.parseDouble(endCo[1]));
					line.setPath1(point1);
					line.setPath2(point2);
				
				} 
				else {
					String[] endCo = pointsString.split(",");
					Point2D point = new Point2D(Double.parseDouble(endCo[0]), Double.parseDouble(endCo[1]));
					line.setPath1(point);

				}

			}
			else if (tmp.contains("<Branch>")) {
				data += tmp;
				while (true) {
					tmp = read.nextLine();
					data += '\n' + tmp;
					if (tmp.contains("</Branch>")) {
						break;
					}
				}

				line.branches.add(branchParser(data));
				data = "";

			}

			tmp = read.nextLine();

		}
		return line;

	}

	private static Branch branchParser(String data) {
		Scanner read = new Scanner(data);
		String tmp = read.nextLine();
		Branch branch = new Branch();
		while (read.hasNextLine()) {

			if (tmp.contains("ZOrder")) {
				String Ztarget = "\"ZOrder\">";
				String ZOrderString = tmp.substring(tmp.indexOf(Ztarget) + Ztarget.length(), tmp.indexOf("</P>"));
				int ZOrder = Integer.parseInt(ZOrderString);
				branch.setZOrder(ZOrder);

			}

			else if (tmp.contains("Dst")) {
				String dstTarget = "\"Dst\">";
				String dstString = tmp.substring(tmp.indexOf(dstTarget) + dstTarget.length(), tmp.indexOf("#"));
				int dst = Integer.parseInt(dstString);
				branch.setDest(dst);

				String inputOrderTarget = "in:";
				String inputOrderString = tmp.substring(tmp.indexOf("in:") + inputOrderTarget.length(),
						tmp.indexOf("</P>"));
				int inputOrder = Integer.parseInt(inputOrderString);
				branch.setInputOrder(inputOrder);
			} else if (tmp.contains("Points")) {
				String pointsString = tmp.substring(tmp.indexOf('[') + 1, tmp.indexOf(']'));

				String[] tokens = pointsString.split(",");
				Point2D breakingPoint = new Point2D(Double.parseDouble(tokens[0].trim()),
						Double.parseDouble(tokens[1].trim()));
				branch.setBreakingPoint(breakingPoint);

			}

			tmp = read.nextLine();

		}
		return branch;

	}

}
