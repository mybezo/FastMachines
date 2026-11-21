package net.guizhanss.fastmachines.implementation.items.machines.base

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

/**
 * A Bedrock-friendly fast machine.
 *
 * Bedrock Edition players cannot reliably shift-click or right-click inside chest menus,
 * so instead of a single craft button whose amount depends on the click type,
 * this machine provides a column of craft buttons, each with a fixed craft amount.
 *
 * The right-most column of the menu is used by the craft buttons.
 */
abstract class BedrockFastMachine(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
    energyCapacity: Int = 1024,
    energyConsumption: Int = 8,
) : BaseFastMachine(itemGroup, itemStack, recipeType, recipe, energyCapacity, energyConsumption) {

    final override val craftSlots: Map<Int, Int?>
        get() = CRAFT_BUTTON_SLOTS

    final override val showEnergySlot: Boolean
        get() = false

    final override val craftInputSlots: IntArray
        get() = BEDROCK_INPUT_SLOTS

    final override val craftOutputPushSlots: IntArray
        get() = BEDROCK_OUTPUT_PUSH_SLOTS

    final override val previewSlots: IntArray
        get() = BEDROCK_PREVIEW_SLOTS

    final override fun craftButtonItem(amount: Int?): ItemStack {
        val n = amount ?: 1
        val cost = energyPerUse.toLong() * n
        val item = ItemStack(craftItemMaterial)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Craft x$n", NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, false)
        )
        meta.lore(
            listOf(
                Component.text("Click to craft the selected item.", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, false),
                Component.text("Amount: ", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, false)
                    .append(
                        Component.text("${n}x", NamedTextColor.WHITE)
                            .decoration(TextDecoration.ITALIC, false)
                            .decoration(TextDecoration.BOLD, false)
                    ),
                Component.text("Energy cost: ", NamedTextColor.GRAY)
                    .decoration(TextDecoration.ITALIC, false)
                    .decoration(TextDecoration.BOLD, false)
                    .append(
                        Component.text("${"%,d".format(cost)} J", NamedTextColor.AQUA)
                            .decoration(TextDecoration.ITALIC, false)
                            .decoration(TextDecoration.BOLD, false)
                    ),
            )
        )

        item.itemMeta = meta
        return item
    }

    companion object {

        // slot -> fixed craft amount
        val CRAFT_BUTTON_SLOTS: Map<Int, Int?> = linkedMapOf(
            8 to 1,
            17 to 8,
            26 to 16,
            35 to 32,
            44 to 64,
            53 to 128,
        )

        val BEDROCK_INPUT_SLOTS: IntArray =
            INPUT_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()

        val BEDROCK_OUTPUT_PUSH_SLOTS: IntArray =
            OUTPUT_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()

        val BEDROCK_PREVIEW_SLOTS: IntArray =
            PREVIEW_SLOTS.filter { it !in CRAFT_BUTTON_SLOTS.keys }.toIntArray()
    }
}
