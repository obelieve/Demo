package com.github.obelieve.login.entity;

public class SendSMSEntity {

    /**
     * validate_code存在时，触发拼图验证码
     */
    private String validate_code;

    public String getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(String validate_code) {
        this.validate_code = validate_code;
    }
}
