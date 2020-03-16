package com.evans.technologies.usuario.model.ResponsesApi;

public class RegisterResponse {

    boolean success;
    String msg;
    String user;

    public RegisterResponse(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
