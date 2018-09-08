package com.jiaoshizige.teacherexam.http;
import org.xutils.http.annotation.HttpResponse;

import java.io.Serializable;

@HttpResponse(parser = JsonResponseParser.class)
public class SupportResponse implements Serializable {
    private String result;
    private int error;
    private String message;
    private String status_code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }


}
