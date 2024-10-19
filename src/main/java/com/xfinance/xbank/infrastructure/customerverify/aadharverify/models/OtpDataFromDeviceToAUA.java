public class OtpDataFromDeviceToAUA {

	public static final String SMS_CHANNEL = "01";
	public static final String EMAIL_CHANNEL = "02";
	public static final String BOTH_EMAIL_SMS_CHANNEL = "00";

	private String uid;
	private String terminalId;
	private String channel;

	public OtpDataFromDeviceToAUA(String uid, String terminalId, String channel) {
		this.uid = uid;
		this.terminalId = terminalId;
		this.channel = channel;
	}

	public String getChannel() { return this.channel; }
	public String getTerminalId() { return this.terminalId; }
	public String getUid() { return this.uid; }

}