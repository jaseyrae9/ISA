export class Friendship {
    user1Id: number;
    user1Firstname: string;
    user1Lastname: string;
    user2Id = -1;
    user2Firstname: string;
    user2Lastname: string;
    status: number; // 0 - prijatelji, 1 - primljen zahtev, 2 - poslan zahtev, 3 - nista
}

