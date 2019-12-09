package webplay.ResponseClasses;

public class AuthSignIn {
    private String access_token;
    private String token_type;
    private String secret;
    private String algorithm;
    private float expires_in;
    private String captcha_result;
    private String message;


    // Getter Methods

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public float getExpires_in() {
        return expires_in;
    }

    public String getCaptcha_result() {
        return captcha_result;
    }

    public String getMessage() {
        return message;
    }

    // Setter Methods

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setExpires_in(float expires_in) {
        this.expires_in = expires_in;
    }

    public void setCaptcha_result(String captcha_result) {
        this.captcha_result = captcha_result;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
