package Utilities;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	private Pattern pattern;
	private Matcher matcher;
	
	public Validator() {}
	
	public Validator(String pattern) 
	{
		this.pattern = Pattern.compile(pattern);
	}
	
	public ArrayList<String> getAllMatchs(String expresion, String pattern) 
	{
		setRegex(expresion, pattern);
		ArrayList<String> results = new ArrayList<String>();
		while (matcher.find())
	        results.add(matcher.group());
		return results;
	}
	
	public ArrayList<Integer> getAllPositionMatchs(String expresion, String pattern) 
	{
		setRegex(expresion, pattern);
		ArrayList<Integer> results = new ArrayList<Integer>();
		while (matcher.find())
	        results.add(matcher.start());
		return results;
	}
	
	public boolean isValid(String expresion, String pattern)
	{
		setRegex(expresion, pattern);
		return this.matcher.matches();
	}

	private void setRegex(String expresion, String pattern) {
		this.pattern = Pattern.compile(pattern);
		this.matcher = this.pattern.matcher(expresion);
	}
}
