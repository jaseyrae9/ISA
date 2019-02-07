export class User {
    password: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    address: string;
    lengthTravelled: number;

    constructor(password?: string, firstName?: string, lastName?: string, email?: string,
      phoneNumber?: string, address?: string, lengthTravelled?: number) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.lengthTravelled = lengthTravelled;
    }
}
