package net.guizhanss.fastmachines.core.recipes.loaders

import net.guizhanss.fastmachines.FastMachines
import net.guizhanss.fastmachines.core.recipes.choices.ExactChoice
import net.guizhanss.fastmachines.core.recipes.raw.RawRecipe
import net.guizhanss.fastmachines.implementation.items.machines.base.BaseFastMachine
import net.guizhanss.fastmachines.utils.items.countItems
import org.bukkit.inventory.ItemStack
import java.util.logging.Level

/**
 * A [RecipeLoader] that loads recipes for Networks Expansion's `NTW_EXPANSION_WORKBENCH`.
 *
 * IMPORTANT: this machine does NOT have a dedicated `RecipeType` of its own. Most items craftable
 * here (and hundreds of unrelated items from other addons) simply use `RecipeType.NULL` as a
 * generic placeholder, so filtering by recipe type is unreliable (it previously matched 1188
 * completely unrelated items). Instead, we call this fork's own public helper method
 * `com.balugaq.netex.api.helpers.SupportedExpansionWorkbenchRecipes#getRecipes()` via reflection
 * (it's a public static method, no field-hacking needed), which returns the curated
 * `Map<ItemStack[], ItemStack>` of items genuinely craftable at this specific workbench — same
 * shape as Quantum Workbench's recipe map.
 */
class NetworksExpansionWorkbenchRecipeLoader(
    machine: BaseFastMachine,
    private val className: String = "com.balugaq.netex.api.helpers.SupportedExpansionWorkbenchRecipes",
    private val methodName: String = "getRecipes",
    enableRandomRecipes: Boolean = false,
) : RecipeLoader(machine, enableRandomRecipes) {

    @Suppress("UNCHECKED_CAST")
    override fun beforeLoad() {
        val recipesMap: Map<Array<ItemStack?>, ItemStack> = try {
            val clazz = Class.forName(className)
            val method = clazz.getMethod(methodName)
            method.invoke(null) as Map<Array<ItemStack?>, ItemStack>
        } catch (e: Exception) {
            FastMachines.log(
                Level.SEVERE,
                e,
                "An error occurred while loading Networks Expansion Workbench recipes. " +
                    "The helper class/method may not exist in your version of Networks " +
                    "(it may be an older version without com.balugaq.netex support).",
            )
            return
        }

        FastMachines.debug("Found ${recipesMap.size} raw Expansion Workbench recipes.")

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
