package cn.bossge.cloud_diary_sso.constant;

public final class AccountMessage {
    public static final String REGISTRATION_SUCCESS = "Thank you for the registration, please check your mailbox for the account active mail.";
    public static final String REGISTRATION_ERROR = "Failed to register, please check your input";
    public static final String REGISTRATION_ERROR_BY_EMAIL = "Failed to send registration email, please try again.";
    public static final String REGISTRATION_PENDING_FOR_ACTIVE_ERROR = "Please check your mailbox for the account active mail.";
    public static final String REGISTRATION_EMAIL_REGISTERED_ERROR = "Please check your mailbox for the account active mail.";
    public static final String REGISTRATION_EMAIL_EXISTS_ERROR = "Your account has been resgisted, please try to sign in.";
    
    public static final String VERIFY_EMAIL_SUCCESS = "Congratulations! The email address has been verified successfully.";
    public static final String VERIFY_EMAIL_ERROR = "Failed to verify the email address.";

    public static final String LOGIN_SUCCESS = "Login successfully.";
    public static final String LOGIN_ERROR = "Failed to login.";

    public static final String PASSWORD_UPDATE_SUCCESS = "Password has been changed successfully.";
    public static final String PASSWORD_UPDATE_ERROR = "Failed to change password.";
    public static final String PASSWORD_IS_WRONG = "The password is not correct.";
    
    public static final String EMAIL_REQUIRED = "Please check your email.";
    public static final String PASSWORD_TEMP_SUCCESS = "Please check your mailbox to move forward.";
    public static final String ACCOUNT_NOT_ACTIVE = "Your account is not active, please active it firstly.";
    
}
