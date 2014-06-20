package com.genokiller.blogofgenokiller.utils;

import android.widget.Button;

/**
 * Created by yoshizuka on 14/06/14.
 */
public class Item {
    private String text;
    private int button;
    private int id;
    private String type;
    public final static int BUTTON_MORE_LESS = 1;
    public final static int BUTTON_MORE_LESS_EDIT = 2;
    public final static int BUTTON_EDIT = 3;
    public final static int NO_BUTTON = -1;
    public Item(String text, int id)
    {
        this.text = text;
        this.id = id;
        this.button = NO_BUTTON;
    }
    public Item(String text, int id, String type)
    {
        this.text = text;
        this.id = id;
        this.button = NO_BUTTON;
        this.type = type;
    }
    public Item(String text, int id, int button)
    {
        this.text = text;
        this.button = button;
        this.id = id;
    }
    public Item(String text, int id, int button, String type)
    {
        this.text = text;
        this.button = button;
        this.id = id;
        this.type = type;
    }
    public Item(String text)
    {
        this.text = text;
        this.button = NO_BUTTON;
    }
    public Item(String text, String type)
    {
        this.text = text;
        this.button = NO_BUTTON;
        this.type = type;
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

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }
    public boolean has_button(){
        return button > -1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
