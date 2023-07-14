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

type Nullable<T> = T|null

export function syncFlips(): Readonly<Nullable<Flip>[]> {
    const flips = reactive<Nullable<Flip>[]>([
        null, null, null, null, null, null, null, null, null, null, null, null
    ])

    sync(flips)
        .finally(() => console.log("done syncing"))

    setInterval(() => clean(flips), 1000)

    return flips
}

function clean(flips: Nullable<Flip>[]) {
    const now= new Date().getTime()
    for (const [index, flip] of flips.entries()) {
        if (flip !== null && flip.flipped) {
            const flipDate = new Date(flip.flipped)
            // Coin was flipped 15 seconds ago, remove
            if (now - flipDate.getTime() > 15_000) {
                flips[index] = null
            }
        }
    }
}

async function sync(flips: Nullable<Flip>[]) {
    // Everytime read function yield
    for await (const newFlips of read()) {

        // For every flip server thought was active
        serverFlips: for (const newFlip of newFlips) {

            // Check for any updates in state and replace
            clientFlips: for (const [index, flip] of flips.entries()) {

                if (flip !== null) {
                    const {
                        id,
                        flipped,
                        playerTails,
                        playerHeads
                    } = flip

                    if (id === newFlip.id) {
                        // If there are differences between local and remote, replace local
                        if (JSON.stringify([flipped, playerHeads, playerTails])
                            !== JSON.stringify([newFlip.flipped, newFlip.playerHeads, newFlip.playerTails])) {
                            flips[index] = newFlip
                        }

                        // Flip with matching ID exists locally, no need to add to board
                        continue serverFlips
                    }
                }

            }

            // Flip was not merged with local state, meaning it should be considered new if not flipped yet
            // Add it to the first non-null position on the board
            if (newFlip.flipped === null) {
                const index = flips.indexOf(null)
                if (index >= 0) {
                    flips[index] = newFlip
                    console.debug('added flip to board', newFlip)
                } else {
                    console.debug('no more space on board', newFlip)
                }
            }
        }
    }
}

async function* read(): AsyncGenerator<Flip[]> {
    yield await (await fetch('/api/rounds'))
        .json() as Flip[]

    await new Promise((resolve) => setTimeout(resolve, 1000))

    yield* read()
}

export async function create(
    player: Player,
    outcome: Outcome,
    amount: string,
): Promise<Flip> {
    return await (await fetch('/api/rounds', {
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
    await fetch('/api/rounds/join', {
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