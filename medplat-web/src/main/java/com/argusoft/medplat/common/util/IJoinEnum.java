package com.argusoft.medplat.common.util;


import javax.persistence.criteria.JoinType;

/**
 * Created this enum so that we can use generics for the enum that we have created in entities.
 * @author Kuldeep
 * @since 31/08/2020 4:30
 */
public interface IJoinEnum {
    /**
     * Returns string value
     * @return A string value
     */
    String getValue();

    /**
     * Returns an alias value
     * @return A value of alias
     */
    String getAlias();

    /**
     * Returns type of join
     * @return An instance of JoinType
     */
    JoinType getJoinType();

}
