package net.guizhanss.fastmachines.core.items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.Test
import kotlin.test.assertEquals

class ItemWrapperTest {

    @BeforeEach
    fun beforeEach() {
        MockBukkit.mock()
    }

    @AfterEach
    fun afterEach() {
        MockBukkit.unmock()
    }

    @Test
    fun testConstructor() {
        val wrapper1 = ItemWrapper.of(ItemStack(Material.DIAMOND, 32))

        assertEquals(1, wrapper1.baseItem.amount)
    }

    @Test
    fun testEquals() {
        val wrapper1 = ItemWrapper.of(ItemStack(Material.DIAMOND, 32))
        val wrapper2 = ItemWrapper.of(ItemStack(Material.DIAMOND, 32))

        assertEquals(wrapper1, wrapper2)

        val wrapper3 = ItemWrapper.of(ItemStack(Material.GOLD_INGOT, 32))
        val wrapper4 = ItemWrapper.of(ItemStack(Material.GOLD_INGOT, 16))

        assertEquals(wrapper3, wrapper4)
    }

    @Test
    fun testHashIgnoresMeta() {
        // same item with differently serialized meta must still hash the same
        val item1 = ItemStack(Material.DIAMOND).apply {
            editMeta { it.lore(listOf(Component.text("Crafting material"))) }
        }
        val item2 = ItemStack(Material.DIAMOND).apply {
            editMeta {
                it.lore(
                    listOf(
                        Component.text("Crafting material")
                            .decoration(TextDecoration.BOLD, false)
                            .decoration(TextDecoration.UNDERLINED, false)
                    )
                )
            }
        }

        assertEquals(ItemWrapper.of(item1), ItemWrapper.of(item2))
        assertEquals(ItemWrapper.of(item1).hashCode(), ItemWrapper.of(item2).hashCode())
        assertEquals(ItemWrapper.of(Material.DIAMOND).hashCode(), ItemWrapper.of(item1).hashCode())
    }
}
