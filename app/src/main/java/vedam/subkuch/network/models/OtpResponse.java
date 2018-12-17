package vedam.subkuch.network.models;

public class OtpResponse {
    private String Status;

    private String CountryCode;

    private String MobileNumber;

    private String GeneratedTime;

    private String OTP;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String CountryCode) {
        this.CountryCode = CountryCode;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public String getGeneratedTime() {
        return GeneratedTime;
    }

    public void setGeneratedTime(String GeneratedTime) {
        this.GeneratedTime = GeneratedTime;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    @Override
    public String toString() {
        return "OtpResponse [Status = " + Status + ", CountryCode = " + CountryCode + ", MobileNumber = " + MobileNumber + ", GeneratedTime = " + GeneratedTime + ", OTP = " + OTP + "]";
    }
}
