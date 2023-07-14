<script setup lang="ts">
import CoinFlip from "@/components/CoinFlip.vue"
import {syncFlips} from "@/api/client"

const flips = syncFlips()
</script>

<template>
  <div class="board">
    <template
        v-for="(flip, n) in flips"
        :key="`t-${flip?.id ?? '-'}:${n}`"
    >
      <div
          v-if="flip === null"
          class="slot"
          :key="`t--${n}`"
      />
      <CoinFlip
          v-else
          :flip="flip"
          :key="`t-${flip.id}:${n}`"
      />
    </template>
  </div>
</template>

<style scoped>
.board {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  grid-auto-rows: 180px;
  gap: var(--px-md);

  .slot {
    border-radius: var(--px-md);
    background-color: var(--c-primary-500);
  }
}

@media (min-width: 768px) {
  .board {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (min-width: 1440px) {
  .board {
    grid-template-columns: repeat(4, 1fr);
  }
}
</style>