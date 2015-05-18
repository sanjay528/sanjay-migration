package com.hugo.source;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import com.hugo.util.Bean;

public class Main {
	static Logger logger = Logger.getLogger(Main.class);
	static Properties prop = new Properties();
	public static String methodName;
	public static String fileName;
	public static int countno;
	public static int endno;
	public static int i;
	public static String destination;
	static Bean bean = new Bean();

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		try {
			prop.load(new FileInputStream("Migrate.properties"));
			if (args.length > 0) {
				methodName = args[0];
				fileName = args[1];		
				destination=args[2];
			}
		if (methodName.trim().equalsIgnoreCase("Activation")) {
				String w1 = prop.getProperty("startClientno");
				String w2 = prop.getProperty("endClientno");
				
				Activation.readActivationProcessFile(fileName,destination);
			}
		if (methodName.trim().equalsIgnoreCase("Client")) {
				String w1 = prop.getProperty("startClientno");
				String w2 = prop.getProperty("endClientno");
				//int x1 = Integer.parseInt(w1);
				//int x2 = Integer.parseInt(w2);
				
				TestClient.readClient(fileName,destination);

			}
			
		/*	if (methodName.trim().equalsIgnoreCase("simple")) {
				String w1 = prop.getProperty("startClientno");
				String w2 = prop.getProperty("endClientno");
				//int x1 = Integer.parseInt(w1);
				//int x2 = Integer.parseInt(w2);
				
			Simple.readActivationProcessFile(fileName,destination);

			}*/
			
		
		} catch (Exception e) {

			logger.error("failure :Exceptions happen!", e);

		}
	}
}
