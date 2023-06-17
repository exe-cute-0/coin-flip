<script setup lang="ts">
import Avatar from "@/components/Avatar.vue"
import type {Flip} from "@/api/client";
import {join} from "@/api/client";

const player = localStorage.getItem('player') as string

function joinTails() {
  join(flip, player, "TAILS")
      .catch(() => {
      })
}

function joinHeads() {
  join(flip, player, "HEADS")
      .catch(() => {
      })
}

const {flip} = defineProps<{
  flip: Flip,
}>()
</script>

<template>
  <div class="flip">
    <div
        class="side head"
        :class="{
          'winner': flip.outcome === 'HEADS',
          'loser': flip.outcome === 'TAILS',
        }"
    >
      <span class="selection">Heads</span>
      <Avatar
          v-if="flip.playerHeads !== null"
          :player="flip.playerHeads"
          :selection="'HEADS'"
      />
      <div
          v-else
          class="join"
      >
        <button @click="joinHeads">Join</button>
      </div>
      <span class="amount">{{ flip.amount }}</span>
    </div>
    <div class="vs">
      <div
          v-if="flip.outcome !== null"
          class="toss"
          :class="{
            'to-heads' : flip.outcome === 'HEADS',
            'to-tails' : flip.outcome === 'TAILS',
          }"
      >
        <div class="coin">
          <div class="head">
            <span>HEADS</span>
          </div>
          <div class="tail">
            <span>TAILS</span>
          </div>
        </div>
      </div>
      <span v-else>VS</span>
    </div>
    <div
        class="side tail"
        :class="{
          'winner': flip.outcome === 'TAILS',
          'loser': flip.outcome === 'HEADS',
        }"
    >
      <span class="selection">Tails</span>
      <Avatar
          v-if="flip.playerTails !== null"
          :player="flip.playerTails"
          :selection="'TAILS'"
      />
      <div
          v-else
          class="join"
      >
        <button @click="joinTails" class="secondary">Join</button>
      </div>
      <span class="amount">{{ flip.amount }}</span>
    </div>
  </div>
</template>

<style scoped lang="scss">
.flip {
  display: flex;
  justify-content: space-between;
  padding: var(--px-lg);
  border: var(--px-mn) solid var(--c-secondary-300);
  border-radius: var(--px-md);
  box-sizing: border-box;
}

.side {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-basis: 30%;
  justify-content: space-between;
  opacity: 1;
  transition: 0.6s opacity 5.5s ease-out;

  .selection {
    font-size: var(--tx-sm);
    font-weight: 700;
    text-transform: uppercase;
  }

  &.head .selection {
    color: var(--c-primary-200);
  }

  &.tail .selection {
    color: var(--c-secondary-200);
  }

  .amount {
    padding: var(--px-sm) var(--px-md);
    border-radius: var(--px-sm);
    background-color: var(--c-primary-500)
  }
}

.vs {
  display: flex;
  align-items: center;
  perspective: 900px;

  > span {
    font-weight: 700;
    font-size: var(--tx-xl);
    padding: var(--px-sm) var(--px-md);
    border-radius: var(--px-sm);
    background-color: var(--c-primary-500)
  }
}

.toss {
  transform-style: preserve-3d;

  &.to-tails {
    animation: 1s cubic-bezier(0.24, 0.63, 0, 1) 3s 2 alternate toss;

    .coin {
      animation: 1.9s linear 3s 1 forwards spin-to-tails;
    }
  }

  &.to-heads {
    animation: 1s cubic-bezier(0.24, 0.63, 0, 1) 3s 2 alternate toss;

    .coin {
      animation: 1.9s linear 3s 1 forwards spin-to-heads;
    }
  }
}

.coin {
  width: 80px;
  height: 80px;
  position: relative;
  transform-style: preserve-3d;

  .head, .tail {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    font-weight: 700;
    display: flex;
    justify-content: center;
    align-items: center;
    position: absolute;
    top: 0;
    left: 0;
    box-sizing: border-box;
    backface-visibility: hidden;
  }

  .head {
    background-color: var(--c-primary-500);
    border: var(--px-mn) solid var(--c-primary-200);
    color: var(--c-primary-100);
  }

  .tail {
    background-color: var(--c-secondary-500);
    border: var(--px-mn) solid var(--c-secondary-200);
    color: var(--c-secondary-100);
    transform: rotateX(180deg) translateZ(0.001px);
  }
}

@keyframes toss {
  from {
    transform: translateZ(0);
  }
  to {
    transform: translateZ(440px);
  }
}

@keyframes spin-to-heads {
  from {
    transform: rotateX(0deg);
  }
  to {
    transform: rotateX(1440deg);
  }
}

@keyframes spin-to-tails {
  from {
    transform: rotateX(0deg);
  }
  to {
    transform: rotateX(1620deg);
  }
}
</style>