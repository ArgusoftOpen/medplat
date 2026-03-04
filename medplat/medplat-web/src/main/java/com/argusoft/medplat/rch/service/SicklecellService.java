package com.argusoft.medplat.rch.service;

import com.argusoft.medplat.rch.dto.SicklecellScreeningDto;

/**
 * <p>
 * Define services for rim.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
public interface SicklecellService {

    /**
     * Add sickle cell screening details.
     *
     * @param sicklecellScreeningDto Sickle cell screening details.
     */
    void create(SicklecellScreeningDto sicklecellScreeningDto);
}
