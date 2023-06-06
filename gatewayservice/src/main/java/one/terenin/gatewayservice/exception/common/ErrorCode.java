package one.terenin.gatewayservice.exception.common;

public enum ErrorCode {

    NOT_A_USER_PICTURE("THAT IS NOT A USER PHOTO. REJECTED"),
    PICTURE_ALREADY_IN_USE("THAT PICTURE ALREADY IN USE"),
    FAILED("THE PROBLEM WAS IN UPDATE USER INFO"),
    NO_SUCH_FILE("NO SUCH FILE"),
    FILE_ALREADY_ADDED("FILE ALREADY ADDED"),
    USERNAME_IN_USE("USERNAME ALREADY IN USE"),
    PASSWORD_ALREADY_IN_USE("PASSWORD ALREADY IN USE");

    private final String info;

    ErrorCode(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
