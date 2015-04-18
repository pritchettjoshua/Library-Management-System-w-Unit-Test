package assignment7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
/**
 * Implements a basic unit testing framework to test the tester methods
 * for a class specidied at the command line
 */
public class UTF {
	private static Class testClass;
	private static String methodName;
	private static String input;
	
	/**
	 * Creates a Class object from the given input, creates an array that stores
	 * all of the given class's methods, and then invokes them on an instance of
	 * the class if they are identified as tester methods (public,non-static, void and no arguments). 
	 */
	public static void main(String[]args){
		Scanner scanner = new Scanner(System.in);
		 input = scanner.nextLine();
		
		
		
			try {
				testClass = Class.forName(input);
				Method[] testMethods= testClass.getMethods();
				
				for(Method m:testMethods){
					methodName = m.toString();
					if(methodName.contains("test")
							&& !methodName.contains("static")
							&& methodName.contains("void")
							&& methodName.contains("public")
							&& methodName.contains("()"))
					{
						Object testObject = testClass.getConstructor().newInstance();
						m.invoke(testObject);
						System.out.println("The method '" + m.toString() + "' was tested.");
					}
				}
			} catch (IllegalAccessException e) {
				System.out.println("The method " + methodName + " does not have access to this class.");
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid arguments for the method " + methodName + ".");
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				System.out.println("There was an invocation target exception for the method " + methodName + ".");
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.out.println("An instance of the class " + testClass.toString() + "cannot be created.");
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.out.println("The method " + methodName + " was not found.");
				e.printStackTrace();
			} catch (SecurityException e) {
				System.out.println("There was a security exception for the method " + methodName + ".");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("The class " + input + " was not found.");
				//e.printStackTrace();
			}finally{  //do nothing
				
			}
		
	}

}
