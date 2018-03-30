package model;

public class Contact {

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private Contact(Builder builder){
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNumber = builder.phoneNumber;
    }

    @Override
    public String toString() {
        return "["+firstName+" "+lastName + " - "+phoneNumber+"]";
    }

    //Builder <<--------------------------------------------------------------------------------------------------------
    public static class Builder {

        private String firstName;
        private String lastName;
        private String phoneNumber;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Contact build() {
            return new Contact(this);
        }
    }

}