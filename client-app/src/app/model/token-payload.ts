import { Role } from './role';

export class TokenPayload {
    sub: string;
    roles: Role[];
    audience: string;
    created: number;
    exp: number;
    companyId: number;

    constructor(sub: string, roles: Role[], audience: string, created: number, exp: number, companyId: number) {
        this.sub = sub;
        this.roles = roles;
        this.audience = audience;
        this.created = created;
        this.exp = exp;
        this.companyId = companyId;
    }
}
