export class Room {
    id: number;
    floor: number;
    roomNumber: number;
    numberOfBeds: number;
    price: number;
    type: string;
    active: boolean;

    constructor(id?: number, floor?: number, roomNumber?: number, numberOfBeds?: number, price?: number, type?: string, active?: boolean) {
        this.id = id;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
        this.type = type;
        this.active = active;
    }
}
