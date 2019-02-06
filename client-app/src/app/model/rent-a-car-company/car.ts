import { CarReservation } from './car-reservation';
import { RentACarCompany } from './rent-a-car-company';
import { BranchOffice } from './branch-offfice';

export class Car {
    id: number;
    brand: string;
    model: string;
    type: string;
    seatsNumber: number;
    doorsNumber: number;
    yearOfProduction: number;
    price: number;
    reservations: CarReservation[] = [];
    rentACarCompany: RentACarCompany;
    averageRating: number;
    isFast: Boolean;
    beginDate: Date;
    endDate: Date;
    fastPickUpBranchOffice: BranchOffice;
    fastDropOffBranchOffice: BranchOffice;
    discount: number;
}
