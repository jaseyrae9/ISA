export class Car {
    id: number;
    brand: string;
    model: string;
    seatsNumber: number;
    doorsNumber: number;
    yearOfProduction: number;
    price: number;

    constructor(id: number, brand : string, model : string, seatsNumber: number, doorsNumber: number, yearOfProduction: number, price: number){
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.seatsNumber = seatsNumber;
        this.doorsNumber = doorsNumber;
        this.yearOfProduction = yearOfProduction;
        this.price = price;
    }
}