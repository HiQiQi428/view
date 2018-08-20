package org.luncert.view.util;

public interface StatusCode {

    public static final int OK = 0;

    // pig not found
    public static final int PIG_NOT_FOUND = 1;

    // invalid sid
    public static final int INVALID_SID = 2;

    // invalid strain identifier
    public static final int INVALID_STRAIN_IDENTIFIER = 3;

    // field is null
    public static final int FIELD_IS_NULL = 4;

    // exception occured
    public static final int EXCEPTION_OCCUR = 5;

    // found nothing
    public static final int FOUND_NOTHING = 6;

    public static final int PICTURE_NOT_FOUND = 7;

}