package net.guizhanss.fastmachines.core.items

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun
import io.github.thebusybiscuit.slimefun4.utils.itemstack.ItemStackWrapper
import net.guizhanss.fastmachines.utils.items.isSimilarTo
import net.guizhanss.guizhanlib.kt.slimefun.extensions.getSlimefunItem
import net.guizhanss.guizhanlib.kt.slimefun.extensions.isSlimefunItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * A wrapper for [ItemStack] that only focus on the item type and meta.
 */
@ConsistentCopyVisibility
data class ItemWrapper private constructor(
    val baseItem: ItemStack
) : Comparable<ItemWrapper> {

    companion object {

        /**
         * Creates a new [ItemWrapper] with the given [ItemStack].
         */
        fun of(item: ItemStack): ItemWrapper {
            val baseItem = if (item is ItemStackWrapper) {
                // Slimefun's ItemStackWrapper, construct a new ItemStack
                ItemStack(item.type).apply {
                    if (item.hasItemMeta()) {
                        itemMeta = item.itemMeta
                    }
                }
            } else {
                item.clone().apply { amount = 1 }
            }

            return ItemWrapper(baseItem)
        }

        /**
         * Creates a new [ItemWrapper] with the given [Material].
         */
        fun of(material: Material): ItemWrapper {
            val baseItem = ItemStack(material)

            return ItemWrapper(baseItem)
        }

        /**
         * Calculates the hash from the stable identity of the item only: the material,
         * plus the Slimefun item ID for Slimefun items.
         *
         * [ItemMeta.hashCode] must not be used here: it depends on how the name/lore components
         * are serialized, so two copies of the same item (e.g. one that went through a Networks
         * grid/drawer/quantum storage) can have different meta hashes while still being
         * [isSimilarTo] each other, which breaks the hashCode/equals contract.
         *
         * Items that are equal always have the same Slimefun ID (or both have none),
         * since [isSimilarTo] requires their PersistentDataContainers to be equal.
         */
        private fun ItemStack.calcHash(): Int {
            val typeHash = type.hashCode()
            if (type.isAir || !hasItemMeta()) return typeHash

            val sfId = try {
                Slimefun.getItemDataService().getItemData(itemMeta).orElse(null)
            } catch (_: Exception) {
                // Slimefun is not available (e.g. in unit tests)
                null
            } catch (_: LinkageError) {
                null
            } ?: return typeHash

            return 31 * typeHash + sfId.hashCode()
        }
    }

    // cache ItemMeta and hashCode
    val baseItemMeta = if (baseItem.hasItemMeta()) baseItem.itemMeta else null
    private val itemHash = baseItem.calcHash()

    override fun equals(other: Any?) = other is ItemWrapper && isSimilarTo(other.baseItem)

    override fun hashCode() = itemHash

    override fun compareTo(other: ItemWrapper): Int {
        // quick compare hash code, items with different hash code are likely different
        if (itemHash != other.itemHash) return itemHash - other.itemHash

        // same hash and amount, compare item
        if (!isSimilarTo(other.baseItem)) return baseItem.hashCode() - other.baseItem.hashCode()

        // should be same
        return 0
    }

    override fun toString() = if (baseItem.isSlimefunItem()) {
        "ItemWrapper(slimefunId=${baseItem.getSlimefunItem().id})"
    } else {
        "ItemWrapper(type=${baseItem.type}${if (baseItemMeta != null) ", meta=${baseItemMeta}" else ""})"
    }

}
