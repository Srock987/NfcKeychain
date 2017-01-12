package com.pawel.nfckeychain.CustomCreations;

/**
 * Created by Pawel on 2016-12-06.
 */

public class Guest {

    private byte id;
    private String name;
    private String key;

    public Guest(byte aId, String aName, String aKey){
        id=aId;
        name=aName;
        key=aKey;
    }

    public byte getId(){
        return id;
    }

    public void setId(byte aId){
        id = aId;
    }

    public String getName(){
        return name;
    }

    public String getKey(){
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guest guest = (Guest) o;

        if (id != guest.id) return false;
        if (name != null ? !name.equals(guest.name) : guest.name != null) return false;
        return key != null ? key.equals(guest.key) : guest.key == null;

    }
}
