package com.genokiller.blogofgenokiller.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by yoshizuka on 21/06/14.
 * Permet de géré les champs d'édition des info de façon plus large
 */
public class EditTextInfo extends EditText {
    /**
     * Definit si un champs est obligatoire
     */
    private boolean mandatory;
    /**
     * Definit le name de l'input correspondant
     */
    private String name;
    /**
     * Defnit le name de l'info_id (spécifique aux infos)
     */
    private String name_info_id;
    /**
     * Identifie l'id de l'info
     */
    private int info_id;
    /**
     * Identifie le type de l'info
     */
    private String info_type;

    public EditTextInfo(Context context) {
        super(context);
    }

    public EditTextInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextInfo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_info_id() {
        return name_info_id;
    }

    public void setName_info_id(String name_info_id) {
        this.name_info_id = name_info_id;
    }

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public String getInfo_type() {
        return info_type;
    }

    public boolean has_type_info()
    {
        return info_type != null;
    }

    public void setInfo_type(String info_type) {
        this.info_type = info_type;
    }
}
