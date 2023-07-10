package com.argusoft.medplat.web.healthinfra.constants;

/**
 * <p>
 * Define constants for heath infra structure.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public class HealthInfrastructureConstants {

    private HealthInfrastructureConstants() {
        throw new IllegalStateException("Utility Class");
    }

    // Health Infrastructure Type IDs
    public static final Integer INFRA_DISTRICT_HOSPITAL = 1007;
    public static final Integer INFRA_SUB_DISTRICT_HOSPITAL = 1008;
    public static final Integer INFRA_COMMUNITY_HEALTH_CENTER = 1009;
    public static final Integer INFRA_TRUST_HOSPITAL = 1010;
    public static final Integer INFRA_MEDICAL_COLLEGE_HOSPITAL = 1012;
    public static final Integer INFRA_PRIVATE_HOSPITAL = 1013;
    public static final Integer INFRA_PHC = 1061;
    public static final Integer INFRA_SC = 1062;
    public static final Integer INFRA_UPHC = 1063;
    public static final Integer INFRA_GRANT_IN_AID = 1064;
    public static final Integer INFRA_URBAN_COMMUNITY_HEALTH_CENTER = 1084;
}
