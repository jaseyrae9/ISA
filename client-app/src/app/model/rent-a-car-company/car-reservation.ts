import { BranchOffice } from './branch-offfice';

export class CarReservation {
    id: number;
    pickUpDate: Date;
    dropOffDate: Date;
    pickUpBranchOffice: BranchOffice;
    dropOffBranchOffice: BranchOffice;
    // active?
}
