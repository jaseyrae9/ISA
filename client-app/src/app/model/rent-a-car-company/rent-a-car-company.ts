import { Car } from 'src/app/model/rent-a-car-company/car';
export class RentACarCompany {
    id: number;
    name: string;
    description: string;
    cars: Car[];

    constructor(id?: number, name?: string, description?: string, cars?: Car[]) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cars = cars;
    }
}
