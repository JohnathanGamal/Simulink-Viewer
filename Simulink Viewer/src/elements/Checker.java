package elements;

public class Checker {
	public static void checkName(String file) throws NotValidMDLFileException{
		if(!file.endsWith(".mdl")) {
			String type = file.substring(file.lastIndexOf("."));
			throw new NotValidMDLFileException("Invalid file type " + type);
		}
	}
	public static void checkContent(long len) throws EmptyMDLFileException{
		if(len == 0) {
			throw new EmptyMDLFileException();
		}
	}


}
