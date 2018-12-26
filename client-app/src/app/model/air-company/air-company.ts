export class AirCompany {
    id: number;
    name: string;
    description: string;

    constructor(id: number, name: string, address: string, description: string, lng: number, lat: number) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
