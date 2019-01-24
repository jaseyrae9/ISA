import { CarReservation } from './car-reservation';

export class Car {
    id: number;
    brand: string;
    model: string;
    type: string;
    seatsNumber: number;
    doorsNumber: number;
    yearOfProduction: number;
    price: number;
    carReservations: CarReservation[] = [];
}
