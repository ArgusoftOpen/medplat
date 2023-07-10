package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * @author kelvin
 */

public abstract class BaseEntity {

    @DatabaseField(id = true, columnName = "_ID")
    protected Integer id;

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!(o instanceof BaseEntity))
            return false;

        BaseEntity baseEntity = (BaseEntity) o;
        return baseEntity.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
