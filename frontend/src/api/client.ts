import {reactive} from "vue"

export type Outcome = 'HEADS' | 'TAILS'
export type Player = string

export interface Flip {
    id: number
    created: string
    amount: string
    flipped: string|null
    outcome: Outcome|null
    playerHeads: Player|null
    playerTails: Player|null
}

export function syncFlips(): Readonly<Flip[]> {
    const flips = reactive<Flip[]>([])

    sync(flips)
        .finally(() => console.log("done syncing"))

    setInterval(() => clean(flips), 1000)

    return flips
}

function clean(flips: Flip[]) {
    const now= new Date().getTime()
    for (const [index, {
        flipped
    }] of flips.entries()) {
        if (flipped) {
            const flipDate = new Date(flipped)
            // Coin was flipped 15 seconds ago, remove
            if (now - flipDate.getTime() > 15_000) {
                flips.splice(index, 1)
            }
        }
    }
}

async function sync(flips: Flip[]) {
    for await (const newFlips of read()) {
        a: for (const newFlip of newFlips) {
            for (const [index, {
                id,
                flipped,
                playerTails,
                playerHeads
            }] of flips.entries()) {
                if (id === newFlip.id) {
                    // If there are differences between local and remote, replace local
                    if (JSON.stringify([flipped, playerHeads, playerTails])
                        !== JSON.stringify([newFlip.flipped, newFlip.playerHeads, newFlip.playerTails])) {
                        flips[index] = newFlip
                    }

                    // Flip with matching ID exists locally, no need to append
                    continue a
                }
            }

            if (newFlip.flipped === null) {
                flips.push(newFlip)
            }
        }
    }
}

async function* read(): AsyncGenerator<Flip[]> {
    yield await (await fetch('http://localhost:8080/rounds'))
        .json() as Flip[]

    await new Promise((resolve) => setTimeout(resolve, 1000))

    yield* read()
}

export async function create(
    player: Player,
    outcome: Outcome,
    amount: string,
): Promise<Flip> {
    return await (await fetch('http://localhost:8080/rounds', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            player: player,
            amount: amount,
            outcome: outcome,
        })
    })).json() as Flip
}

export async function join(
    flip: Flip,
    player: Player,
    outcome: Outcome,
): Promise<void> {
    await fetch('http://localhost:8080/rounds/join', {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            roundId: flip.id,
            player: player,
            outcome: outcome,
        })
    })
}