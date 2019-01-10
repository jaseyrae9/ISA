export class Room {
    id: number; 
    floor: number;
    roomNumber: number;
    numberOfBeds: number;
    price: number;
    type: string;

    constructor(id?: number, floor?: number, roomNumber?: number, numberOfBeds?: number, price?: number, type?: string)
    {
        this.id = id;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
        this.type = type;
    }
}