package net.guizhanss.fastmachines.implementation.items.machines.networks

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.loaders.NetworksExpansionWorkbenchRecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class FastExpansionWorkbench(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BaseFastMachine(itemGroup, itemStack, recipeType, recipe, 1024, 8) {

    override val craftItemMaterial: Material
        get() = Material.STRIPPED_BAMBOO_BLOCK

    override val recipeLoader: RecipeLoader
        get() = NetworksExpansionWorkbenchRecipeLoader(this)

    override fun registerPrecondition() = FastMachines.integrationService.networksEnabled
}
