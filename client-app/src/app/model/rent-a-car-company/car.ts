export class Car {
    id: number;
    brand: string;
    model: string;
    type: string;
    seatsNumber: number;
    doorsNumber: number;
    yearOfProduction: number;
    price: number;
    active: boolean;

    constructor(id?: number, type?: string, brand?: string, model?: string,
        seatsNumber?: number, doorsNumber?: number, yearOfProduction?: number,
        price?: number, active?: boolean) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.seatsNumber = seatsNumber;
        this.doorsNumber = doorsNumber;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
        this.type = type;
    }
}
