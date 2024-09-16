package utilities;

import base.Base;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLCert extends Base {
	
	public static InputStream inputStream;
	
	public static RestAssuredConfig sslconfig() {
		String keyPassphrase = "";
		SSLContext sslContext = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		inputStream = loader.getResourceAsStream("adr_new1.p12");
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(inputStream, keyPassphrase.toCharArray());

			sslContext = SSLContexts.custom()
			        .loadKeyMaterial(keyStore, null)
			        .build();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
		SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
		SSLConfig sslConfig = new SSLConfig();
		sslConfig = sslConfig.sslSocketFactory(socketFactory);
		sslConfig.relaxedHTTPSValidation();

		RestAssuredConfig config = new RestAssuredConfig();
		config = config.sslConfig(sslConfig);
		    
//		String response = RestAssured.given().config(cfg).baseUri("https://mtlsuat-ib.bankvic.com.au/ultracsauthMTLSuat/.well-known/openid-configuration").get().asPrettyString();
//		System.out.println(response);
		
		return config;
	}

}
