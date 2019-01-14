package sample;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class Vacancy {
    private String name;
    private String organization;
    private String URLOrganization;
    private Long salaryFrom;
    private Long salaryTo;
    private Boolean gross; //если True до вычетов налогов иначе false
    private String schedule;
    private String employment;
    private String experience;
    private String description;
    private ArrayList<String> key_skills;
    private LinkedHashSet<String> specializations;
    private String email;
    private String city;
    private String street;
    private String building;
    private double lat;
    private double lon;

    public Vacancy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getURLOrganization() {
        return URLOrganization;
    }

    public void setURLOrganization(String URLOrganization) {
        this.URLOrganization = URLOrganization;
    }

    public Long getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Long salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Long getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Long salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Boolean getGross() {
        return gross;
    }

    public void setGross(Boolean gross) {
        this.gross = gross;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getKey_skills() {
        return key_skills;
    }

    public void setKey_skills(ArrayList<String> key_skills) {
        this.key_skills = key_skills;
    }

    public LinkedHashSet<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(LinkedHashSet<String> specializations) {
        this.specializations = specializations;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        if (city == null) {
            city = "";
        }
        return city;
    }

    public void setCity(String city) {
        if(city != null) {
            if (city.endsWith("ня Румянцево")) {
                this.city = "г. Москва, " + city;
            } else {
                this.city = city;
            }
        }
    }

    public String getStreet() {
        if (street == null) {
            street = "";
        }
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        if (building == null) {
            building = "";
        }
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
