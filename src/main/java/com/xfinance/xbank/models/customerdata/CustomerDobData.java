
import java.io.Serializable;
import java.utils.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table(name="customer_dob_data")
public class CustomerDobData implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int customer_dob_data_id;

	private Integer customer_dob_yyyy;
	private Integer customer_dob_mm;
	private Integer customer_dob_dd;

	private String customer_dob;

	CustomerDobData(
		String customer_dob
	) {
		this.customer_dob = customer_dob;
	}
}