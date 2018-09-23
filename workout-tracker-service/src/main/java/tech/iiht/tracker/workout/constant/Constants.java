package tech.iiht.tracker.workout.constant;

public final class Constants {

	public static final String APP_SECRET = System.getProperty("app.secret");
	public static final long AUTH_TOKEN_EXPIRE_MILLIS = (24 * 60 * 60 * 1000) - 1;
	public static final String DT_FMT_ISO_LOCAL_DT = "yyyy-MM-dd";
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String PASSWORD_FORMAT_ERROR = "Invalid Password. Password must follow these criteria : "
			+ Constants.LINE_SEPARATOR + "1. must be exactly 8 characters" + Constants.LINE_SEPARATOR
			+ "2. must contain atleast one uppercase letter" + Constants.LINE_SEPARATOR
			+ "3. must contain atleast one lowercase letter" + Constants.LINE_SEPARATOR
			+ "4. must contain atleast one numerical letter" + Constants.LINE_SEPARATOR
			+ "5. must contain one special character [#-@$]" + Constants.LINE_SEPARATOR;
	public static final String REGEX_ALPHA = "[a-zA-Z]+";
	public static final String REGEX_PASSWORD = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$-]).{8}$";
	public static final String SPACE = " ";

	private Constants() {
		super();
	}

}
