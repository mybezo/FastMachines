package net.guizhanss.fastmachines.core.recipes.loaders

import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.choices.ExactChoice
import net.guizhanss.fastmachines.core.recipes.raw.RawRecipe
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import net.guizhanss.fastmachines.utils.items.countItems
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

/**
 * A [RecipeLoader] that loads recipes from Networks' `NetworkQuantumWorkbench`.
 *
 * Unlike most Slimefun items, `NetworkQuantumWorkbench` does NOT implement `RecipeDisplayItem`.
 * Its recipes are stored in a private static `Map<ItemStack[], ItemStack>` field named `RECIPES`,
 * populated through the item's custom `RecipeType` callback. We use reflection to read it directly,
 * the same way [InfinityExpansionRecipeLoader] reads InfinityExpansion's private `TYPE` field.
 */
class NetworksQuantumWorkbenchRecipeLoader(
    machine: BaseFastMachine,
    private val className: String = "io.github.sefiraat.networks.slimefun.network.NetworkQuantumWorkbench",
    private val fieldName: String = "RECIPES",
    enableRandomRecipes: Boolean = false,
) : RecipeLoader(machine, enableRandomRecipes) {

    @Suppress("UNCHECKED_CAST")
    override fun beforeLoad() {
        val recipesMap: Map<Array<ItemStack?>, ItemStack> = try {
            val clazz = Class.forName(className)
            val field = clazz.getDeclaredField(fieldName)
            field.isAccessible = true

            field.get(null) as Map<Array<ItemStack?>, ItemStack>
        } catch (e: Exception) {
            FastMachines.log(
                Level.SEVERE,
                e,
                "An error occurred while loading Networks Quantum Workbench recipes via reflection. " +
                    "The class or field name may have changed in your version of Networks.",
            )
            return
        }

        FastMachines.debug("Found ${recipesMap.size} raw Quantum Workbench recipes via reflection.")

        for ((input, output) in recipesMap) {
            val inputChoices = input.toList().countItems().map { (item, amount) -> ExactChoice(item, amount) }

            // skip malformed / empty recipes just in case
            if (inputChoices.isEmpty()) {
                FastMachines.debug("  - Skipping recipe with no valid inputs: $input -> $output")
                continue
            }

            rawRecipes.add(RawRecipe(inputChoices, listOf(output)))
        }
    }
}
