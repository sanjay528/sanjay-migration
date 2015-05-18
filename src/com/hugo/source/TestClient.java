package com.hugo.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

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

import au.com.bytecode.opencsv.CSVReader;

import com.hugo.util.Bean;
import com.hugo.util.Util;
import org.json.*;

public class TestClient 
{
	static Logger logger=Logger.getLogger(TestClient.class);
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
	
	public static void createClient()
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
		
		JSONObject client = new JSONObject();
		client.put("entryType",bean.getEntryType());
		client.put("officeId", bean.getOfficeId());
		client.put("clientCategory", bean.getClientCategory());
		client.put("addressNo", bean.getBuildingCode());
		//	client.put("fullname", "");
		client.put("street", bean.getStreet());
		client.put("city", bean.getCity());
		client.put("state", bean.getState());
		client.put("country", bean.getCountry());
		client.put("zipCode", bean.getZipCode());
		client.put("title", bean.getTitle());
		client.put("firstname", bean.getFirstname());
		client.put("lastname", bean.getLastname());
		client.put("phone", bean.getPhone());
		client.put("email", bean.getEmail());
		//client.put("externalId",bean.getExternalid());
		client.put("locale", bean.getLocale());
		client.put("active", bean.getActive());
		client.put("dateFormat", bean.getDateformat());
		client.put("activationDate", bean.getActivationDate());
		client.put("flag",flag);
		//client.put("login",bean.getLogin());
		//client.put("password",bean.getPassword());
		
		System.out.println("------------" + client.toString());
		
    try{
		StringEntity se = new StringEntity(client.toString());

		HttpPost postRequest1 = new HttpPost(prop.getProperty("clientQuery").trim());
		postRequest1.setHeader("Authorization", "Basic " + new String(encoded));
		postRequest1.setHeader("Content-Type", "application/json");
		postRequest1.addHeader("X-Obs-Platform-TenantId", tenantIdentifier);
		postRequest1.setEntity(se);

		HttpResponse response1 = httpClient.execute(postRequest1);
		BufferedReader br1 = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));
		String output;
		if (response1.getStatusLine().getStatusCode() != 200) {
			logger.error("CNO:"+bean.getCno()+" =Failed : HTTP error code : " + response1.getStatusLine().getStatusCode());
			
			while ((output = br1.readLine()) != null) {
				     //logger.error(output);
				     org.json.JSONObject jsonObject = new org.json.JSONObject(output);
					JSONArray array=jsonObject.getJSONArray("errors");
					org.json.JSONObject jsonObject1=array.getJSONObject(0);
					String data=jsonObject1.getString("developerMessage");
					System.out.println(data);
					bean.setResult("failure: " + data);
				}
			
			return;
		}
	
		String clientId = null;

		logger.info("Output from Server .... \n");
		while ((output = br1.readLine()) != null) {

		clientId = Util.getStringFromJson("resourceIdentifier", output);

			logger.info(clientId);
			logger.info("clientid is " + clientId);
			bean.setResult("success");
			bean.setClientid(clientId);
			//logger.info("***********************************");

		}
     }catch(Exception e)
		{
			System.out.println(e);
		}
		httpClient.getConnectionManager().shutdown();

	}

	public static synchronized void readClient(String fileName, String destination) throws FileNotFoundException, IOException 
	{
		file=fileName;
		dest=destination;
		prop.load(new FileInputStream("Migrate.properties"));
		CSVReader csvReader= new CSVReader(new FileReader(fileName));
		String[] row;
	
		row =csvReader.readNext();
		
		
			while((row =csvReader.readNext())!=null)
			{
				
				/*String[] readData=row.clone();
				String[] currentRowData = readData[0].split(",");*/
				String[] readData=row.clone();
				String[] currentRowData =readData;
				bean.setCno(currentRowData[0]);
				bean.setEntryType(currentRowData[1]);
				String d=currentRowData[2];
				bean.setOfficeId(Double.valueOf(d));
				bean.setClientCategory(currentRowData[3]);
				bean.setBuildingCode(currentRowData[4]);
				bean.setStreet(currentRowData[5]);
				bean.setCity(currentRowData[6]);
				bean.setState(currentRowData[7]);
				bean.setCountry(currentRowData[8]);
				bean.setZipCode(currentRowData[9]);
				bean.setTitle(currentRowData[10]);
				bean.setFirstname(currentRowData[11]);
				bean.setLastname(currentRowData[12]);
				bean.setPhone(currentRowData[13]);
				bean.setEmail(currentRowData[14]);
				bean.setLocale(currentRowData[15]);
				bean.setActive(currentRowData[16]);
				bean.setDateformat(currentRowData[17]);
				bean.setActivationDate(currentRowData[18]);
				bean.setFlag(currentRowData[19]);
				System.out.println(bean.getCno());
				createClient();
				writeCsv();
		
			}
			csvReader.close();
		
	}
public static synchronized  void writeCsv() throws IOException
{
	boolean alreadyExists = new File(dest).exists();
	
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
