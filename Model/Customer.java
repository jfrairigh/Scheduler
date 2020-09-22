
package Model;

public class Customer {
    
    private final String custName, street, zip, phone, city, country;
    private final int custId, addId, cityId;

    public Customer(int custId, int addId, int cityId, String custName, String street, String zip, String phone, String city, String country) {
        this.custName = custName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.country = country;
        this.custId = custId;
        this.addId = addId;
        this.cityId = cityId;
    }
    
    public int getCustId(){
        return custId;
    }
    
    public String getCustName() {
        return custName;
    }

    public int getAddId() {
        return addId;
    }
    
    public String getStreet() {
        return street;
    }

    public int getCityId() {
        return cityId;
    }
    
    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return custName;
    }
    
    
}

    
