package com.xfinance.xbank.infrastructure.customerverify.aadharverify.models.uid_auth_request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="Auth", propOrder = {"tkn", "skey", "uses", "data", "hmac"})
public class UidAuthRequest {
	@XmlElement(name="Tkn")
	protected Tkn tkn;

	@XmlElement(name="Skey", required=true)
	protected Skey skey;

	@XmlElement(name="uses", required=true)
	protected Uses uses;

	@XmlElement(name="data", required=true)
	protected byte[] data;

	@XmlElement(name="hmac")
	protected byte[] hmac;

	@XmlAttribute(required=true)
	protected String uid;

	@XmlAttribute(required=true)
	protected String ac;

	@XmlAttribute(required=true)
	protected String tid;

	@XmlAttribute
	protected String ver;

	@XmlAttribute
	protected String txn;

	@XmlAttribute
	protected String lk;

	@XmlAttribute(required=true)
	protected String sa;

	public Tkn getTkn() {
		return tkn;
	}

	public void setTkn(Tkn value) {
		this.tkn = tkn;
	}

	public Skey getSkey() {
		return skey;
	}

	public void setSkey(Skey value) {
		this.skey = value;
	}

	public Uses getUses() {
		return uses;
	}

	public void setUses(Uses uses) {
		this.uses = uses;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getHmac() {
		return hmac;
	}

	public void setHmac(byte[] hmac) {
		this.hmac = ((byte[])hmac);
	}

	public String getUid() {
		return uid;
	}

	public void setString(String uid) {
		this.uid = uid;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getTxn() {
		return txn;
	}

	public void setTxn(String txn) {
		this.txn = txn;
	}

	public String getLk() {
		return lk;
	}

	public void setLk(String lk) {
		this.lk = lk;
	}

	public String getSa() {
		return sa;
	}

	public void setSa(String sa) {
		this.sa = sa;
	}
}