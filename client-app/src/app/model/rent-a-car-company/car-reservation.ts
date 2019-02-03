import { BranchOffice } from './branch-offfice';
import { Car } from './car';
import { RentACarCompany } from './rent-a-car-company';

export class CarReservation {
    id: number;
    pickUpDate: Date;
    dropOffDate: Date;
    pickUpBranchOffice: BranchOffice = new BranchOffice();
    dropOffBranchOffice: BranchOffice = new BranchOffice();
    car: Car = new Car();
    isCarRated: Boolean;
    isCompanyRated: Boolean;
    rentACarCompany: RentACarCompany;
}
