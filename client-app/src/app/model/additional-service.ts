export class AdditionalService {
    id: number;
    name: string;
    description: string;
    price: number;
    active: boolean;
    isFast: boolean;

    constructor(id?: number, name?: string, description?: string, price?: number, isFast?: boolean) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = true;
        this.isFast = isFast;
    }
}
