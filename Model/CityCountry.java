package Model;

public class CityCountry {
    
    private final String city, country;
    private final int cityId, countryId;

    public CityCountry(String city, String country, int cityId, int countryId) {
        this.city = city;
        this.country = country;
        this.cityId = cityId;
        this.countryId = countryId;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public int getCityId() {
        return cityId;
    }

    public int getCountryId() {
        return countryId;
    }  

    @Override
    public String toString() {
        return city + ", " + country;
    }
    
    
}
