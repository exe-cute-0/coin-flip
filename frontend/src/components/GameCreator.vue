<script setup lang="ts">
import {create} from "@/api/client";

const player = localStorage.getItem('player') as string

function newBetOnHeads() {
  create(player, "HEADS", "10.00")
}

function newBetOnTails() {
  create(player, "TAILS", "10.00")
}

const {} = defineProps<{

}>()
</script>

<template>
  <div class="creator">
    <span>Create new bet on</span>
    <div>
      <button @click="newBetOnHeads">Heads</button>
      <button @click="newBetOnTails" class="secondary">Tails</button>
    </div>
  </div>
</template>

<style scoped lang="scss">
.creator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--px-md);

  span {
    text-transform: uppercase;
    font-weight: 700;
  }

  div {
    display: flex;
    gap: var(--px-md);
  }
}
</style>