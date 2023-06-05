package one.terenin.exception.common;

public enum ErrorCode {
    FILE_NOT_UPLOADED("FILE NOT UPLOADED"),
    FILE_MAP_ERROR("FILE NOT MAPPED TO RESPONSE"),
    DOWNLOAD_FAILED("DOWNLOAD FAILED");

    private final String info;

    ErrorCode(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
