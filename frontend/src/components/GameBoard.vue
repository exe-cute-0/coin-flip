<script setup lang="ts">
import CoinFlip from "@/components/CoinFlip.vue"
import {syncFlips} from "@/api/client"

const flips = syncFlips()
</script>

<template>
  <div class="board">
    <template
        v-for="n in 12"
        :key="`${flips[n-1]?.id ?? '-'}:${n}`"
    >
      <div
          v-if="n-1 >= flips.length"
          class="slot"
      />
      <CoinFlip
          v-else
          :flip="flips[n-1]"
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
  padding: var(--px-xl);

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