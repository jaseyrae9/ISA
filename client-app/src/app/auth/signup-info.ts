export class SignUpInfo {

  username: string;
  email: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: string;
  password: string;
  matchingPassword: string;


  constructor(email: string, firstName: string, lastName: string, address: string,
    phoneNumber: string, password: string) {
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.phoneNumber = phoneNumber;
      this.password = password;
  }
}
