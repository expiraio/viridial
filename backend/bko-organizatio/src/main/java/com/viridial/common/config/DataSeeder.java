package com.viridial.common.config;

import com.viridial.common.repositories.TimezoneRepository;
import com.viridial.countries.entities.CityEntity;
import com.viridial.countries.entities.CountryEntity;
import com.viridial.countries.entities.TimezoneEntity;
import com.viridial.countries.repositories.CityRepository;
import com.viridial.countries.repositories.CountryRepository;
import com.viridial.organization.entities.TeamAddressEntity;
import com.viridial.organization.entities.TeamEntity;
import com.viridial.organization.entities.TeamPhoneEntity;
import com.viridial.organization.repositories.TeamAddressRepository;
import com.viridial.organization.repositories.TeamPhoneRepository;
import com.viridial.organization.repositories.TeamRepository;
import com.viridial.referentiel.entities.ReferentialEntity;
import com.viridial.referentiel.repositories.ReferentialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data seeder component to initialize database with seed data.
 * Runs on application startup to populate initial data.
 */
@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private TimezoneRepository timezoneRepository;

    @Autowired
    private ReferentialRepository referentialRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamAddressRepository teamAddressRepository;

    @Autowired
    private TeamPhoneRepository teamPhoneRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Starting data seeding...");

        try {
            // Delete all data first (in reverse dependency order)
            deleteAllData();
            
            // Seed data
            seedTimezones();
            seedReferentials();
            seedCountries();
            seedCities();
            seedOrganizations();

            logger.info("Data seeding completed successfully!");
        } catch (Exception e) {
            logger.error("Error during data seeding", e);
            throw new RuntimeException("Failed to seed data", e);
        }
    }

    /**
     * Deletes all data in reverse dependency order to avoid FK violations.
     */
    private void deleteAllData() {
        logger.info("Deleting all existing data...");
        
        try {
            // Delete child entities first
            teamPhoneRepository.deleteAll();
            teamAddressRepository.deleteAll();
            teamRepository.deleteAll();
            cityRepository.deleteAll();
            countryRepository.deleteAll();
            
            // Handle referentials with self-referencing FK constraint
            // Temporarily disable the FK constraint to allow deletion
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential DROP CONSTRAINT IF EXISTS fk_referential_type"
            ).executeUpdate();
            entityManager.flush();
            
            // Also disable other self-referencing constraints
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential DROP CONSTRAINT IF EXISTS fk_referential_sub_type"
            ).executeUpdate();
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential DROP CONSTRAINT IF EXISTS fk_referential_parent"
            ).executeUpdate();
            entityManager.flush();
            
            // Now delete all referentials
            referentialRepository.deleteAll();
            entityManager.flush();
            
            // Re-create the constraints
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential ADD CONSTRAINT fk_referential_type " +
                "FOREIGN KEY (type_id) REFERENCES com_referential(id)"
            ).executeUpdate();
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential ADD CONSTRAINT fk_referential_sub_type " +
                "FOREIGN KEY (sub_type_id) REFERENCES com_referential(id)"
            ).executeUpdate();
            entityManager.createNativeQuery(
                "ALTER TABLE com_referential ADD CONSTRAINT fk_referential_parent " +
                "FOREIGN KEY (parent_id) REFERENCES com_referential(id)"
            ).executeUpdate();
            entityManager.flush();
            
            // Delete timezones last
            timezoneRepository.deleteAll();
            
            entityManager.flush();
            entityManager.clear();
            
            logger.info("All existing data deleted");
        } catch (Exception e) {
            logger.error("Error deleting data", e);
            throw new RuntimeException("Failed to delete all data", e);
        }
    }

    /**
     * Seeds timezone data with real-world timezones.
     */
    private void seedTimezones() {
        logger.info("Seeding timezones...");
        List<TimezoneEntity> timezones = new ArrayList<>();

        // Major timezones
        timezones.add(createTimezone("UTC", "Coordinated Universal Time", "UTC", "+00:00", "+00:00", false, "The primary time standard"));
        timezones.add(createTimezone("Europe/Paris", "Central European Time (Paris)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Europe/London", "Greenwich Mean Time", "GMT", "+00:00", "+01:00", true, "Greenwich Mean Time / British Summer Time"));
        timezones.add(createTimezone("America/New_York", "Eastern Standard Time (New York)", "EST", "-05:00", "-04:00", true, "Eastern Time (US & Canada)"));
        timezones.add(createTimezone("America/Chicago", "Central Standard Time (Chicago)", "CST", "-06:00", "-05:00", true, "Central Time (US & Canada)"));
        timezones.add(createTimezone("America/Denver", "Mountain Standard Time", "MST", "-07:00", "-06:00", true, "Mountain Time (US & Canada)"));
        timezones.add(createTimezone("America/Los_Angeles", "Pacific Standard Time", "PST", "-08:00", "-07:00", true, "Pacific Time (US & Canada)"));
        timezones.add(createTimezone("Asia/Tokyo", "Japan Standard Time", "JST", "+09:00", "+09:00", false, "Japan Standard Time"));
        timezones.add(createTimezone("Asia/Shanghai", "China Standard Time", "CST", "+08:00", "+08:00", false, "China Standard Time"));
        timezones.add(createTimezone("Asia/Dubai", "Gulf Standard Time", "GST", "+04:00", "+04:00", false, "Gulf Standard Time"));
        timezones.add(createTimezone("Asia/Kolkata", "India Standard Time", "IST", "+05:30", "+05:30", false, "India Standard Time"));
        timezones.add(createTimezone("Australia/Sydney", "Australian Eastern Time (Sydney)", "AET", "+10:00", "+11:00", true, "Australian Eastern Standard Time"));
        timezones.add(createTimezone("Europe/Moscow", "Moscow Standard Time", "MSK", "+03:00", "+03:00", false, "Moscow Time"));
        timezones.add(createTimezone("Africa/Cairo", "Eastern European Time", "EET", "+02:00", "+02:00", false, "Eastern European Time"));
        timezones.add(createTimezone("America/Sao_Paulo", "Brasilia Time", "BRT", "-03:00", "-03:00", false, "Brasilia Time"));
        timezones.add(createTimezone("America/Mexico_City", "Central Standard Time (Mexico)", "CST", "-06:00", "-05:00", true, "Central Time (Mexico)"));
        timezones.add(createTimezone("America/Buenos_Aires", "Argentina Time", "ART", "-03:00", "-03:00", false, "Argentina Time"));
        timezones.add(createTimezone("Europe/Berlin", "Central European Time (Berlin)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Europe/Rome", "Central European Time (Rome)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Europe/Madrid", "Central European Time (Madrid)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Europe/Amsterdam", "Central European Time (Amsterdam)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Europe/Stockholm", "Central European Time (Stockholm)", "CET", "+01:00", "+02:00", true, "Central European Time"));
        timezones.add(createTimezone("Asia/Singapore", "Singapore Time", "SGT", "+08:00", "+08:00", false, "Singapore Standard Time"));
        timezones.add(createTimezone("Asia/Bangkok", "Indochina Time", "ICT", "+07:00", "+07:00", false, "Indochina Time"));
        timezones.add(createTimezone("Asia/Hong_Kong", "Hong Kong Time", "HKT", "+08:00", "+08:00", false, "Hong Kong Time"));
        timezones.add(createTimezone("Asia/Seoul", "Korea Standard Time", "KST", "+09:00", "+09:00", false, "Korea Standard Time"));
        timezones.add(createTimezone("Australia/Melbourne", "Australian Eastern Time (Melbourne)", "AET", "+10:00", "+11:00", true, "Australian Eastern Standard Time"));
        timezones.add(createTimezone("Pacific/Auckland", "New Zealand Time", "NZST", "+12:00", "+13:00", true, "New Zealand Standard Time"));
        timezones.add(createTimezone("Africa/Johannesburg", "South Africa Standard Time", "SAST", "+02:00", "+02:00", false, "South Africa Standard Time"));
        timezones.add(createTimezone("America/Toronto", "Eastern Standard Time (Toronto)", "EST", "-05:00", "-04:00", true, "Eastern Time (Canada)"));

        timezoneRepository.saveAll(timezones);
        logger.info("Seeded {} timezones", timezones.size());
    }

    private TimezoneEntity createTimezone(String code, String name, String abbreviation, 
                                          String utcOffset, String dstOffset, boolean usesDst, String description) {
        TimezoneEntity tz = new TimezoneEntity();
        tz.setCode(code);
        tz.setName(name);
        tz.setAbbreviation(abbreviation);
        tz.setUtcOffset(utcOffset);
        tz.setDstOffset(dstOffset);
        tz.setUsesDst(usesDst);
        tz.setDescription(description);
        tz.setActive(true);
        tz.setCreatedBy("system");
        tz.setUpdatedBy("system");
        tz.setVersion(1);
        tz.setCreatedAt(LocalDateTime.now());
        tz.setUpdatedAt(LocalDateTime.now());       
        return tz;
    }

    /**
     * Seeds referential data (languages, regions, etc.).
     */
    private void seedReferentials() {
        logger.info("Seeding referentials...");
        List<ReferentialEntity> allReferentials = new ArrayList<>();

        // First, check if root type already exists
        Optional<ReferentialEntity> existingRootType = referentialRepository.findByCodeAndDataType(
            "TYPE_REFERENTIAL_TYPE", "REFERENTIAL_TYPE");
        
        ReferentialEntity rootType;
        if (existingRootType.isPresent()) {
            rootType = existingRootType.get();
            logger.debug("Root referential type already exists, using existing one");
        } else {
            // Create a root type referential that will be used as the type for all other type referentials
            // Since typeId is required and we need a self-reference, we'll:
            // 1. Temporarily disable the FK constraint
            // 2. Insert the root type with a placeholder typeId
            // 3. Get the generated ID
            // 4. Update it to reference itself
            // 5. Re-enable the FK constraint
            
            Long rootTypeId;
            try {
                // Temporarily disable FK constraint
                entityManager.createNativeQuery(
                    "ALTER TABLE com_referential DROP CONSTRAINT IF EXISTS fk_referential_type"
                ).executeUpdate();
                entityManager.flush();
                
                // Insert the root type with placeholder typeId
                rootTypeId = ((Number) entityManager.createNativeQuery(
                    "INSERT INTO com_referential (code, data_type, label, description, external_code, " +
                    "active, locale, display_order, type_id, created_at, updated_at, created_by, updated_by, version) " +
                    "VALUES ('TYPE_REFERENTIAL_TYPE', 'REFERENTIAL_TYPE', 'Referential Type', " +
                    "'Root type for all referential types', 'ROOT_TYPE', true, 'en', 0, 0, " +
                    "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', 0) " +
                    "RETURNING id"
                ).getSingleResult()).longValue();
                
                // Update it to reference itself
                entityManager.createNativeQuery(
                    "UPDATE com_referential SET type_id = :id WHERE id = :id"
                ).setParameter("id", rootTypeId).executeUpdate();
                
                entityManager.flush();
                
                // Re-create the FK constraint
                entityManager.createNativeQuery(
                    "ALTER TABLE com_referential ADD CONSTRAINT fk_referential_type " +
                    "FOREIGN KEY (type_id) REFERENCES com_referential(id)"
                ).executeUpdate();
                
                entityManager.flush();
                
                // Load the root type entity
                rootType = referentialRepository.findById(rootTypeId)
                    .orElseThrow(() -> new RuntimeException("Failed to create root referential type"));
            } catch (Exception e) {
                // If something goes wrong, try to restore the constraint
                try {
                    entityManager.createNativeQuery(
                        "ALTER TABLE com_referential ADD CONSTRAINT IF NOT EXISTS fk_referential_type " +
                        "FOREIGN KEY (type_id) REFERENCES com_referential(id)"
                    ).executeUpdate();
                } catch (Exception restoreException) {
                    logger.warn("Could not restore FK constraint", restoreException);
                }
                throw new RuntimeException("Failed to create root referential type", e);
            }
        }
        
        allReferentials.add(rootType);
        
        // Get the root type ID for use in creating other type referentials
        Long rootTypeId = rootType.getId();
        
        // Now create type referentials that reference the root type
        ReferentialEntity typeLanguage = createReferential("TYPE_LANGUAGE", "REFERENTIAL_TYPE", "Language Type", 
            "Type for language referentials", null, null, rootTypeId, null, true, "en", 1);
        ReferentialEntity typeRegion = createReferential("TYPE_REGION", "REFERENTIAL_TYPE", "Region Type", 
            "Type for region referentials", null, null, rootTypeId, null, true, "en", 2);
        ReferentialEntity typeSubRegion = createReferential("TYPE_SUB_REGION", "REFERENTIAL_TYPE", "Sub-Region Type", 
            "Type for sub-region referentials", null, null, rootTypeId, null, true, "en", 3);
        ReferentialEntity typeTeamType = createReferential("TYPE_TEAM_TYPE", "REFERENTIAL_TYPE", "Team Type", 
            "Type for team/organization types", null, null, rootTypeId, null, true, "en", 4);
        ReferentialEntity typeIndustry = createReferential("TYPE_INDUSTRY", "REFERENTIAL_TYPE", "Industry Type", 
            "Type for industry referentials", null, null, rootTypeId, null, true, "en", 5);
        ReferentialEntity typeAddressType = createReferential("TYPE_ADDRESS_TYPE", "REFERENTIAL_TYPE", "Address Type", 
            "Type for address types", null, null, rootTypeId, null, true, "en", 6);
        ReferentialEntity typePhoneType = createReferential("TYPE_PHONE_TYPE", "REFERENTIAL_TYPE", "Phone Type", 
            "Type for phone types", null, null, rootTypeId, null, true, "en", 7);

        List<ReferentialEntity> types = new ArrayList<>();
        types.add(typeLanguage);
        types.add(typeRegion);
        types.add(typeSubRegion);
        types.add(typeTeamType);
        types.add(typeIndustry);
        types.add(typeAddressType);
        types.add(typePhoneType);

        // Save types first to get IDs
        List<ReferentialEntity> savedTypes = referentialRepository.saveAll(types);
        allReferentials.addAll(savedTypes);
        Long languageTypeId = savedTypes.get(0).getId();
        Long regionTypeId = savedTypes.get(1).getId();
        Long subRegionTypeId = savedTypes.get(2).getId();
        Long teamTypeTypeId = savedTypes.get(3).getId();
        Long industryTypeId = savedTypes.get(4).getId();
        Long addressTypeTypeId = savedTypes.get(5).getId();
        Long phoneTypeTypeId = savedTypes.get(6).getId();

        // Languages
        List<ReferentialEntity> languages = new ArrayList<>();
        languages.add(createReferential("LANG_EN", "LANGUAGE", "English", 
            "English language", "en", null, languageTypeId, null, true, "en", 1));
        languages.add(createReferential("LANG_FR", "LANGUAGE", "French", 
            "French language", "fr", null, languageTypeId, null, true, "en", 2));
        languages.add(createReferential("LANG_ES", "LANGUAGE", "Spanish", 
            "Spanish language", "es", null, languageTypeId, null, true, "en", 3));
        languages.add(createReferential("LANG_DE", "LANGUAGE", "German", 
            "German language", "de", null, languageTypeId, null, true, "en", 4));
        languages.add(createReferential("LANG_IT", "LANGUAGE", "Italian", 
            "Italian language", "it", null, languageTypeId, null, true, "en", 5));
        languages.add(createReferential("LANG_PT", "LANGUAGE", "Portuguese", 
            "Portuguese language", "pt", null, languageTypeId, null, true, "en", 6));
        languages.add(createReferential("LANG_AR", "LANGUAGE", "Arabic", 
            "Arabic language", "ar", null, languageTypeId, null, true, "en", 7));
        languages.add(createReferential("LANG_ZH", "LANGUAGE", "Chinese", 
            "Chinese language", "zh", null, languageTypeId, null, true, "en", 8));
        languages.add(createReferential("LANG_JA", "LANGUAGE", "Japanese", 
            "Japanese language", "ja", null, languageTypeId, null, true, "en", 9));
        languages.add(createReferential("LANG_RU", "LANGUAGE", "Russian", 
            "Russian language", "ru", null, languageTypeId, null, true, "en", 10));
        languages.add(createReferential("LANG_KO", "LANGUAGE", "Korean", 
            "Korean language", "ko", null, languageTypeId, null, true, "en", 11));
        languages.add(createReferential("LANG_NL", "LANGUAGE", "Dutch", 
            "Dutch language", "nl", null, languageTypeId, null, true, "en", 12));
        languages.add(createReferential("LANG_SV", "LANGUAGE", "Swedish", 
            "Swedish language", "sv", null, languageTypeId, null, true, "en", 13));
        languages.add(createReferential("LANG_PL", "LANGUAGE", "Polish", 
            "Polish language", "pl", null, languageTypeId, null, true, "en", 14));
        languages.add(createReferential("LANG_TR", "LANGUAGE", "Turkish", 
            "Turkish language", "tr", null, languageTypeId, null, true, "en", 15));
        languages.add(createReferential("LANG_TH", "LANGUAGE", "Thai", 
            "Thai language", "th", null, languageTypeId, null, true, "en", 16));
        languages.add(createReferential("LANG_VI", "LANGUAGE", "Vietnamese", 
            "Vietnamese language", "vi", null, languageTypeId, null, true, "en", 17));
        languages.add(createReferential("LANG_ID", "LANGUAGE", "Indonesian", 
            "Indonesian language", "id", null, languageTypeId, null, true, "en", 18));
        languages.add(createReferential("LANG_HI", "LANGUAGE", "Hindi", 
            "Hindi language", "hi", null, languageTypeId, null, true, "en", 19));
        allReferentials.addAll(referentialRepository.saveAll(languages));

        // Regions
        List<ReferentialEntity> regions = new ArrayList<>();
        regions.add(createReferential("REG_AFRICA", "REGION", "Africa", 
            "African continent", "AF", null, regionTypeId, null, true, "en", 1));
        regions.add(createReferential("REG_AMERICAS", "REGION", "Americas", 
            "American continents", "AM", null, regionTypeId, null, true, "en", 2));
        regions.add(createReferential("REG_ASIA", "REGION", "Asia", 
            "Asian continent", "AS", null, regionTypeId, null, true, "en", 3));
        regions.add(createReferential("REG_EUROPE", "REGION", "Europe", 
            "European continent", "EU", null, regionTypeId, null, true, "en", 4));
        regions.add(createReferential("REG_OCEANIA", "REGION", "Oceania", 
            "Oceania continent", "OC", null, regionTypeId, null, true, "en", 5));

        // Save regions to get IDs
        List<ReferentialEntity> savedRegions = referentialRepository.saveAll(regions);
        allReferentials.addAll(savedRegions);
        Long americasId = savedRegions.stream().filter(r -> r.getCode().equals("REG_AMERICAS")).findFirst().get().getId();
        Long asiaId = savedRegions.stream().filter(r -> r.getCode().equals("REG_ASIA")).findFirst().get().getId();
        Long europeId = savedRegions.stream().filter(r -> r.getCode().equals("REG_EUROPE")).findFirst().get().getId();

        // Sub-regions
        List<ReferentialEntity> subRegions = new ArrayList<>();
        subRegions.add(createReferential("SUBREG_WESTERN_EUROPE", "SUB_REGION", "Western Europe", 
            "Western European sub-region", "WEU", null, subRegionTypeId, europeId, true, "en", 1));
        subRegions.add(createReferential("SUBREG_EASTERN_EUROPE", "SUB_REGION", "Eastern Europe", 
            "Eastern European sub-region", "EEU", null, subRegionTypeId, europeId, true, "en", 2));
        subRegions.add(createReferential("SUBREG_NORTH_AMERICA", "SUB_REGION", "North America", 
            "North American sub-region", "NAM", null, subRegionTypeId, americasId, true, "en", 3));
        subRegions.add(createReferential("SUBREG_SOUTH_AMERICA", "SUB_REGION", "South America", 
            "South American sub-region", "SAM", null, subRegionTypeId, americasId, true, "en", 4));
        subRegions.add(createReferential("SUBREG_MIDDLE_EAST", "SUB_REGION", "Middle East", 
            "Middle Eastern sub-region", "ME", null, subRegionTypeId, asiaId, true, "en", 5));
        subRegions.add(createReferential("SUBREG_SOUTH_ASIA", "SUB_REGION", "South Asia", 
            "South Asian sub-region", "SAS", null, subRegionTypeId, asiaId, true, "en", 6));
        subRegions.add(createReferential("SUBREG_EAST_ASIA", "SUB_REGION", "East Asia", 
            "East Asian sub-region", "EAS", null, subRegionTypeId, asiaId, true, "en", 7));
        allReferentials.addAll(referentialRepository.saveAll(subRegions));

        // Team Types
        List<ReferentialEntity> teamTypes = new ArrayList<>();
        teamTypes.add(createReferential("TEAM_TYPE_CORPORATION", "TEAM_TYPE", "Corporation", 
            "Large corporation or company", "CORP", null, teamTypeTypeId, null, true, "en", 1));
        teamTypes.add(createReferential("TEAM_TYPE_SME", "TEAM_TYPE", "Small/Medium Enterprise", 
            "Small or medium-sized enterprise", "SME", null, teamTypeTypeId, null, true, "en", 2));
        teamTypes.add(createReferential("TEAM_TYPE_STARTUP", "TEAM_TYPE", "Startup", 
            "Startup company", "STARTUP", null, teamTypeTypeId, null, true, "en", 3));
        teamTypes.add(createReferential("TEAM_TYPE_NONPROFIT", "TEAM_TYPE", "Non-Profit", 
            "Non-profit organization", "NPO", null, teamTypeTypeId, null, true, "en", 4));
        teamTypes.add(createReferential("TEAM_TYPE_GOVERNMENT", "TEAM_TYPE", "Government", 
            "Government organization", "GOV", null, teamTypeTypeId, null, true, "en", 5));
        allReferentials.addAll(referentialRepository.saveAll(teamTypes));

        // Industries
        List<ReferentialEntity> industries = new ArrayList<>();
        industries.add(createReferential("IND_TECH", "INDUSTRY", "Technology", 
            "Technology and software industry", "TECH", null, industryTypeId, null, true, "en", 1));
        industries.add(createReferential("IND_FINANCE", "INDUSTRY", "Finance", 
            "Financial services industry", "FIN", null, industryTypeId, null, true, "en", 2));
        industries.add(createReferential("IND_HEALTHCARE", "INDUSTRY", "Healthcare", 
            "Healthcare and medical industry", "HEALTH", null, industryTypeId, null, true, "en", 3));
        industries.add(createReferential("IND_EDUCATION", "INDUSTRY", "Education", 
            "Education and training industry", "EDU", null, industryTypeId, null, true, "en", 4));
        industries.add(createReferential("IND_MANUFACTURING", "INDUSTRY", "Manufacturing", 
            "Manufacturing industry", "MFG", null, industryTypeId, null, true, "en", 5));
        industries.add(createReferential("IND_RETAIL", "INDUSTRY", "Retail", 
            "Retail and commerce industry", "RETAIL", null, industryTypeId, null, true, "en", 6));
        industries.add(createReferential("IND_CONSULTING", "INDUSTRY", "Consulting", 
            "Consulting and professional services", "CONSULT", null, industryTypeId, null, true, "en", 7));
        industries.add(createReferential("IND_ENERGY", "INDUSTRY", "Energy", 
            "Energy and utilities industry", "ENERGY", null, industryTypeId, null, true, "en", 8));
        industries.add(createReferential("IND_TELECOM", "INDUSTRY", "Telecommunications", 
            "Telecommunications industry", "TELECOM", null, industryTypeId, null, true, "en", 9));
        industries.add(createReferential("IND_MEDIA", "INDUSTRY", "Media & Entertainment", 
            "Media and entertainment industry", "MEDIA", null, industryTypeId, null, true, "en", 10));
        industries.add(createReferential("IND_TRANSPORT", "INDUSTRY", "Transportation", 
            "Transportation and logistics industry", "TRANS", null, industryTypeId, null, true, "en", 11));
        industries.add(createReferential("IND_REALESTATE", "INDUSTRY", "Real Estate", 
            "Real estate industry", "RE", null, industryTypeId, null, true, "en", 12));
        industries.add(createReferential("IND_FOOD", "INDUSTRY", "Food & Beverage", 
            "Food and beverage industry", "FOOD", null, industryTypeId, null, true, "en", 13));
        industries.add(createReferential("IND_AUTOMOTIVE", "INDUSTRY", "Automotive", 
            "Automotive industry", "AUTO", null, industryTypeId, null, true, "en", 14));
        allReferentials.addAll(referentialRepository.saveAll(industries));

        // Address Types
        List<ReferentialEntity> addressTypes = new ArrayList<>();
        addressTypes.add(createReferential("ADDR_TYPE_HEADQUARTERS", "ADDRESS_TYPE", "Headquarters", 
            "Main headquarters address", "HQ", null, addressTypeTypeId, null, true, "en", 1));
        addressTypes.add(createReferential("ADDR_TYPE_BRANCH", "ADDRESS_TYPE", "Branch Office", 
            "Branch office address", "BRANCH", null, addressTypeTypeId, null, true, "en", 2));
        addressTypes.add(createReferential("ADDR_TYPE_MAILING", "ADDRESS_TYPE", "Mailing Address", 
            "Mailing address", "MAIL", null, addressTypeTypeId, null, true, "en", 3));
        addressTypes.add(createReferential("ADDR_TYPE_BILLING", "ADDRESS_TYPE", "Billing Address", 
            "Billing address", "BILL", null, addressTypeTypeId, null, true, "en", 4));
        allReferentials.addAll(referentialRepository.saveAll(addressTypes));

        // Phone Types
        List<ReferentialEntity> phoneTypes = new ArrayList<>();
        phoneTypes.add(createReferential("PHONE_TYPE_MAIN", "PHONE_TYPE", "Main Phone", 
            "Main business phone number", "MAIN", null, phoneTypeTypeId, null, true, "en", 1));
        phoneTypes.add(createReferential("PHONE_TYPE_FAX", "PHONE_TYPE", "Fax", 
            "Fax number", "FAX", null, phoneTypeTypeId, null, true, "en", 2));
        phoneTypes.add(createReferential("PHONE_TYPE_MOBILE", "PHONE_TYPE", "Mobile", 
            "Mobile phone number", "MOBILE", null, phoneTypeTypeId, null, true, "en", 3));
        phoneTypes.add(createReferential("PHONE_TYPE_SUPPORT", "PHONE_TYPE", "Support", 
            "Customer support phone number", "SUPPORT", null, phoneTypeTypeId, null, true, "en", 4));
        allReferentials.addAll(referentialRepository.saveAll(phoneTypes));

        logger.info("Seeded {} referentials", allReferentials.size());
    }

    private ReferentialEntity createReferential(String code, String dataType, String label, 
                                               String description, String externalCode, String iconUrl,
                                               Long typeId, Long parentId, boolean active, String locale, Integer displayOrder) {
        ReferentialEntity ref = new ReferentialEntity();
        ref.setCode(code);
        ref.setDataType(dataType);
        ref.setLabel(label);
        ref.setDescription(description);
        ref.setExternalCode(externalCode);
        ref.setIconUrl(iconUrl);
        ref.setTypeId(typeId);
        ref.setParentId(parentId);
        ref.setActive(active);
        ref.setLocale(locale);
        ref.setDisplayOrder(displayOrder);
        ref.setCreatedBy("system");
        ref.setUpdatedBy("system");
        ref.setVersion(1);
        ref.setCreatedAt(LocalDateTime.now());
        ref.setUpdatedAt(LocalDateTime.now());
        return ref;
    }

    /**
     * Seeds country data with real-world countries.
     */
    private void seedCountries() {
        logger.info("Seeding countries...");
        
        // Get referentials for countries
        ReferentialEntity langEn = referentialRepository.findByCodeAndDataType("LANG_EN", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_EN not found"));
        ReferentialEntity langFr = referentialRepository.findByCodeAndDataType("LANG_FR", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_FR not found"));
        ReferentialEntity langEs = referentialRepository.findByCodeAndDataType("LANG_ES", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_ES not found"));
        ReferentialEntity langDe = referentialRepository.findByCodeAndDataType("LANG_DE", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_DE not found"));
        ReferentialEntity langAr = referentialRepository.findByCodeAndDataType("LANG_AR", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_AR not found"));
        ReferentialEntity langZh = referentialRepository.findByCodeAndDataType("LANG_ZH", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_ZH not found"));
        ReferentialEntity langJa = referentialRepository.findByCodeAndDataType("LANG_JA", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_JA not found"));
        ReferentialEntity langPt = referentialRepository.findByCodeAndDataType("LANG_PT", "LANGUAGE")
            .orElseThrow(() -> new RuntimeException("Language LANG_PT not found"));

        ReferentialEntity regionEurope = referentialRepository.findByCodeAndDataType("REG_EUROPE", "REGION")
            .orElseThrow(() -> new RuntimeException("Region REG_EUROPE not found"));
        ReferentialEntity regionAmericas = referentialRepository.findByCodeAndDataType("REG_AMERICAS", "REGION")
            .orElseThrow(() -> new RuntimeException("Region REG_AMERICAS not found"));
        ReferentialEntity regionAsia = referentialRepository.findByCodeAndDataType("REG_ASIA", "REGION")
            .orElseThrow(() -> new RuntimeException("Region REG_ASIA not found"));
        ReferentialEntity regionAfrica = referentialRepository.findByCodeAndDataType("REG_AFRICA", "REGION")
            .orElseThrow(() -> new RuntimeException("Region REG_AFRICA not found"));
        ReferentialEntity regionOceania = referentialRepository.findByCodeAndDataType("REG_OCEANIA", "REGION")
            .orElseThrow(() -> new RuntimeException("Region REG_OCEANIA not found"));

        ReferentialEntity subRegionWesternEurope = referentialRepository.findByCodeAndDataType("SUBREG_WESTERN_EUROPE", "SUB_REGION")
            .orElseThrow(() -> new RuntimeException("Sub-region SUBREG_WESTERN_EUROPE not found"));
        ReferentialEntity subRegionNorthAmerica = referentialRepository.findByCodeAndDataType("SUBREG_NORTH_AMERICA", "SUB_REGION")
            .orElseThrow(() -> new RuntimeException("Sub-region SUBREG_NORTH_AMERICA not found"));
        ReferentialEntity subRegionMiddleEast = referentialRepository.findByCodeAndDataType("SUBREG_MIDDLE_EAST", "SUB_REGION")
            .orElseThrow(() -> new RuntimeException("Sub-region SUBREG_MIDDLE_EAST not found"));
        ReferentialEntity subRegionEastAsia = referentialRepository.findByCodeAndDataType("SUBREG_EAST_ASIA", "SUB_REGION")
            .orElseThrow(() -> new RuntimeException("Sub-region SUBREG_EAST_ASIA not found"));

        // Get timezones
        TimezoneEntity tzParis = timezoneRepository.findByCode("Europe/Paris")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/Paris not found"));
        TimezoneEntity tzLondon = timezoneRepository.findByCode("Europe/London")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/London not found"));
        TimezoneEntity tzNewYork = timezoneRepository.findByCode("America/New_York")
            .orElseThrow(() -> new RuntimeException("Timezone America/New_York not found"));
        TimezoneEntity tzTokyo = timezoneRepository.findByCode("Asia/Tokyo")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Tokyo not found"));
        TimezoneEntity tzDubai = timezoneRepository.findByCode("Asia/Dubai")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Dubai not found"));
        TimezoneEntity tzShanghai = timezoneRepository.findByCode("Asia/Shanghai")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Shanghai not found"));
        TimezoneEntity tzCairo = timezoneRepository.findByCode("Africa/Cairo")
            .orElseThrow(() -> new RuntimeException("Timezone Africa/Cairo not found"));
        TimezoneEntity tzSydney = timezoneRepository.findByCode("Australia/Sydney")
            .orElseThrow(() -> new RuntimeException("Timezone Australia/Sydney not found"));
        TimezoneEntity tzSingapore = timezoneRepository.findByCode("Asia/Singapore")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Singapore not found"));
        TimezoneEntity tzSeoul = timezoneRepository.findByCode("Asia/Seoul")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Seoul not found"));
        TimezoneEntity tzMexicoCity = timezoneRepository.findByCode("America/Mexico_City")
            .orElseThrow(() -> new RuntimeException("Timezone America/Mexico_City not found"));
        TimezoneEntity tzAmsterdam = timezoneRepository.findByCode("Europe/Amsterdam")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/Amsterdam not found"));
        TimezoneEntity tzStockholm = timezoneRepository.findByCode("Europe/Stockholm")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/Stockholm not found"));
        TimezoneEntity tzBangkok = timezoneRepository.findByCode("Asia/Bangkok")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Bangkok not found"));
        TimezoneEntity tzAuckland = timezoneRepository.findByCode("Pacific/Auckland").orElse(null);

        List<CountryEntity> countries = new ArrayList<>();

        // Europe
        countries.add(createCountry("France", "France", "FR", "FRA", "250", "+33", "EUR", ".fr", 
            regionEurope.getId(), subRegionWesternEurope.getId(), langFr.getId(), tzParis.getId(), true, true));
        countries.add(createCountry("United Kingdom", "United Kingdom", "GB", "GBR", "826", "+44", "GBP", ".uk", 
            regionEurope.getId(), subRegionWesternEurope.getId(), langEn.getId(), tzLondon.getId(), true, true));
        countries.add(createCountry("Germany", "Deutschland", "DE", "DEU", "276", "+49", "EUR", ".de", 
            regionEurope.getId(), subRegionWesternEurope.getId(), langDe.getId(), tzParis.getId(), true, true));
        countries.add(createCountry("Spain", "España", "ES", "ESP", "724", "+34", "EUR", ".es", 
            regionEurope.getId(), subRegionWesternEurope.getId(), langEs.getId(), tzParis.getId(), true, true));
        countries.add(createCountry("Italy", "Italia", "IT", "ITA", "380", "+39", "EUR", ".it", 
            regionEurope.getId(), subRegionWesternEurope.getId(), langEn.getId(), tzParis.getId(), true, true));

        // Americas
        countries.add(createCountry("United States", "United States", "US", "USA", "840", "+1", "USD", ".us", 
            regionAmericas.getId(), subRegionNorthAmerica.getId(), langEn.getId(), tzNewYork.getId(), true, true));
        countries.add(createCountry("Canada", "Canada", "CA", "CAN", "124", "+1", "CAD", ".ca", 
            regionAmericas.getId(), subRegionNorthAmerica.getId(), langEn.getId(), tzNewYork.getId(), true, true));
        countries.add(createCountry("Brazil", "Brasil", "BR", "BRA", "076", "+55", "BRL", ".br", 
            regionAmericas.getId(), null, langPt.getId(), null, true, true));

        // Asia
        countries.add(createCountry("Japan", "日本", "JP", "JPN", "392", "+81", "JPY", ".jp", 
            regionAsia.getId(), subRegionEastAsia.getId(), langJa.getId(), tzTokyo.getId(), true, true));
        countries.add(createCountry("China", "中国", "CN", "CHN", "156", "+86", "CNY", ".cn", 
            regionAsia.getId(), subRegionEastAsia.getId(), langZh.getId(), tzShanghai.getId(), true, true));
        countries.add(createCountry("United Arab Emirates", "الإمارات العربية المتحدة", "AE", "ARE", "784", "+971", "AED", ".ae", 
            regionAsia.getId(), subRegionMiddleEast.getId(), langAr.getId(), tzDubai.getId(), true, true));
        countries.add(createCountry("India", "भारत", "IN", "IND", "356", "+91", "INR", ".in", 
            regionAsia.getId(), null, langEn.getId(), null, true, true));

        // Africa
        countries.add(createCountry("Egypt", "مصر", "EG", "EGY", "818", "+20", "EGP", ".eg", 
            regionAfrica.getId(), null, langAr.getId(), tzCairo.getId(), true, true));
        countries.add(createCountry("South Africa", "South Africa", "ZA", "ZAF", "710", "+27", "ZAR", ".za", 
            regionAfrica.getId(), null, langEn.getId(), null, true, true));
        countries.add(createCountry("Australia", "Australia", "AU", "AUS", "036", "+61", "AUD", ".au", 
            regionOceania.getId(), null, langEn.getId(), tzSydney.getId(), true, true));
        countries.add(createCountry("New Zealand", "New Zealand", "NZ", "NZL", "554", "+64", "NZD", ".nz", 
            regionOceania.getId(), null, langEn.getId(), tzAuckland != null ? tzAuckland.getId() : null, true, true));
        countries.add(createCountry("Singapore", "Singapore", "SG", "SGP", "702", "+65", "SGD", ".sg", 
            regionAsia.getId(), null, langEn.getId(), tzSingapore.getId(), true, true));
        countries.add(createCountry("South Korea", "대한민국", "KR", "KOR", "410", "+82", "KRW", ".kr", 
            regionAsia.getId(), null, langEn.getId(), tzSeoul.getId(), true, true));
        countries.add(createCountry("Mexico", "México", "MX", "MEX", "484", "+52", "MXN", ".mx", 
            regionAmericas.getId(), null, langEs.getId(), tzMexicoCity.getId(), true, true));
        countries.add(createCountry("Argentina", "Argentina", "AR", "ARG", "032", "+54", "ARS", ".ar", 
            regionAmericas.getId(), null, langEs.getId(), null, true, true));
        countries.add(createCountry("Netherlands", "Nederland", "NL", "NLD", "528", "+31", "EUR", ".nl", 
            regionEurope.getId(), null, langEn.getId(), tzAmsterdam.getId(), true, true));
        countries.add(createCountry("Sweden", "Sverige", "SE", "SWE", "752", "+46", "SEK", ".se", 
            regionEurope.getId(), null, langEn.getId(), tzStockholm.getId(), true, true));
        countries.add(createCountry("Poland", "Polska", "PL", "POL", "616", "+48", "PLN", ".pl", 
            regionEurope.getId(), null, langEn.getId(), null, true, true));
        countries.add(createCountry("Turkey", "Türkiye", "TR", "TUR", "792", "+90", "TRY", ".tr", 
            regionAsia.getId(), null, langEn.getId(), null, true, true));
        countries.add(createCountry("Thailand", "ประเทศไทย", "TH", "THA", "764", "+66", "THB", ".th", 
            regionAsia.getId(), null, langEn.getId(), tzBangkok.getId(), true, true));
        countries.add(createCountry("Vietnam", "Việt Nam", "VN", "VNM", "704", "+84", "VND", ".vn", 
            regionAsia.getId(), null, langEn.getId(), null, true, true));
        countries.add(createCountry("Indonesia", "Indonesia", "ID", "IDN", "360", "+62", "IDR", ".id", 
            regionAsia.getId(), null, langEn.getId(), null, true, true));

        countryRepository.saveAll(countries);
        logger.info("Seeded {} countries", countries.size());
    }

    private CountryEntity createCountry(String name, String nativeName, String iso2, String iso3, 
                                       String numericCode, String phoneCode, String currencyCode, String domain,
                                       Long regionId, Long subRegionId, Long languageId, Long timezoneId,
                                       boolean enabled, boolean active) {
        CountryEntity country = new CountryEntity();
        country.setName(name);
        country.setNativeName(nativeName);
        country.setIso2(iso2);
        country.setIso3(iso3);
        country.setNumericCode(numericCode);
        country.setPhoneCode(phoneCode);
        country.setCurrencyCode(currencyCode);
        country.setDomain(domain);
        country.setRegionId(regionId);
        country.setSubRegionId(subRegionId);
        country.setLanguageId(languageId);
        country.setTimezoneId(timezoneId);
        country.setEnabled(enabled);
        country.setActive(active);
        country.setCreatedBy("system");
        country.setUpdatedBy("system");
        country.setVersion(1);
        country.setCreatedAt(LocalDateTime.now());
        country.setUpdatedAt(LocalDateTime.now());
        return country;
    }

    /**
     * Seeds city data with real-world cities.
     */
    private void seedCities() {
        logger.info("Seeding cities...");

        // Get countries
        CountryEntity france = countryRepository.findByIso2("FR")
            .orElseThrow(() -> new RuntimeException("Country FR not found"));
        CountryEntity uk = countryRepository.findByIso2("GB")
            .orElseThrow(() -> new RuntimeException("Country GB not found"));
        CountryEntity usa = countryRepository.findByIso2("US")
            .orElseThrow(() -> new RuntimeException("Country US not found"));
        CountryEntity japan = countryRepository.findByIso2("JP")
            .orElseThrow(() -> new RuntimeException("Country JP not found"));
        CountryEntity uae = countryRepository.findByIso2("AE")
            .orElseThrow(() -> new RuntimeException("Country AE not found"));
        CountryEntity china = countryRepository.findByIso2("CN")
            .orElseThrow(() -> new RuntimeException("Country CN not found"));
        CountryEntity egypt = countryRepository.findByIso2("EG")
            .orElseThrow(() -> new RuntimeException("Country EG not found"));

        // Get timezones
        TimezoneEntity tzParis = timezoneRepository.findByCode("Europe/Paris")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/Paris not found"));
        TimezoneEntity tzLondon = timezoneRepository.findByCode("Europe/London")
            .orElseThrow(() -> new RuntimeException("Timezone Europe/London not found"));
        TimezoneEntity tzNewYork = timezoneRepository.findByCode("America/New_York")
            .orElseThrow(() -> new RuntimeException("Timezone America/New_York not found"));
        TimezoneEntity tzTokyo = timezoneRepository.findByCode("Asia/Tokyo")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Tokyo not found"));
        TimezoneEntity tzDubai = timezoneRepository.findByCode("Asia/Dubai")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Dubai not found"));
        TimezoneEntity tzShanghai = timezoneRepository.findByCode("Asia/Shanghai")
            .orElseThrow(() -> new RuntimeException("Timezone Asia/Shanghai not found"));
        TimezoneEntity tzCairo = timezoneRepository.findByCode("Africa/Cairo")
            .orElseThrow(() -> new RuntimeException("Timezone Africa/Cairo not found"));

        List<CityEntity> cities = new ArrayList<>();

        // France
        cities.add(createCity("Paris", "Paris", france.getId(), tzParis.getId(), 48.8566, 2.3522, true, true, true));
        cities.add(createCity("Lyon", "Lyon", france.getId(), tzParis.getId(), 45.7640, 4.8357, false, false, true));
        cities.add(createCity("Marseille", "Marseille", france.getId(), tzParis.getId(), 43.2965, 5.3698, false, false, true));

        // United Kingdom
        cities.add(createCity("London", "London", uk.getId(), tzLondon.getId(), 51.5074, -0.1278, true, true, true));
        cities.add(createCity("Manchester", "Manchester", uk.getId(), tzLondon.getId(), 53.4808, -2.2426, false, false, true));
        cities.add(createCity("Birmingham", "Birmingham", uk.getId(), tzLondon.getId(), 52.4862, -1.8904, false, false, true));

        // United States
        cities.add(createCity("New York", "New York", usa.getId(), tzNewYork.getId(), 40.7128, -74.0060, false, false, true));
        cities.add(createCity("Los Angeles", "Los Angeles", usa.getId(), null, 34.0522, -118.2437, false, false, true));
        cities.add(createCity("Chicago", "Chicago", usa.getId(), null, 41.8781, -87.6298, false, false, true));

        // Japan
        cities.add(createCity("Tokyo", "東京", japan.getId(), tzTokyo.getId(), 35.6762, 139.6503, true, true, true));
        cities.add(createCity("Osaka", "大阪", japan.getId(), tzTokyo.getId(), 34.6937, 135.5023, false, false, true));
        cities.add(createCity("Kyoto", "京都", japan.getId(), tzTokyo.getId(), 35.0116, 135.7681, false, false, true));

        // UAE
        cities.add(createCity("Dubai", "دبي", uae.getId(), tzDubai.getId(), 25.2048, 55.2708, false, false, true));
        cities.add(createCity("Abu Dhabi", "أبو ظبي", uae.getId(), tzDubai.getId(), 24.4539, 54.3773, true, false, true));

        // China
        cities.add(createCity("Beijing", "北京", china.getId(), tzShanghai.getId(), 39.9042, 116.4074, true, true, true));
        cities.add(createCity("Shanghai", "上海", china.getId(), tzShanghai.getId(), 31.2304, 121.4737, false, false, true));

        // Egypt
        cities.add(createCity("Cairo", "القاهرة", egypt.getId(), tzCairo.getId(), 30.0444, 31.2357, true, true, true));
        cities.add(createCity("Alexandria", "الإسكندرية", egypt.getId(), tzCairo.getId(), 31.2001, 29.9187, false, false, true));

        // Get additional countries
        CountryEntity australia = countryRepository.findByIso2("AU").orElse(null);
        CountryEntity singapore = countryRepository.findByIso2("SG").orElse(null);
        CountryEntity southKorea = countryRepository.findByIso2("KR").orElse(null);
        CountryEntity mexico = countryRepository.findByIso2("MX").orElse(null);
        CountryEntity netherlands = countryRepository.findByIso2("NL").orElse(null);
        CountryEntity sweden = countryRepository.findByIso2("SE").orElse(null);
        CountryEntity thailand = countryRepository.findByIso2("TH").orElse(null);
        
        TimezoneEntity tzSydney = timezoneRepository.findByCode("Australia/Sydney").orElse(null);
        TimezoneEntity tzSingapore = timezoneRepository.findByCode("Asia/Singapore").orElse(null);
        TimezoneEntity tzSeoul = timezoneRepository.findByCode("Asia/Seoul").orElse(null);
        TimezoneEntity tzMexicoCity = timezoneRepository.findByCode("America/Mexico_City").orElse(null);
        TimezoneEntity tzAmsterdam = timezoneRepository.findByCode("Europe/Amsterdam").orElse(null);
        TimezoneEntity tzStockholm = timezoneRepository.findByCode("Europe/Stockholm").orElse(null);
        TimezoneEntity tzBangkok = timezoneRepository.findByCode("Asia/Bangkok").orElse(null);

        if (australia != null && tzSydney != null) {
            cities.add(createCity("Sydney", "Sydney", australia.getId(), tzSydney.getId(), -33.8688, 151.2093, false, true, true));
            cities.add(createCity("Melbourne", "Melbourne", australia.getId(), tzSydney.getId(), -37.8136, 144.9631, false, true, true));
        }
        if (singapore != null && tzSingapore != null) {
            cities.add(createCity("Singapore", "Singapore", singapore.getId(), tzSingapore.getId(), 1.3521, 103.8198, true, true, true));
        }
        if (southKorea != null && tzSeoul != null) {
            cities.add(createCity("Seoul", "서울", southKorea.getId(), tzSeoul.getId(), 37.5665, 126.9780, true, true, true));
        }
        if (mexico != null && tzMexicoCity != null) {
            cities.add(createCity("Mexico City", "Ciudad de México", mexico.getId(), tzMexicoCity.getId(), 19.4326, -99.1332, true, true, true));
        }
        if (netherlands != null && tzAmsterdam != null) {
            cities.add(createCity("Amsterdam", "Amsterdam", netherlands.getId(), tzAmsterdam.getId(), 52.3676, 4.9041, true, true, true));
        }
        if (sweden != null && tzStockholm != null) {
            cities.add(createCity("Stockholm", "Stockholm", sweden.getId(), tzStockholm.getId(), 59.3293, 18.0686, true, true, true));
        }
        if (thailand != null && tzBangkok != null) {
            cities.add(createCity("Bangkok", "กรุงเทพมหานคร", thailand.getId(), tzBangkok.getId(), 13.7563, 100.5018, true, true, true));
        }

        cityRepository.saveAll(cities);
        logger.info("Seeded {} cities", cities.size());
    }

    private CityEntity createCity(String name, String nativeName, Long countryId, Long timezoneId,
                                 Double latitude, Double longitude, boolean isCapital, boolean isMetropolitan, boolean active) {
        CityEntity city = new CityEntity();
        city.setName(name);
        city.setNativeName(nativeName);
        city.setCountryId(countryId);
        city.setTimezoneId(timezoneId);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        city.setCapital(isCapital);
        city.setMetropolitan(isMetropolitan);
        city.setActive(active);
        city.setCreatedBy("system");
        city.setUpdatedBy("system");
        city.setVersion(1);
        city.setCreatedAt(LocalDateTime.now());
        city.setUpdatedAt(LocalDateTime.now());
        return city;
    }

    /**
     * Seeds organization/team data with real-world organizations.
     */
    private void seedOrganizations() {
        logger.info("Seeding organizations...");

        // Get required referentials
        ReferentialEntity teamTypeCorp = referentialRepository.findByCodeAndDataType("TEAM_TYPE_CORPORATION", "TEAM_TYPE")
            .orElseThrow(() -> new RuntimeException("Team type TEAM_TYPE_CORPORATION not found"));
        ReferentialEntity teamTypeSME = referentialRepository.findByCodeAndDataType("TEAM_TYPE_SME", "TEAM_TYPE")
            .orElseThrow(() -> new RuntimeException("Team type TEAM_TYPE_SME not found"));
        ReferentialEntity teamTypeStartup = referentialRepository.findByCodeAndDataType("TEAM_TYPE_STARTUP", "TEAM_TYPE")
            .orElseThrow(() -> new RuntimeException("Team type TEAM_TYPE_STARTUP not found"));

        ReferentialEntity industryTech = referentialRepository.findByCodeAndDataType("IND_TECH", "INDUSTRY")
            .orElseThrow(() -> new RuntimeException("Industry IND_TECH not found"));
        ReferentialEntity industryFinance = referentialRepository.findByCodeAndDataType("IND_FINANCE", "INDUSTRY")
            .orElseThrow(() -> new RuntimeException("Industry IND_FINANCE not found"));
        ReferentialEntity industryHealthcare = referentialRepository.findByCodeAndDataType("IND_HEALTHCARE", "INDUSTRY")
            .orElseThrow(() -> new RuntimeException("Industry IND_HEALTHCARE not found"));
        ReferentialEntity industryConsulting = referentialRepository.findByCodeAndDataType("IND_CONSULTING", "INDUSTRY")
            .orElseThrow(() -> new RuntimeException("Industry IND_CONSULTING not found"));

        ReferentialEntity addrTypeHQ = referentialRepository.findByCodeAndDataType("ADDR_TYPE_HEADQUARTERS", "ADDRESS_TYPE")
            .orElseThrow(() -> new RuntimeException("Address type ADDR_TYPE_HEADQUARTERS not found"));
        ReferentialEntity addrTypeBranch = referentialRepository.findByCodeAndDataType("ADDR_TYPE_BRANCH", "ADDRESS_TYPE")
            .orElseThrow(() -> new RuntimeException("Address type ADDR_TYPE_BRANCH not found"));

        ReferentialEntity phoneTypeMain = referentialRepository.findByCodeAndDataType("PHONE_TYPE_MAIN", "PHONE_TYPE")
            .orElseThrow(() -> new RuntimeException("Phone type PHONE_TYPE_MAIN not found"));
        ReferentialEntity phoneTypeFax = referentialRepository.findByCodeAndDataType("PHONE_TYPE_FAX", "PHONE_TYPE")
            .orElseThrow(() -> new RuntimeException("Phone type PHONE_TYPE_FAX not found"));

        // Get countries and cities
        CountryEntity usa = countryRepository.findByIso2("US")
            .orElseThrow(() -> new RuntimeException("Country US not found"));
        CountryEntity france = countryRepository.findByIso2("FR")
            .orElseThrow(() -> new RuntimeException("Country FR not found"));
        CountryEntity uk = countryRepository.findByIso2("GB")
            .orElseThrow(() -> new RuntimeException("Country GB not found"));

        CityEntity paris = cityRepository.findByNameAndCountryId("Paris", france.getId())
            .orElse(null);
        CityEntity london = cityRepository.findByNameAndCountryId("London", uk.getId())
            .orElse(null);
        CityEntity newYork = cityRepository.findByNameAndCountryId("New York", usa.getId())
            .orElse(null);

        List<TeamEntity> teams = new ArrayList<>();

        // Create sample organizations
        TeamEntity techCorp = createTeam("TECH001", "TECH-CORP-001", "TechCorp Solutions", 
            "Leading technology solutions provider specializing in enterprise software", 
            "contact@techcorp.com", "https://www.techcorp.com", "12-3456789", "FR12345678901", 
            null, java.time.LocalDate.of(2010, 5, 15), 500, "Main technology company", 
            teamTypeCorp.getId(), industryTech.getId(), null, true);
        teams.add(techCorp);

        TeamEntity financeSME = createTeam("FIN001", "FIN-SME-001", "Global Finance Group", 
            "Financial services and investment advisory firm", 
            "info@globalfinance.com", "https://www.globalfinance.com", "98-7654321", "US987654321", 
            null, java.time.LocalDate.of(2005, 3, 20), 150, "Financial services company", 
            teamTypeSME.getId(), industryFinance.getId(), null, true);
        teams.add(financeSME);

        TeamEntity healthStartup = createTeam("HEALTH001", "HEALTH-START-001", "HealthTech Innovations", 
            "Innovative healthcare technology startup", 
            "hello@healthtech.io", "https://www.healthtech.io", null, null, 
            null, java.time.LocalDate.of(2020, 8, 10), 25, "Healthcare startup", 
            teamTypeStartup.getId(), industryHealthcare.getId(), null, true);
        teams.add(healthStartup);

        TeamEntity consultingCorp = createTeam("CONSULT001", "CONSULT-CORP-001", "Strategic Consulting Partners", 
            "Management and strategic consulting services", 
            "contact@strategicconsult.com", "https://www.strategicconsult.com", "55-1234567", "GB551234567", 
            null, java.time.LocalDate.of(1995, 1, 1), 300, "Consulting firm", 
            teamTypeCorp.getId(), industryConsulting.getId(), null, true);
        teams.add(consultingCorp);

        ReferentialEntity industryEnergy = referentialRepository.findByCodeAndDataType("IND_ENERGY", "INDUSTRY").orElse(null);
        ReferentialEntity industryTelecom = referentialRepository.findByCodeAndDataType("IND_TELECOM", "INDUSTRY").orElse(null);
        ReferentialEntity industryMedia = referentialRepository.findByCodeAndDataType("IND_MEDIA", "INDUSTRY").orElse(null);
        
        if (industryEnergy != null) {
            teams.add(createTeam("ENERGY001", "ENERGY-CORP-001", "Green Energy Solutions", 
                "Renewable energy and sustainability solutions", 
                "info@greenenergy.com", "https://www.greenenergy.com", "11-2233445", "US112233445", 
                null, java.time.LocalDate.of(2015, 6, 1), 200, "Energy company", 
                teamTypeCorp.getId(), industryEnergy.getId(), null, true));
        }
        
        if (industryTelecom != null) {
            teams.add(createTeam("TELECOM001", "TELECOM-CORP-001", "Global Telecom Networks", 
                "Telecommunications and network infrastructure", 
                "contact@globaltelecom.com", "https://www.globaltelecom.com", "22-3344556", "GB223344556", 
                null, java.time.LocalDate.of(2000, 3, 15), 1000, "Telecom provider", 
                teamTypeCorp.getId(), industryTelecom.getId(), null, true));
        }
        
        if (industryMedia != null) {
            teams.add(createTeam("MEDIA001", "MEDIA-SME-001", "Digital Media Agency", 
                "Digital marketing and media services", 
                "hello@digitalmedia.com", "https://www.digitalmedia.com", "33-4455667", "FR334455667", 
                null, java.time.LocalDate.of(2018, 9, 20), 80, "Media agency", 
                teamTypeSME.getId(), industryMedia.getId(), null, true));
        }

        // Save teams first
        teams = new ArrayList<>(teamRepository.saveAll(teams));
        logger.info("Seeded {} teams", teams.size());

        // Add addresses for teams
        List<TeamAddressEntity> addresses = new ArrayList<>();
        
        if (paris != null) {
            addresses.add(createAddress(techCorp.getId(), "123 Avenue des Champs-Élysées", null, null, 
                "Building A", "5th Floor", "Suite 501", "75008", "Île-de-France", 
                48.8698, 2.3077, paris.getId(), france.getId(), addrTypeHQ.getId(), true, true, 
                "Main headquarters"));
        }

        if (london != null) {
            addresses.add(createAddress(financeSME.getId(), "456 Canary Wharf", "One Canada Square", null, 
                null, "25th Floor", null, "E14 5AB", "Greater London", 
                51.5045, -0.0195, london.getId(), uk.getId(), addrTypeHQ.getId(), true, true, 
                "London headquarters"));
            
            addresses.add(createAddress(consultingCorp.getId(), "789 Oxford Street", null, null, 
                null, "3rd Floor", null, "W1D 2HZ", "Greater London", 
                51.5155, -0.1416, london.getId(), uk.getId(), addrTypeBranch.getId(), false, true, 
                "London branch office"));
        }

        if (newYork != null) {
            addresses.add(createAddress(financeSME.getId(), "100 Wall Street", null, null, 
                "Financial Plaza", "10th Floor", "Suite 1001", "10005", "New York", 
                40.7074, -74.0113, newYork.getId(), usa.getId(), addrTypeBranch.getId(), false, true, 
                "New York branch"));
            
            addresses.add(createAddress(healthStartup.getId(), "200 Broadway", null, null, 
                "Tech Hub", "15th Floor", null, "10038", "New York", 
                40.7128, -74.0060, newYork.getId(), usa.getId(), addrTypeHQ.getId(), true, true, 
                "Main office"));
        }

        teamAddressRepository.saveAll(addresses);
        logger.info("Seeded {} addresses", addresses.size());

        // Add phone numbers for teams
        List<TeamPhoneEntity> phones = new ArrayList<>();
        
        phones.add(createPhone(techCorp.getId(), "+33", "123456789", null, "+33 1 23 45 67 89", 
            phoneTypeMain.getId(), true, true, true, null, "Main business line"));
        phones.add(createPhone(techCorp.getId(), "+33", "123456790", null, "+33 1 23 45 67 90", 
            phoneTypeFax.getId(), false, true, true, null, "Fax number"));

        phones.add(createPhone(financeSME.getId(), "+44", "2076543210", null, "+44 20 7654 3210", 
            phoneTypeMain.getId(), true, true, true, null, "Main phone"));
        phones.add(createPhone(financeSME.getId(), "+1", "2125551234", null, "+1 (212) 555-1234", 
            phoneTypeMain.getId(), false, true, true, null, "New York office"));

        phones.add(createPhone(healthStartup.getId(), "+1", "2125555678", null, "+1 (212) 555-5678", 
            phoneTypeMain.getId(), true, true, true, null, "Main contact"));

        phones.add(createPhone(consultingCorp.getId(), "+44", "2076543299", null, "+44 20 7654 3299", 
            phoneTypeMain.getId(), true, true, true, null, "Main office phone"));

        teamPhoneRepository.saveAll(phones);
        logger.info("Seeded {} phone numbers", phones.size());
    }

    private TeamEntity createTeam(String internalCode, String externalCode, String name, 
                                 String description, String email, String website, String taxId, 
                                 String vatNumber, String logoUrl, java.time.LocalDate foundedDate, 
                                 Integer employeeCount, String notes, Long teamTypeId, Long industryId, 
                                 Long parentId, boolean active) {
        TeamEntity team = new TeamEntity();
        team.setInternalCode(internalCode);
        team.setExternalCode(externalCode);
        team.setName(name);
        team.setDescription(description);
        team.setEmail(email);
        team.setWebsite(website);
        team.setTaxId(taxId);
        team.setVatNumber(vatNumber);
        team.setLogoUrl(logoUrl);
        team.setFoundedDate(foundedDate);
        team.setEmployeeCount(employeeCount);
        team.setNotes(notes);
        team.setTeamTypeId(teamTypeId);
        team.setIndustryId(industryId);
        team.setParentId(parentId);
        team.setActive(active);
        team.setCreatedBy("system");
        team.setUpdatedBy("system");
        team.setVersion(1);
        team.setCreatedAt(LocalDateTime.now());
        team.setUpdatedAt(LocalDateTime.now());
        return team;
    }

    private TeamAddressEntity createAddress(Long teamId, String street, String street2, String street3,
                                           String building, String floor, String unit, String postalCode,
                                           String state, Double latitude, Double longitude, Long cityId,
                                           Long countryId, Long addressTypeId, boolean primary, 
                                           boolean validated, String notes) {
        TeamAddressEntity address = new TeamAddressEntity();
        address.setTeamId(teamId);
        address.setStreet(street);
        address.setStreet2(street2);
        address.setStreet3(street3);
        address.setBuilding(building);
        address.setFloor(floor);
        address.setUnit(unit);
        address.setPostalCode(postalCode);
        address.setState(state);
        address.setLatitude(latitude);
        address.setLongitude(longitude);
        address.setCityId(cityId);
        address.setCountryId(countryId);
        address.setAddressTypeId(addressTypeId);
        address.setPrimary(primary);
        address.setValidated(validated);
        address.setNotes(notes);
        address.setCreatedBy("system");
        address.setUpdatedBy("system");
        address.setVersion(1);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        return address;
    }

    private TeamPhoneEntity createPhone(Long teamId, String countryCode, String number, String extension,
                                       String formattedNumber, Long typeId, boolean primaryPhone, 
                                       boolean active, boolean verified, java.time.LocalDateTime lastVerifiedAt,
                                       String notes) {
        TeamPhoneEntity phone = new TeamPhoneEntity();
        phone.setTeamId(teamId);
        phone.setCountryCode(countryCode);
        phone.setNumber(number);
        phone.setExtension(extension);
        phone.setFormattedNumber(formattedNumber);
        phone.setTypeId(typeId);
        phone.setPrimaryPhone(primaryPhone);
        phone.setActive(active);
        phone.setVerified(verified);
        phone.setLastVerifiedAt(lastVerifiedAt);
        phone.setNotes(notes);
        phone.setCreatedBy("system");
        phone.setUpdatedBy("system");
        phone.setVersion(1);
        phone.setCreatedAt(LocalDateTime.now());
        phone.setUpdatedAt(LocalDateTime.now());
        return phone;
    }
}

