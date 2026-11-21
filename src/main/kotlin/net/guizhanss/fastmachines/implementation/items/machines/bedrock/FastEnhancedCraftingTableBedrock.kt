package net.guizhanss.fastmachines.implementation.items.machines.bedrock

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.SlimefunMultiblockRecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BedrockFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Bedrock-friendly version of the Fast Enhanced Crafting Table.
 * Uses fixed-amount craft buttons instead of shift-clicking to craft.
 */
class FastEnhancedCraftingTableBedrock(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BedrockFastMachine(itemGroup, itemStack, recipeType, recipe) {

    override val craftItemMaterial: Material
        get() = Material.CRAFTING_TABLE

    override val recipeLoader: RecipeLoader
        get() = SlimefunMultiblockRecipeLoader(this, SlimefunItems.ENHANCED_CRAFTING_TABLE.itemId)
}
