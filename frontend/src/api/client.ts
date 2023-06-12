import {reactive} from "vue"

export interface Player {
    id: number,
}

export interface Flip {
    id: number,
    heads?: Player,
    tails?: Player,
    amount: string
}

export function syncFlips(): Readonly<Flip[]> {
    const flips = reactive<Flip[]>([])

    flips.push({
        id: 9201,
        heads: {
            id: 2431,
        },
        amount: '$200.00',
    }, {
        id: 2324,
        tails: {
            id: 123,
        },
        amount: '$50.00',
    }, {
        id: 235535,
        heads: {
            id: 856,
        },
        tails: {
            id: 2431,
        },
        amount: '$6.00',
    })

    // setInterval(() => flips.push({}), 300)

    return flips
}

