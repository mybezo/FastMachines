package net.guizhanss.fastmachines.core.recipes.loaders

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.choices.ExactChoice
import net.guizhanss.fastmachines.core.recipes.raw.RawRecipe
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import net.guizhanss.fastmachines.utils.items.countItems
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

/**
 * A [RecipeLoader] that loads recipes from Networks Expansion's `NTW_EXPANSION_STORAGE_UPGRADE_TABLE`.
 *
 * Like Quantum Workbench, this item keeps its own private `Map<ItemStack[], ItemStack>` field named
 * `recipes` — but unlike Quantum Workbench, this field is an INSTANCE field (not static), populated
 * inside the item's own constructor. We use reflection to read it off the live [SlimefunItem]
 * instance instead of the class itself.
 */
class NetworksStorageUpgradeTableRecipeLoader(
    machine: BaseFastMachine,
    private val id: String = "NTW_EXPANSION_STORAGE_UPGRADE_TABLE",
    private val fieldName: String = "recipes",
    enableRandomRecipes: Boolean = false,
) : RecipeLoader(machine, enableRandomRecipes) {

    @Suppress("UNCHECKED_CAST")
    override fun beforeLoad() {
        val recipesMap: Map<Array<ItemStack?>, ItemStack> = try {
            val sfItem = SlimefunItem.getById(id) ?: error("The item $id was not found.")
            val field = sfItem.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true

            field.get(sfItem) as Map<Array<ItemStack?>, ItemStack>
        } catch (e: Exception) {
            FastMachines.log(
                Level.SEVERE,
                e,
                "An error occurred while loading Networks Expansion Storage Upgrade Table recipes via reflection. " +
                    "The class or field name may have changed in your version of Networks.",
            )
            return
        }

        FastMachines.debug("Found ${recipesMap.size} raw Storage Upgrade Table recipes via reflection.")

        for ((input, output) in recipesMap) {
            val inputChoices = input.toList().countItems().map { (item, amount) -> ExactChoice(item, amount) }

            if (inputChoices.isEmpty()) {
                FastMachines.debug("  - Skipping recipe with no valid inputs: $input -> $output")
                continue
            }

            rawRecipes.add(RawRecipe(inputChoices, listOf(output)))
        }
    }
}
