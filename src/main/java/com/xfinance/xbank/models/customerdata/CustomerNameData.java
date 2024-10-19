
import java.io.Serializable;
import java.utils.ArrayList;
import java.utils.HashSet;
import java.utils.List;
import java.utils.Set;

import javax.persistence.GeneratedValue;

public class CustomerNameData implements Serializable {

	@Id
	@GeneratedValue(name="customer_name_data")
	private int customer_name_data_id;

	// Name fields
	private String f_name;
	private String m_name;
	private String l_name;

	CustomerNameData(
		String f_name,
		String m_name,
		String l_name
	) {
		this.f_name = f_name;
		this.m_name = m_name;
		this.l_name = l_name;
	}

	public String getFName() {
		return f_name;
	}

	public String getMName() {
		return m_name;
	}

	public String getLName() {
		return l_name;
	}

	public void setFName(String f_name) {
		this.f_name = f_name;
	}

	public void setMName(String m_name) {
		this.m_name = m_name;
	}

	public void setLName(String l_name) {
		this.l_name = l_name;
	}
}