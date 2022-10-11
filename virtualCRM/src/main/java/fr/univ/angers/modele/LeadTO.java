package fr.univ.angers.virtualCRM.modele;

import java.util.Calendar;

public class LeadTO {
    private String firstName;
    private String lastName;
    private double annualRevenue;
    private String phone;
    private String street;
    private String postalCode;
    private String city;
    private String country;
    private Calendar creationDate;
    private GeographicPointTO geographicPointTO;
    private String company;
    private String state;

    /*
        Getters and Setters
    */
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAnnualRevenue() {
        return annualRevenue;
    }
    public void setAnnualRevenue(double annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public GeographicPointTO getGeographicPointTO() {
        return geographicPointTO;
    }
    public void setGeographicPointTO(GeographicPointTO geographicPointTO) {
        this.geographicPointTO = geographicPointTO;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
