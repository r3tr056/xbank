public class Hash {
	private static final Logger logger = LoggerFactory.getLogger(Hash.class);

	/**
	 * Compute the hash digest of the given data using SHA-256 algo
	 * 
	 * @param originalBytes Original bytes of the data
	 * @return Hashed bytes
	 * @throws NoSuchAlgorithmException In case a provided algo does not exist
	 */
	private static byte[] hash(byte[] originalBytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(originalBytes);
	}

	/**
	 * Compute the SHA256 of the provided bytes
	 * 
	 * @param originalBytes : Original Bytes
	 * @return SHA256 hash of the provided original bytes
	 */
	public static byte[] sha256(byte[] originalBytes) {
		try {
			return hash(originalBytes);
		} catch (NoSuchAlogrithmException e) {
			logger.warn(ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * Compute SHA256 hash of the string, that was transfetted to data using the provided
	 * charset
	 * 
	 * @param string String to be hashed
	 * @param charset Charset to be used to convert string -> bytes
	 * @return SHA256 hash of the string
	 */
	public static byte[] sha256(String string, Charset charset) {
		byte[] originalBytes = string.getBytes(charset);
		return sha256(originalBytes);
	}

	/**
	 * Compute the SHA256 hash of the string
	 * 
	 * @param string String to be hashed
	 * @return SHA256 hash of the string
	 */
	public static byte[] sha256(String string) {
		return sha256(string, StandardCharsets.UTF_8);
	}
}