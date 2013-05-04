package morning.v1.POS4US.Configuration;

public class ErrorMsg {
	public static final String FILE_CREATED = "FILE_CREATED";
	public static final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
	public static final String IO_EXCEPTION = "IO_EXCEPTION";
	
	public static final String UNSUPPORTED_ENCODING_EXCEPTION = "UNSUPPORTED_ENCODING_EXCEPTION";
	public static final String CLIENT_PROTOCOL_EXCEPTION = "CLIENT_PROTOCOL_EXCEPTION";
	
	public static final int LOGIN_SUCCESS = 1;
	public static final int AUTHENTICATION_FAILURE = 0;
	public static final int SERVER_OPERATION_CEASED = -1;
	public static final int DEAD_SERVER_IP_OR_STATUS = -2;
}
