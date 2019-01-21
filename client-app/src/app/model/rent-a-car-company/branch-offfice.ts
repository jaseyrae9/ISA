export class BranchOffice {
    id: number;
    name: string;
    active: boolean;

    constructor(id?: number, name?: string, active?: boolean) {
        this.id = id;
        this.name = name;
        this.active = active;
    }
}
