export class AirCompany {
    id: number;
    name: string;
    description: string;

    constructor(id?: number, name?: string, description?: string) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    copy(airCompany: AirCompany): AirCompany {
      if (airCompany === undefined) {
        return new AirCompany();
      }
      const ret = new AirCompany(airCompany.id, airCompany.name, airCompany.description);
      return ret;
    }
}
