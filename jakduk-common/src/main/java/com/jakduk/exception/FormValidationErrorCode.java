package com.jakduk.exception;

/**
 * @author pyohwan
 * 16. 7. 6 오후 11:13
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
    public final static String NEW_PASSWORD_NOT_EMPTY = "newPassword_NotEmpty";
    public final static String NEW_PASSWORD_SIZE = "newPassword_Size";
    public final static String NEW_PASSWORD_CONFIRM_NOT_EMPTY = "newPasswordConfirm_NotEmpty";
    public final static String NEW_PASSWORD_CONFIRM_SIZE = "newPasswordConfirm_Size";
    public final static String PASSWORD_MISMATCH = "password_Mismatch";
    public final static String NEW_PASSWORD_MISMATCH = "newPassword_Mismatch";

    public final static String SUBJECT_SIZE = "subject_Size";
    public final static String SUBJECT_NOT_EMPTY = "subject_NotEmpty";

    public final static String CONTENT_SIZE = "content_Size";
    public final static String CONTENT_NOT_EMPTY = "content_NotEmpty";

    public final static String COMMENT_CONTENTS_SIZE = "contents_Size";

    public final static String SEQ_MIN = "seq_Min";
    public final static String SEQ_NOT_NULL = "seq_NotNull";
}

