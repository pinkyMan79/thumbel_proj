package one.terenin.exception.common;

public enum ErrorCode {

    // user codes
    DISABLE("ACCOUNT DISABLED"),
    UNMATCHED("PASSWORD UNMATCHED"),
    INVALID_USERNAME("USERNAME IS INVALID"),
    // jwt codes
    UNAUTHORIZED("JWT TOKEN CAN NOT BE APPROVED. USER UNAUTHORIZED"),
    JWT_EXPIRED("JWT TOKEN IS EXPIRED");

    private final String info;

    ErrorCode(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
