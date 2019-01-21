export class AdditionalService {
    id: number;
    name: string;
    description: string;
    price: number;
    active: boolean;

    constructor(id?: number, name?: string, description?: string, price?: number) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = true;
    }
}
