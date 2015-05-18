package com.hugo.source;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

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

public class Simple {
	static Logger logger=Logger.getLogger(Simple.class);
	static Properties prop=new Properties();
	public static int i;
	public static String file;
	public static String dest;
	public static long records=0;
	public static long totalRecords;
	static Bean bean = new Bean();
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
	
		public static void createActivationProcess()
		{
			HttpClient httpClient = new DefaultHttpClient();
			
			httpClient = wrapClient(httpClient);
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			String tenantIdentifier = prop.getProperty("tenantIdentfier");
			String credentials = username.trim() + ":" + password.trim();
	        boolean flag = false;
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
			clientjson.put("addressNo", bean.getAddressNo());
			clientjson.put("street", bean.getStreet());
			//client.put("area",bean.getArea());
			clientjson.put("city", bean.getCity());
			clientjson.put("state", bean.getState());
			clientjson.put("country", bean.getCountry());
			clientjson.put("zipCode", bean.getZipCode());
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
				String clientId = null;
				while ((output1 = reader.readLine()) != null) {
					output = output + output1;
					}
				if (response1.getStatusLine().getStatusCode() != 200) {
					org.json.JSONObject obj = new org.json.JSONObject(output);
					//obj.getJSONObject("errors").get("defaultUserMessage");
					String error = obj.getJSONArray("errors").getJSONObject(0).getString("defaultUserMessage");
					System.out.println(error);
					logger.error("CNO:"+bean.getCno()+"  HTTP error code : " + error);//response1.getStatusLine().getStatusCode());	
					bean.setResult(error);
					return;
					} else{
						clientId = Util.getStringFromJson("resourceIdentifier", output);
						CSVReader csvReader= new CSVReader(new FileReader(dest));
						String[] row;
						row =csvReader.readNext();
									clientId = Util.getStringFromJson("resourceIdentifier", output);

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
					String[] readData=row.clone();
				String[] currentRowData =readData; //readData[0].split(regex);
				
				System.out.println(currentRowData[0]);
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
					//result
				createActivationProcess();
				writeCsv();
				}
			csvReader.close();
			}
		public static synchronized  void writeCsv() throws IOException
		{
			boolean alreadyExists = new File(dest).exists();
			
			//CsvWriter writer=new CsvWriter(new FileWriter(dest,true),',');
			//CsvWriter writer =new CsvWriter(new FileWriter(dest, true),',');
			FileWriter writer=new FileWriter(dest, true);
			
			if(!alreadyExists){
				writer.write("CNO,");
				writer.write("Result,");
				writer.write("ClientId,");
				writer.write("\n");
			}
			
				/*long ldate1 = System.currentTimeMillis();
				String date1 = new SimpleDateFormat("HH:mm:ss.SSS")
						.format(new Date(ldate1));
				bean.setLastTime(date1);*/
				writer.write(bean.getCno());
				writer.write(",");
				writer.write(bean.getResult());
				writer.write(",");
				if(bean.getClientid()!=null){ 
				writer.write(bean.getClientid());
				writer.write(",");
				}else
				{
					writer.write(",");
				}
				writer.write("\n");
				bean.setClientid(null);
				bean.setResult(null);
			writer.flush();
			writer.close();
			
			}

		
}

