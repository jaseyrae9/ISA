export class User {
    password: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    address: string;

    constructor(password: string, firstName: string, lastName: string, email: string, phoneNumber: string, address: string)
    {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
