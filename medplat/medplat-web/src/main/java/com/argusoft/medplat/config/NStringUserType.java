/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author admin
 */
public class NStringUserType implements UserType  {

    @Override
    public Object assemble(Serializable arg0, Object owner)
             {

        return deepCopy(arg0);
    }

    @Override
    public Object deepCopy(Object arg0)  {
        if(arg0==null) return null;
        return arg0.toString();
    }

    @Override
    public Serializable disassemble(Object arg0) {
        return (Serializable)deepCopy(arg0);
    }

    @Override
    public boolean equals(Object arg0, Object arg1)  {
        if(arg0 == null )
            return arg1 == null;
        return arg0.equals(arg1);
    }

    @Override
    public int hashCode(Object arg0) {
        return arg0.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object arg0, Object target, Object owner)
             {
        return deepCopy(arg0);
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.NVARCHAR};
    }


    @Override
    public Object nullSafeGet(ResultSet rs, String[] strings, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String result = rs.getString(strings[0]);
        return result == null || result.trim().length() == 0
                ? null : result;
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object o, int i, SharedSessionContractImplementor si) throws SQLException {
        if(o == null)
            ps.setNull(i,Types.NVARCHAR);
        else
            ps.setNString(i, o.toString());
    }
}
