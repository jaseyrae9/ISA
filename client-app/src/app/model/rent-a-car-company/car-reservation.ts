import { BranchOffice } from './branch-offfice';

export class CarReservation {
    id: number;
    pickUpDate: Date;
    dropOffDate: Date;
    pickUpBranchOffice: BranchOffice = new BranchOffice();
    dropOffBranchOffice: BranchOffice = new BranchOffice();
    // active?
}
