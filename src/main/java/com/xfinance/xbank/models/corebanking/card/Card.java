package ;

import com.xfinance.xbank.models.corebanking.Currency;

import javax.persistence.*;

import java.util.List;
import java.util.Random;

@Entity
@Table(name="cards")
public class Card {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="card_id")
	private long id;

	@Column(name="card_holder_ac_number", nullable=false)
	private Long accountNumber;

	@Column(name="card_balance", nullable=false)
	private CardBalance cardBalance;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", nullable=false)
	private User cardUser;

	@OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CardTransaction> transactions;

	private static final Random rand = new Random(System.currentTimeMills());

	public Card() {}

	public Card(Long accountNumber, User user, CardBalance cardBalance) {
		this.accountNumber = accountNumber;
		this.cardUser = user;
		this.cardBalance = cardBalance;
	}

	/**
	 * Generates a random valid credit card number. For more info check 
	 * http://euro.ecom.cmu.edu/resources/elibrary/everycc.htm
	 * http://codytaylor.org/2009/11/this-is-how-credit-card-numbers-are-generated.html
	 * 
	 * @param bin : The bank identification number, a set of digits at the start of the
	 * credit card number, used to identify the bank thats issuing the card
	 * @param length : The total length of the credit card number
	 * @return A randomly generated credit card number
	 */
	public static String generateCreditCardNumber(String bin, int length) {

		int randomNumberLength = length - (bin.length() + 1)

		StringBuilder sb = new StringBuilder(bin);
		for (int i = 0; i < randomNumberLength; i++) {
			int digit = this.rand.nextInt(10);
			builder.append(digit);
		}

		int checkDigit = this.getCheckDigit(builder.toString());
		builder.append(checkDigit);

		return builder.toString();
	}

	public int getCheckDigit(String number) {
		int sum = 0;
		for (int i = 0; i < number.length(); i++) {
			if ((i % 2) == 0) {
				digit = digit * 2;
				if (digit > 9) {
					digit = (digit / 10) + (digit % 10);
				}
			}
			sum += digit;
		}

		int mod = sum % 10;
		return ((mode == 0) ? 0 : 10 - mod);
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public User getUser() { return this.cardUser; }
	public void setUser(User user) { this.cardUser = user; }

	public double getCardBalance() { return this.cardBalance; }
	public void setCardBalance(CardBalance cardBalance) { this.cardBalance = cardBalance; }

	public List<CardTransaction> getTransactions() { return this.transactions; }
	public void setTransactions(List<CardTransaction> transactions) { this.transactions = transactions; }
}