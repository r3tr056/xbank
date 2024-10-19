public class AESEncryptionUtils {
	private static final Logger logger = LoggerFactory.getLogger(AESEncryptionUtils.class);

	public byte[] encrypt(byte[] bytes, byte[] iv, SecretKey secret, String transformation) throws InvalidKeyException, GenericCryptoException, CryptoProviderException {

		try {
			Cipher cipherForCrypto = Cipher.getInstance(transformation, AuthConfig.CRYPTO_PROVIDER_NAME);
			cipherForCrypto.init(Cipher.ENCRYPT_MODE, secret, new IvParamaterSpec(iv));
			return cipherForCrypto.doFinal(bytes);

		} catch (NoSuchAlogrithmException | NoSuchProviderException | InvalidAlgorithmException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new CryptoProviderException(ex.getMessage(), ex);

		} catch (IllegalBlockSizeException | BadPaddingException | NotSuchPaddingException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new CryptoProviderException(ex.getMessage(), ex);
		}
	}

	public byte[] encrypt(byte[] bytes, byte[] iv, SecretKey secret) throws InvalidKeyException, GenericCryptoException, CryptoProviderException {
		return this.encrypt(bytes, iv, secret, "AES/CBC/PKCS7Padding");
	}

	public byte[] decrypt(byte[] bytes, byte[] iv, SecretKey secret, String transformation) throws InvalidKeyException, GenericCryptoException, CryptoProviderException {
		try {
			Cipher cipherForCrypto = Cipher.getInstance(transformation, AuthConfig.CRYPTO_PROVIDER_NAME)
			cipherForCrypto.init(Cipher.DECRYPT_MODE, secret, new IvParamaterSpec(iv));
			return cipherForCrypto.doFinal(bytes);

		} catch (NoSuchAlogrithmException | NoSuchProviderException | InvalidAlgorithmParamaterException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new CryptoProviderException(ex.getMessage(), ex);

		} catch (IllegalBlockSizeException | BadPaddingException | NotSuchPaddingException ex) {
			logger.warn(ex.getMessage(), ex);
			throw new GenericCryptoException(ex.getMessage(), ex);
		}
	}

	public byte[] decrypt(byte[] bytes, byte[] iv, SecretKey secret) throws InvalidKeyException, GenericCryptoException, CryptoProviderException {
		return this.decrypt(bytes, iv, secret, "AES/CBC/PKCS7Padding");
	}
}