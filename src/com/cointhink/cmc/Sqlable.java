package com.cointhink.cmc;

import android.content.ContentValues;

public interface Sqlable {
    public String getTableName();
    public ContentValues getAttributes();
}
