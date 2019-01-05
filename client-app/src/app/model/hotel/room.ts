export class Room {
    id: number; 
    floor: number;
    roomNumber: number;
    numberOfBeds: number;
    price: number;

    constructor(id: number, floor: number, roomNumber: number, numberOfBeds: number, price: number)
    {
        this.id = id;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
    }
}