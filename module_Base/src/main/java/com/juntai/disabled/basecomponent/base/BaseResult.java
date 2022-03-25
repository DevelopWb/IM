package com.juntai.disabled.basecomponent.base;

import java.util.ArrayList;
import java.util.List;

public class BaseResult  {


    public int code;
    public String msg;
    public String message;
    public List<String> filePaths;
    /**
     * errorData : {"errorMsg":"昵称必须是2-10位字符"}
     */

    private ErrorDataBean errorData;

    public List<String> getFilePaths() {
        if (filePaths == null) {
            return new ArrayList<>();
        }
        return filePaths;
    }


    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message == null ? "" : message;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public ErrorDataBean getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorDataBean errorData) {
        this.errorData = errorData;
    }

    public static class ErrorDataBean {
        /**
         * errorMsg : 昵称必须是2-10位字符
         */

        private String errorMsg;

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }
}
