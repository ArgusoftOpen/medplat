package com.argusoft.sewa.android.app.core;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface ImmunisationService {

    Set<String> getDueImmunisationsForChild(Date dateOfBirth, String givenImmunisations);

    String vaccinationValidationForChild(Date dob, Date givenDate, String currentVaccine, Map<String, Date> vaccineGivenDateMap);

    boolean isImmunisationMissed(Date dateOfBirth, String immunisation);

}
