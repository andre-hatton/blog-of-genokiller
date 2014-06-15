package com.genokiller.blogofgenokiller.utils;

import android.widget.Button;

/**
 * Created by yoshizuka on 14/06/14.
 */
public class Item {
    private String text;
    private boolean has_button;
    private int id;
    public Item(String text, boolean has_button)
    {
        this.text = text;
        this.has_button = has_button;
    }
    public Item(String text, int id, boolean has_button)
    {
        this.text = text;
        this.has_button = has_button;
        this.id = id;
    }
    public Item(String text)
    {
        this.text = text;
    }
    public Item(String text, int id)
    {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean has_button() {
        return has_button;
    }

    public void set_has_button(boolean has_button) {
        this.has_button = has_button;
    }
}
