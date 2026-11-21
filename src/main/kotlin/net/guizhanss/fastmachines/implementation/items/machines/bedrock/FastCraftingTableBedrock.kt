package net.guizhanss.fastmachines.implementation.items.machines.bedrock

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.fastmachines.core.recipes.loaders.MultipleLoader
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.VanillaRecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BedrockFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

class FastCraftingTableBedrock(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BedrockFastMachine(itemGroup, itemStack, recipeType, recipe) {

    override val craftItemMaterial: Material
        get() = Material.CRAFTING_TABLE

    override val recipeLoader: RecipeLoader
        get() = MultipleLoader(
            this,
            VanillaRecipeLoader(this, ShapedRecipe::class.java),
            VanillaRecipeLoader(this, ShapelessRecipe::class.java),
        )
}
