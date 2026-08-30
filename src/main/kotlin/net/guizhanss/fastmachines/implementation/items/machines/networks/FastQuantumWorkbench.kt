package net.guizhanss.fastmachines.implementation.items.machines.networks

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.loaders.NetworksQuantumWorkbenchRecipeLoader
import net.guizhanss.fastmachines.core.recipes.loaders.RecipeLoader
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class FastQuantumWorkbench(
    itemGroup: ItemGroup,
    itemStack: SlimefunItemStack,
    recipeType: RecipeType,
    recipe: Array<out ItemStack?>,
) : BaseFastMachine(itemGroup, itemStack, recipeType, recipe, 1024, 8) {

    override val craftItemMaterial: Material
        get() = Material.SMITHING_TABLE

    override val recipeLoader: RecipeLoader
        get() = NetworksQuantumWorkbenchRecipeLoader(this)

    override fun registerPrecondition() = FastMachines.integrationService.networksEnabled
}
