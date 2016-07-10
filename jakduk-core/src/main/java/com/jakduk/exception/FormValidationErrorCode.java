package com.jakduk.exception;

/**
 * Created by pyohwan on 16. 7. 6.
 */
public class FormValidationErrorCode {

    public final static String USERNAME_NOT_EMPTY = "username_NotEmpty";
    public final static String USERNAME_SIZE = "username_Size";
    public final static String USERNAME_EXISTS = "username_Exists";

    public final static String ACCESS_TOKEN_NOT_EMPTY = "accessToken_NotEmpty";

    public final static String EMAIL_NOT_FORMAT = "email_NotFormat";
    public final static String EMAIL_NOT_EMPTY = "email_NotEmpty";
    public final static String EMAIL_SIZE = "email_Size";
    public final static String EMAIL_EXISTS = "email_Exists";

    public final static String PASSWORD_NOT_EMPTY = "password_NotEmpty";
    public final static String PASSWORD_SIZE = "password_Size";
    public final static String PASSWORD_CONFIRM_NOT_EMPTY = "passwordConfirm_NotEmpty";
    public final static String PASSWORD_CONFIRM_SIZE = "passwordConfirm_Size";
    public final static String PASSWORD_MISMATCH = "password_Mismatch";
}

