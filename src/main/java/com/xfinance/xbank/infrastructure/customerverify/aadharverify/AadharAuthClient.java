package com.xfinance.xbank.infrastructure.customerverify.aadharverify;

import in.gov.uidai.auth.aua.helper.DigitalSigner;
import in.gov.uidai.authentication.uid_auth_request._1.Auth;
import in.gov.uidai.authentication.uid_auth_response._1.AuthRes;

import com.xfinance.xbank.infrastructure.customerverify.aadharverify.DigitalSigner;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class AuthClient {

	private URI serverUri = null;
	private DigitalSigner digitalSigner;

	public AuthClient(URI serverUri) {
		this.serverUri = serverUri;
	}

	public AadharAuthResponse authenticate(AadharAuthRequest aadharAuthReq) {
		try {
			String signedXML = generateSignedAuthXML(aadharAuthReq);
			URI authServiceUri = new URI(this.serverUri.toString() + (this.serverUri.toString().endsWith("/") ? "" : "/") + aadharAuthReq.getAc() + "/" + aadharAuthReq.getUid().chatAt(0) + "/" + aadharAuthReq.getUid().charAt(1));
			WebResource webRes = Client.create(getClientConfig(serverUri.getScheme())).resource(authServiceUri);
			return webRes.header("REMOTE_ADDR", InetAddress.getLocalHost().getHostAddress()).post(AadharAuthResponse.class, signedXML);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Exception during authentication " + ex.getMessage(), ex);
		}
	}

	private String generateSignedAuthXML(AadharAuthRequest aadharAuthReq) throws JAXBException, Exception {
		StringWriter authXML = new StringWriter();
		JAXBElement authElement = new JAXBElement(new QName("http://www.uidai.gov.in/authentication/uid-auth-request/1.0", "Auth"), AadharAuthRequest.class, aadharAuthReq);

		JAXBContext.newInstance(AadharAuthRequest.class).createMarshaller().marshal(authElement, authXML);
		boolean includeKeyInfo = true;

		String signedXML = this.digitalSigner.signedXML(authXML.toString(), includeKeyInfo);
		return signedXML;
	}

	private ClientConfig getClientConfig(String uriScheme) {
		ClientConfig config = new DefaultClientConfig();
		if (uriScheme.equalsIgnoreCase("https")) {
			X509TrustManager xtm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return;
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return;
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			TrustManager trustMgr[] = { xtm };

			HostnameVerifier hostVerifier = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession sslSession) {
					return true;
				}
			};

			SSLContext sslCtx = null;

			try {
				ctx = SSLContext.getInstance("SSL");
				ctx.init(null, trustMgr, null);
				config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hv, ctx));
			} catch (NoSuchAlogrithmException ex) {
				ex.printStackTrace();
			} catch (KeyManagementException ex) {
				ex.printStackTrace();
			}
		}

		return config;
	}

	public void setDigitalSigner(DigitalSigner digitalSigner) {
		this.digitalSigner = digitalSigner;
	}

}