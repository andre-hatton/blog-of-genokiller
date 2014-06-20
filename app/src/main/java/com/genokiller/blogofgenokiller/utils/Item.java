package com.genokiller.blogofgenokiller.utils;

import android.widget.Button;

/**
 * Created by yoshizuka on 14/06/14.
 */
public class Item {
    private String text;
    private Button button_more, button_less;
    private int id;
    public Item(String text, Button button_more, Button button_less)
    {
        this.text = text;
        this.button_less = button_less;
        this.button_more = button_more;
    }
    public Item(String text, int id, Button button_more, Button button_less)
    {
        this.text = text;
        this.button_less = button_less;
        this.button_more = button_more;
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

    public Button getButton_more() {
        return button_more;
    }

    public void setButton_more(Button button_more) {
        this.button_more = button_more;
    }

    public Button getButton_less() {
        return button_less;
    }

    public void setButton_less(Button button_less) {
        this.button_less = button_less;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
