package org.kudrenko.telegram.api;

import org.kudrenko.telegram.R;

public enum Errors {
    PHONE_NUMBER_INVALID("PHONE_NUMBER_INVALID", R.string.error_phone_number_invalid),
    PHONE_CODE_EMPTY("PHONE_CODE_EMPTY", R.string.error_phone_code_empty),
    PHONE_CODE_INVALID("PHONE_CODE_INVALID", R.string.error_phone_code_invalid);

    public String code;
    public int resource;

    Errors(String code, int resource) {
        this.code = code;
        this.resource = resource;
    }

    public static int find(String code) {
        for (Errors errors : values()) {
            if (errors.code.equalsIgnoreCase(code)) {
                return errors.resource;
            }
        }
        return R.string.error_unknown;
    }
}
