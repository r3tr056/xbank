
public class Amount {
	private String tag;
	private Double amount;

	public Validators[] amount_validators;
	public AmountConverters[] amount_converters;

	public Amount(String tag, Double amount, Validators[] validators, AmountConverters[] converters) {

		this.tag = tag;
		this.amount = amount;
		this.amount_validators = validators;
		this.amount_converters = converters;
	}

	public Double getAmount() { return amount; }
	public void setAmount(Double amount) { this.amount = amount; }

	public String getTag() { return tag; }
	public void setTag(String tag) { this.tag = tag; }

	public Validators[] getValidators() { return amount_validators; }
	public void setValidators(Validators[] validators) { this.amount_validators = validators; }

	public AmountConverters[] getConverters() { return amount_converters; }
	public void setConverters(AmountConverters[] converters) { this.amount_converters = converters; }
}