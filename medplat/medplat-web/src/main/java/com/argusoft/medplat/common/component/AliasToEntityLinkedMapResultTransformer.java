/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.component;

import org.hibernate.transform.BasicTransformerAdapter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *<p>Utility class to transform alias to entity</p>
 * @author vaishali
 * @since 25/08/20 4:30 PM
 */
public class AliasToEntityLinkedMapResultTransformer extends BasicTransformerAdapter implements Serializable {
    public static final AliasToEntityLinkedMapResultTransformer INSTANCE = new AliasToEntityLinkedMapResultTransformer();

    private AliasToEntityLinkedMapResultTransformer() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String,Object> result = new LinkedHashMap<>(tuple.length);
        for (int i = 0; i < tuple.length; i++) {
            String alias = aliases[i];
            if (alias != null) {
                result.put(alias, tuple[i]);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof AliasToEntityLinkedMapResultTransformer;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}