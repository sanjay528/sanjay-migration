package com.hugo.source;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
*/import java.util.Properties;

import au.com.bytecode.opencsv.CSVReader;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import com.hugo.util.Bean;
import com.hugo.util.Util;


public class Activation {
	static Logger logger=Logger.getLogger(TestClient.class);
	static Properties prop=new Properties();
	public static int i;
	public static String file;
	public static String dest;
	public static long records=0;
	public static long totalRecords;
	static Bean bean = new Bean();
	/*private static String mysqlhost = null;
	private static String mysqlDatabase = null;
	private static String mysqlUsername = null;
	private static String mysqlPassword = null;
	*/
	public static HttpClient wrapClient(HttpClient base) {

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@SuppressWarnings("unused")
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				@SuppressWarnings("unused")
				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub

				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
		    sr.register(new Scheme("https", ssf,443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			return null;
		}
	}
	
		public static void createActivationProcess() throws FileNotFoundException, IOException
		{
			
			HttpClient httpClient = new DefaultHttpClient();
			
			httpClient = wrapClient(httpClient);
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			String tenantIdentifier = prop.getProperty("tenantIdentfier");
			/* mysqlhost = prop.getProperty("mysqlhost");
			 mysqlDatabase = prop.getProperty("mysqlDatabase");
			 mysqlUsername = prop.getProperty("mysqlUsername");
			 mysqlPassword = prop.getProperty("mysqlPassword");*/
			String credentials = username.trim() + ":" + password.trim();
	        //boolean flag = false;
			// encoding byte array into base 64
			byte[] encoded = Base64.encodeBase64(credentials.getBytes());
			// final JsonElement element = fromJsonHelper.parse(command.json());
			JSONObject activation = new JSONObject();
			JSONArray client =new JSONArray();
			JSONArray sale =new JSONArray();
			JSONArray  allocate=new JSONArray();
			JSONArray order =new JSONArray();
			JSONObject clientjson=new JSONObject();
			JSONObject salejson=new JSONObject();
			JSONObject allocatejson=new JSONObject();
			JSONObject allocatejson1=new JSONObject();
			JSONObject orderjson=new JSONObject();
			clientjson.put("officeId", bean.getOfficeId());
			clientjson.put("firstname", bean.getFirstname());
			clientjson.put("middlename", bean.getMiddlename());
			clientjson.put("lastname", bean.getLastname());
			clientjson.put("fullname", "");
			//clientjson.put("externalId",bean.getExternalid());
			clientjson.put("dateFormat", bean.getDateformat());
			clientjson.put("locale", bean.getLocale());
			clientjson.put("clientCategory", bean.getClientCategory());
			clientjson.put("active", bean.getActive());
			//client.put("activationDate", bean.getActivationDate());
			clientjson.put("addressNo", bean.getAddressNo());//property code
			clientjson.put("street", bean.getStreet());//property street
			//client.put("area",bean.getArea());
			clientjson.put("city", bean.getCity());//property city
			clientjson.put("state", bean.getState());//property state
			clientjson.put("country", bean.getCountry());//property country
			clientjson.put("zipCode", bean.getZipCode());//property zipcode
			clientjson.put("phone", bean.getPhone());
			clientjson.put("email", bean.getEmail());
			clientjson.put("flag","true");
			clientjson.put("login",bean.getLogin());
			clientjson.put("password",bean.getPassword());
			
			client.add(clientjson);
			
			salejson.put("dateFormat", bean.getDateformat());
			salejson.put("locale", bean.getLocale());
			salejson.put("itemId", bean.getItemId());
			salejson.put("quantity", bean.getQuantity());
			salejson.put("chargeCode", bean.getChargeCode());
		    salejson.put("totalPrice", bean.getTotalPrice());
			salejson.put("unitPrice", bean.getUnitPrice());
			salejson.put("discountId", bean.getDiscountId());
			salejson.put("saleDate", bean.getSaleDate());
			salejson.put("saleType", bean.getSaleType());
			salejson.put("officeId", bean.getOfficeId());
			
			JSONArray serialNumber=new JSONArray();
			allocatejson.put("itemMasterId", Integer.parseInt(bean.getItemId()));
			allocatejson.put("serialNumber", bean.getSerailNumber());
			allocatejson.put("status","allocated");
			allocatejson.put("isNewHw","Y");
			
			serialNumber.add(allocatejson);
			salejson.put("serialNumber",serialNumber);
	        
			sale.add(salejson);
			
			//allocatejson1.put("quantity",Integer.parseInt(bean.getQuantity()));
			allocatejson1.put("itemMasterId",Integer.parseInt(bean.getItemId()));
			//allocatejson1.put("serialNumber",serialNumber);
			
			allocate.add(allocatejson1);
			
			orderjson.put("planCode", bean.getPlan());
			orderjson.put("dateFormat", bean.getDateformat());
			orderjson.put("locale", bean.getLocale());
			orderjson.put("billAlign", bean.getBillingcycle());
			orderjson.put("paytermCode", bean.getBillFrequency());
			orderjson.put("start_date", bean.getStartDate());
			orderjson.put("contractPeriod",bean.getContractPeriod());

			order.add(orderjson);
			
			activation.put("client",client);
			activation.put("sale",sale);
			activation.put("allocate",allocate);
			activation.put("bookorder", order);
			try{
				StringEntity se = new StringEntity(activation.toString());
				HttpPost postRequest1 = new HttpPost(prop.getProperty("activationProcessQuery")
				.trim());

				postRequest1.setHeader("Authorization", "Basic " + new String(encoded));
				postRequest1.setHeader("Content-Type", "application/json");

				postRequest1.addHeader("X-Obs-Platform-TenantId", tenantIdentifier);
				postRequest1.setEntity(se);
				HttpResponse response1 = httpClient.execute(postRequest1);
				BufferedReader reader = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));
	
				String output1,output="";
				
				while ((output1 = reader.readLine()) != null) {
					output = output + output1;
					}
				if (response1.getStatusLine().getStatusCode() != 200) {
					org.json.JSONObject obj = new org.json.JSONObject(output);
					//obj.getJSONObject("errors").get("defaultUserMessage");
					String error = obj.getJSONArray("errors").getJSONObject(0).getString("defaultUserMessage");
					System.out.println(error);
					logger.error("CNO:"+bean.getCno()+"  HTTP error code : " + response1.getStatusLine().getStatusCode());	
					//bean.setClientid(null);
					bean.setResult(error);
					return;
					} else{
						String clientId = null;
						clientId = Util.getStringFromJson("resourceIdentifier", output);
						/*String query =" update b_clientuser set zebra_subscriber_id="+bean.getSubscriberId() +
								" where client_id="+clientId;
						processPostQuery(query);*/
						
						@SuppressWarnings("resource")
						CSVReader csvReader= new CSVReader(new FileReader(dest));
						@SuppressWarnings("unused")
						String[] row;
						row =csvReader.readNext();
						logger.info(clientId);
						logger.info("clientid is " + clientId);
						bean.setResult("success");
						bean.setClientid(clientId);	
					}
											
				}
			catch(Exception e)	{
				System.out.println(e);
				}
			httpClient.getConnectionManager().shutdown();
			}
		public static synchronized void readActivationProcessFile(String fileName, String destination) throws IOException{
			file=fileName;
			dest=destination;
			prop.load(new FileInputStream("Migrate.properties"));
			CSVReader csvReader= new CSVReader(new FileReader(fileName));
			String[] row;	
			row =csvReader.readNext();
			while((row =csvReader.readNext())!=null)
			{
				/*long ldate = System.currentTimeMillis();
				String date = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(
				ldate));*/
	
				//String regex=",";
				String[] readData=row.clone();
				String[] currentRowData =readData; //readData[0].split(regex);
				//String[] currentRowData=row.clone();
				//client data
				bean.setCno(currentRowData[0]);
				bean.setDateformat(currentRowData[1]);
				bean.setLocale(currentRowData[2]);
				bean.setActivationDate(currentRowData[3]);				
				bean.setFirstname(currentRowData[4]);
				bean.setLastname(currentRowData[5]);
				String d=currentRowData[6];
				bean.setOfficeId(Double.valueOf(d));
				bean.setClientCategory(currentRowData[7]);
				bean.setActive(currentRowData[8]);
				bean.setAddressNo(currentRowData[9]);
				bean.setStreet(currentRowData[10]);
				bean.setCity(currentRowData[11]);
				bean.setState(currentRowData[12]);
				bean.setCountry(currentRowData[13]);
				bean.setZipCode(currentRowData[14]);
				bean.setPhone(currentRowData[15]);
				bean.setEmail(currentRowData[16]);
				bean.setLogin(currentRowData[17]);
				bean.setPassword(currentRowData[18]);				
				//sale data
				bean.setitemId(currentRowData[19]);
				bean.setQuantity(currentRowData[20]);
				bean.setSaleDate(currentRowData[21]);
				bean.setChargeCode(currentRowData[22]);
				bean.setUnitPrice(currentRowData[23]);
				bean.setTotalPrice(currentRowData[24]);
				bean.setDiscountId(currentRowData[25]);
				bean.setSerailNumber(currentRowData[26]);
				
				//order data
				bean.setPlan(currentRowData[27]);
				bean.setStartDate(currentRowData[28]);
				bean.setContractPeriod(currentRowData[29]);
				bean.setBillFrequency(currentRowData[30]);
				bean.setBillingcycle(currentRowData[31]);
				bean.setSaleType(currentRowData[32]);
				/*String d1=currentRowData[33];
				bean.setSubscriberId(Long.valueOf(d1));
				*/
					//result
				createActivationProcess();
				writeCsv();
				}
			csvReader.close();
			}
		public static synchronized void writeCsv() throws IOException{
			
		boolean alreadyExists = new File(dest).exists();
			
				FileWriter writer1=new FileWriter(dest, true);
				BufferedWriter writer  = new BufferedWriter(writer1);
			if(!alreadyExists){
				writer.append("CNO,");
				writer.append("ClientId,");
				
				writer.append("Result");
				writer.write("\n");
			}
			
				writer.append(bean.getCno());
				writer.append(",");
				if(bean.getClientid()!=null){ 
					writer.append(bean.getClientid());
					writer.append(",");
				}else
				{
					writer.append(",");
				}
				if((bean.getOrderId())!=null){ 
					writer.append(bean.getOrderId());
					writer.append(";");
					}else
					{
						writer.append(" ");
					}
				writer.append(bean.getResult());
				writer.append("\n");
				bean.setClientid(null);
				bean.setOrderId(null);
				bean.setResult(null);
			writer.flush();
			writer.close();
		}
		
		/*private static int processPostQuery(String query){
			
			Connection connection = null;
			PreparedStatement ps = null;
			try{
				//connection = connectionSetUp();
				ps = connection.prepareStatement(query);
				int rs = ps.executeUpdate();
				//logger.info(rs);
				return rs;
				
			} catch (SQLException e) {
				logger.info("SQLException while executing the command..."+e.getMessage());
				return -1;
			
			} catch(NullPointerException npe){
				logger.info("NullPointerException while Using connection/statements.."+npe.getMessage());
				return -1;
			} finally{
				if(null != ps)
					try {
						ps.close();
					} catch (SQLException e) {
						logger.info("SQLException while closing the connection");
					}
				if(null != connection)
					try {
						connection.close();
					} catch (SQLException e) {
						logger.info("SQLException while closing the connection");
					}
			}
			
			
		}
		*/
	/*	private static Connection connectionSetUp(){
			
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			
				String url = "jdbc:mysql://" + mysqlhost + "/" + mysqlDatabase;
				
				return DriverManager.getConnection(url, mysqlUsername, mysqlPassword);
				
			} catch (InstantiationException e) {
				logger.error("InstantiationException in Connection Establish...");
				return null;
			
			} catch (IllegalAccessException e) {
				logger.error("IllegalAccessException in Connection Establish...");
				return null;
			
			} catch (ClassNotFoundException e) {
				
				logger.error("ClassNotFoundException in Connection Establish..."+e.getStackTrace());
				return null;
			
			} catch (SQLException e) {
				logger.error("SQLException in Connection Establish...");
				return null;
			}	
		}*/
}



