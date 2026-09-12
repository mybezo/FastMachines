package net.guizhanss.fastmachines.implementation.items.machines.base

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.guizhanlib.kt.minecraft.items.edit
import org.bukkit.inventory.ItemStack

/**
 * A fast machine variant for Bedrock (Geyser) players.
 *
 * The normal [BasicFastMachine] relies on shift-clicking the craft slot to bulk-craft
 * (1 / 16 / 64 / max depending on click modifiers), which Bedrock players cannot do
 * reliably through Geyser. This variant instead exposes a fixed set of craft buttons,
 * one per craftable amount, at [CRAFT_BUTTON_SLOTS].
 */
abstract class BedrockFastMachine(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BasicFastMachine(itemGroup, itemStack, recipeType, recipe) {

    final override val craftSlots: Map<Int, Int?>
        get() = CRAFT_BUTTON_SLOTS

    final override val craftInputSlots: IntArray
        get() = BEDROCK_INPUT_SLOTS

    final override val craftOutputPushSlots: IntArray
        get() = BEDROCK_OUTPUT_PUSH_SLOTS

    final override val previewSlots: IntArray
        get() = BEDROCK_PREVIEW_SLOTS

    override fun craftButtonItem(amount: Int?): ItemStack {
        val n = amount ?: 1
        return craftItem.clone().edit {
            name("&aCraft x$n")
        }
    }

    companion object {

        // slot -> fixed craft amount
        val CRAFT_BUTTON_SLOTS: Map<Int, Int?> = linkedMapOf(
            8 to 1,
            17 to 8,
            26 to 16,
            35 to 32,
            45 to 64,
            53 to 128,
        )

        val BEDROCK_INPUT_SLOTS: IntArray =
            BaseFastMachine.INPUT_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()

        val BEDROCK_OUTPUT_PUSH_SLOTS: IntArray =
            BaseFastMachine.OUTPUT_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()

        val BEDROCK_PREVIEW_SLOTS: IntArray =
            BaseFastMachine.PREVIEW_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()
    }
}
