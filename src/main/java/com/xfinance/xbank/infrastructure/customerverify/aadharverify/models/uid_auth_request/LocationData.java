
package com.xfinance.xbank.infrastructure.customerverify.aadharverify.uid_auth_request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="LocationData")
public class LocationData {

	@XmlAttribute
	protected String lat;

	@XmlAttribute
	protected String lng;

	@XmlAttribute
	protected String vtc;

	@XmlAttribute
	protected String subdist;

	@XmlAttribute
	protected String dist;

	@XmlAttribute
	protected String stat;

	@XmlAttribute
	protected String pc;

	public String getLat() {
		return lat;
	}

	public void setLat(String value) {
		this.lat = value;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String value) {
		this.lng = lng;
	}

	public String getVtc() {
		return vtc;
	}

	public void setVtc(String value) {
		this.vtc = value;
	}

	public String getSubdist() {
		return subdist;
	}

	public void setSubdist(String value) {
		this.subdist = value;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String value) {
		this.dist = value;
	}

	public void setState(String value) {
		this.state = value;
	}

	public String getState() {
		return state;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String value) {
		this.pc = value;
	}
}