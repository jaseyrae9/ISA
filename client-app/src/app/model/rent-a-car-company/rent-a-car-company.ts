import { Car } from 'src/app/model/rent-a-car-company/car';
import { BranchOffice } from './branch-offfice';
import { Location} from './../location';

export class RentACarCompany {
    id: number;
    name: string;
    description: string;
    cars: Car[];
    branchOffices: BranchOffice[];
    location: Location = new Location();
    // constructor(id?: number, name?: string, description?: string, cars?: Car[], branchOffices?: BranchOffice[]) {
    //     this.id = id;
    //     this.name = name;
    //     this.description = description;
    //     this.cars = cars;
    //     this.branchOffices = branchOffices;
    // }
}
