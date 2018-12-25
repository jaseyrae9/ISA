export class SignUpInfo {

  username: string;
  email: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: string;
  password: string;
  matchingPassword: string;


  constructor(username: string, email: string, firstName: string, lastName: string, address: string,
    phoneNumber: string, password: string, matchingPassword: string) {

      this.username = username;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
      this.phoneNumber = phoneNumber;
      this.password = password;
      this.matchingPassword = matchingPassword;
  }
}
