export class Friendship {
    from_id: number;
    to_id: number;
    from_firstname: string;
    from_lastname: string;
    to_firstname: string;
    to_lastname: string;
    active: boolean;

    public Friendship(from_firstname: string, from_lastname: string, to_firstname: string, to_lastname: string, active: boolean) {
        this.active = active;
        this.from_firstname = from_firstname;
        this.from_lastname = from_lastname;
        this.to_firstname = to_firstname;
        this.to_lastname = to_lastname;
    }
}

