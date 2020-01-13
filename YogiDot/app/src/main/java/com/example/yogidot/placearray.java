package com.example.yogidot;

public class placearray {
    private String address;
    private String placeid;

    public placearray(String _address, String _placeid){
        address=_address;
        placeid=_placeid;
    }
    public String getAddress() {return address;}
    public String getPlaceid() {return placeid;}
}
