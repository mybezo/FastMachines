package net.guizhanss.fastmachines.implementation.items.machines.bedrock

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.loaders.NetworksExpansionWorkbenchRecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BedrockFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class FastExpansionWorkbenchBedrock(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BedrockFastMachine(itemGroup, itemStack, recipeType, recipe) {

    override val craftItemMaterial: Material
        get() = Material.STRIPPED_BAMBOO_BLOCK

    override val recipeLoader: RecipeLoader
        get() = NetworksExpansionWorkbenchRecipeLoader(this)

    override fun registerPrecondition() = FastMachines.integrationService.networksEnabled
}
