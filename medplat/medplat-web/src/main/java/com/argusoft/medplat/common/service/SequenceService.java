
package com.argusoft.medplat.common.service;

/**
 * <p>
 *     Define methods for sequence
 * </p>
 * @author kunjan
 * @since 27/08/2020 4:30
 */
public interface SequenceService {
    /**
     * Returns a next value of given sequence name
     * @param sequenceName A name of sequence
     * @return A next vale of sequence
     */
    Integer getNextValueBySequenceName(String sequenceName);

}
