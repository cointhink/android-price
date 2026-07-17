package com.cointhink.price;

import android.content.ContentValues;

public interface Sqlable {
    public String getTableName();

    public ContentValues getAttributes();
}
