package com.evans.technologies.usuario.model.ResponsesApi;


import com.evans.technologies.usuario.model.user;

public class LoginResponse {

    public Boolean success;
    public String msg;
    public String message;
    public com.evans.technologies.usuario.model.user user;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
