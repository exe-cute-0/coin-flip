<script setup lang="ts">
import type {Outcome, Player} from "@/api/client";

const { player } = defineProps<{
  player: Player,
  selection: Outcome
}>()
</script>

<template>
  <div>
    <svg :class="{
      'head': selection === 'HEADS',
      'tail': selection === 'TAILS',
    }" viewBox="0 0 56 56">
      <circle r="24" cx="28" cy="28" />
    </svg>
    <span>{{ player }}</span>
  </div>
</template>

<style scoped>
div {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--px-sm)
}
svg {
  stroke-width: var(--px-mn);
  fill: none;
  width: 56px;
  height: 56px;

  &.head {
    stroke: var(--c-primary-300);
  }
  &.tail {
    stroke: var(--c-secondary-300);
  }
}
span {
  font-size: var(--tx-sm);
  font-weight: 700;
  color: var(--c-neutral-200)
}
</style>