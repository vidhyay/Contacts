package example.com.contacts;

public class Address {

    public String mDesignation;
    public String mFirstName;
    public String mLastName;
    public String mAddress;
    public String mProvince;
    public String mCountry;
    public String mPostalCode;

    public Address(String designation, String firstName, String lastName, String address, String province, String country, String postalCode) {
        mDesignation = designation;
        mFirstName = firstName;
        mLastName = lastName;
        mAddress = address;
        mProvince = province;
        mCountry = country;
        mPostalCode = postalCode;
    }

    public Boolean isEmpty() {
        if(mDesignation.isEmpty() ||
                mFirstName.isEmpty() ||
                mLastName.isEmpty() ||
                mAddress.isEmpty() ||
                mProvince.isEmpty() ||
                mCountry.isEmpty() ||
                mPostalCode.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String[] getInformation() {
        String[] adr = {mDesignation,mFirstName,mLastName,mAddress,mProvince,mCountry,mPostalCode};
        return adr;
    }
}
