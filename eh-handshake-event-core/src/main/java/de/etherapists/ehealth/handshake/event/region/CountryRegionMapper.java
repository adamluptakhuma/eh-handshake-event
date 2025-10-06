package de.etherapists.ehealth.handshake.event.region;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import software.amazon.awssdk.regions.Region;

public class CountryRegionMapper {

  private static final Map<CountryEnum, Region> MAPPING = new EnumMap<>(CountryEnum.class);

  static {
    // Europe → Frankfurt, Paris, London, etc.
    MAPPING.put(CountryEnum.GERMANY, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.AUSTRIA, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.SWITZERLAND, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.FRANCE, Region.EU_WEST_3);
    MAPPING.put(CountryEnum.BELGIUM, Region.EU_WEST_3);
    MAPPING.put(CountryEnum.LUXEMBOURG, Region.EU_WEST_3);
    MAPPING.put(CountryEnum.UNITED_KINGDOM, Region.EU_WEST_2);
    MAPPING.put(CountryEnum.IRELAND, Region.EU_WEST_1);
    MAPPING.put(CountryEnum.SPAIN, Region.EU_SOUTH_2);
    MAPPING.put(CountryEnum.ITALY, Region.EU_SOUTH_1);
    MAPPING.put(CountryEnum.SWEDEN, Region.EU_NORTH_1);
    MAPPING.put(CountryEnum.POLAND, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.SLOVAKIA, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.CZECH_REPUBLIC, Region.EU_CENTRAL_1);

    // Americas
    MAPPING.put(CountryEnum.UNITED_STATES, Region.US_EAST_1);
    MAPPING.put(CountryEnum.CANADA, Region.CA_CENTRAL_1);
    MAPPING.put(CountryEnum.MEXICO, Region.US_EAST_1);
    MAPPING.put(CountryEnum.BRAZIL, Region.SA_EAST_1);
    MAPPING.put(CountryEnum.ARGENTINA, Region.SA_EAST_1);
    MAPPING.put(CountryEnum.CHILE, Region.SA_EAST_1);

    // Middle East
    MAPPING.put(CountryEnum.UNITED_ARAB_EMIRATES, Region.ME_CENTRAL_1);
    MAPPING.put(CountryEnum.SAUDI_ARABIA, Region.ME_SOUTH_1);
    MAPPING.put(CountryEnum.ISRAEL, Region.EU_CENTRAL_1);

    // Africa
    MAPPING.put(CountryEnum.SOUTH_AFRICA, Region.AF_SOUTH_1);
    MAPPING.put(CountryEnum.EGYPT, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.NIGERIA, Region.EU_CENTRAL_1);
    MAPPING.put(CountryEnum.KENYA, Region.EU_CENTRAL_1);

    // Asia-Pacific
    MAPPING.put(CountryEnum.INDIA, Region.AP_SOUTH_1);
    MAPPING.put(CountryEnum.PAKISTAN, Region.AP_SOUTH_1);
    MAPPING.put(CountryEnum.BANGLADESH, Region.AP_SOUTH_1);
    MAPPING.put(CountryEnum.SINGAPORE, Region.AP_SOUTHEAST_1);
    MAPPING.put(CountryEnum.MALAYSIA, Region.AP_SOUTHEAST_1);
    MAPPING.put(CountryEnum.PHILIPPINES, Region.AP_SOUTHEAST_1);
    MAPPING.put(CountryEnum.VIETNAM, Region.AP_SOUTHEAST_1);
    MAPPING.put(CountryEnum.HONG_KONG, Region.AP_EAST_1);
    MAPPING.put(CountryEnum.JAPAN, Region.AP_NORTHEAST_1);
    MAPPING.put(CountryEnum.KOREA_REPUBLIC_OF, Region.AP_NORTHEAST_2);
    MAPPING.put(CountryEnum.AUSTRALIA, Region.AP_SOUTHEAST_2);
    MAPPING.put(CountryEnum.NEW_ZEALAND, Region.AP_SOUTHEAST_2);
  }

  public static Optional<String> getRegionForCountry(CountryEnum country) {
    return Optional.ofNullable(MAPPING.get(country)).map(Region::id);
  }
}
