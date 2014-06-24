package com.genokiller.blogofgenokiller.utils;

import com.genokiller.blogofgenokiller.models.Application_Model;

/**
 * Created by yoshizuka on 21/06/14.
 * Permet de géré l'URL du site et les retours de l'api (réponse, status, ...)
 */
public class Url {
    private int status;
    private String result;
    //public static final String BASE_URL = "http://192.168.1.3:3000/";
    public static final String BASE_URL = "http://blog-of-genokiller.herokuapp.com/";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
