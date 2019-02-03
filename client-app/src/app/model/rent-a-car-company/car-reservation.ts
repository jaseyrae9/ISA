import { BranchOffice } from './branch-offfice';
import { Car } from './car';

export class CarReservation {
    id: number;
    pickUpDate: Date;
    dropOffDate: Date;
    pickUpBranchOffice: BranchOffice = new BranchOffice();
    dropOffBranchOffice: BranchOffice = new BranchOffice();
    car: Car = new Car();
    isCarRated: Boolean;
    isCompanyRated: Boolean;
}
