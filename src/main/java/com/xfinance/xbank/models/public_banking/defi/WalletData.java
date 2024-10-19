
package com.xfinance.xbank.models.public_banking.defi.wallet;

import lombok.Getter;
import lombok.Setter;

public class WalletData {

	@Getter
	public Long id;

	@Getter @Setter
	public List<WalletTransactionData> wallet_transactions;

	@Getter @Setter
	public byte[] blob;
}
