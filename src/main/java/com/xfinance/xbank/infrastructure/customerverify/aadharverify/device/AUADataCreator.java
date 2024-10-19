package com.xfinance.xbank.infrastructure.customerverify.aadharverify.device;

import com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.AUAData;
import com.xfinance.xbank.infrastructure.customerverify.aadharverify.auth.uid_auth_request_data._1.Pid;

import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

public class AUADataCreator {
	private Encrypter encrypter;
	private HashGenerator hashGen;

	public AUADataCreator(Encrypter encrypter) {
		this.hashGen = new HashGenerator();
		this.encrypter = encrypter;
	}

	public AUAData prepareAUAData(String uid, String terminalId, Pid pid) {
		try {
			String pixXML = createPidXML(pid);

			byte[] pidXmlBytes = pixXML.getBytes();
			byte[] sessionKey = this.encrypter.generateSessionKey();
			byte[] encXMLPIDData = this.encrypter.encryptUsingSessionKey(sessionKey, pidXmlBytes);

			byte[] hmac = this.hashGen.generateSHA256Hash(pidXmlBytes);
			byte[] encryptedHmacBytes = this.encrypter.encryptUsingSessionKey(sessionKey, hmac);

			byte[] encryptedSessionKey = this.encrypter.encryptUsingPublicKey(sessionKey);

			SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
			String certificateIdentifier = df2.format(this.encrypter.getCertExpiryDate());

			String demoXML = getDemoXML(pid);
			byte[] hashedDemoXML = StringUtils.leftPad("0", 64, '0').getBytes();
			if (StringUtils.isNotBlank(demoXML)) {
				hashedDemoXML = hashGen.generateSHA256Hash(demoXML.getBytes());
			}

			return new AUAData(uid, terminalId, encXMLPIDData, encryptedHmacBytes, encryptedSessionKey, hashedDemoXML, certificateIdentifier);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	private String getDemoXML(Pid pid) {
		StringWriter sw = new StringWriter();

		try {
			JAXBContext.newInstance(Pid.class).createMarshaller().marshal(pid, sw);
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}

		String pidXML = sw.toString();

		try {
			int start = pidXML.indexOf("<Demo>");
			int end = pidXML.indexOf("</Demo>") + ("<Demo>".length() + 1);
			return pidXML.substring(start, end);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	private String createPidXML(Pid pid) {
		StringWriter pidXML = new StringWriter();
		try {
			JAXBContext.newInstance(Pid.class).createMarshaller().marshal(pid, pidXML);
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		return pixXML.toString();
	}
}