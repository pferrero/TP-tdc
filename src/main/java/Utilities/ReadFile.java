package Utilities;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class ReadFile {
	
	private String filePath;
	
	public ReadFile() {}
	
	public int linesNumber() { 
		int lineNumber = 0;
		try (Stream<String> stream = Files.lines(Paths.get(filePath),Charset.defaultCharset())) {
        lineNumber = (int)stream.count();
     } catch (IOException e) {
        e.printStackTrace();
     }
		return lineNumber;
	}
	
	public String getLine(int lineNumber) {
		String line = "";
		 try (Stream<String> stream = Files.lines(Paths.get(filePath),Charset.defaultCharset())) {
	         line = stream.skip(lineNumber).findFirst().get();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
		 return line;
	}
	
	public String getFirstLine() {
		return getLine(0);
	}
	
	public String getLastLine() {
		return getLine(linesNumber()-1);
	}
	
	public void assingFile(String filePath) {
		this.setFilePath(filePath);
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	private void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void printFile() {
		try (Stream<String> stream = Files.lines(Paths.get(filePath),Charset.defaultCharset())) {
	         stream.forEach(System.out::println);
	      } catch (IOException e) {
	         e.printStackTrace();
	      }	
	}
}
