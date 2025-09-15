package com.martmists.compose.testmod

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.martmists.compose.openDisplayGui
import com.martmists.compose.layout.*
import com.martmists.compose.ui.*
import com.martmists.compose.unit.ticks
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.InteractionEntity
import net.minecraft.item.ItemDisplayContext
import net.minecraft.item.Items
import net.minecraft.server.command.ServerCommandSource
import org.joml.Matrix4f
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d
import java.lang.ref.WeakReference
import kotlin.math.PI

object ComposeTestMod : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, access, env ->
            dispatcher.register(LiteralArgumentBuilder.literal<ServerCommandSource>("compose-test").executes { ctx ->
                val player = ctx.source.player!!

                val holder = InteractionEntity(EntityType.INTERACTION, player.world)
                holder.interactionWidth = 0f
                holder.interactionHeight = 0f
                holder.calculateDimensions()
                player.world.spawnEntity(holder)
                holder.setPosition(player.x, player.y + 1, player.z)

                // Avoid keeping a strong reference to player in the composable
                val weakPlayer = WeakReference(player)

                val ref = holder.openDisplayGui {
                    // Always face player
                    LaunchedEffect(Unit) {
                        while (isActive) {
                            delay(1.ticks)
                            weakPlayer.get()?.let { p ->
                                face(p)
                            }
                        }
                    }

                    // Item to show
                    var item by remember { mutableStateOf(Items.DIAMOND_PICKAXE) }

                    Column {
                        // Clicking the item name cycles through items
                        Clickable({
                            val itemsToShow = arrayOf(
                                Items.DIAMOND_PICKAXE,
                                Items.GOLD_ORE,
                                Items.NETHER_STAR,
                                Items.GRASS_BLOCK,
                            )
                            val current = itemsToShow.indexOf(item)
                            item = itemsToShow[(current + 1) % itemsToShow.size]
                        }) {
                            // Show item name
                            Text(item.name)
                        }

                        // Display Context for rendered item
                        var index by remember { mutableStateOf(0) }

                        Row {
                            // Speed at which the item rotates
                            var rotationSpeed by remember { mutableStateOf(0.3f) }

                            // Slider to change speed
                            VerticalSlider(
                                rotationSpeed,
                                0f..1f,
                                style = SliderStyle(
                                    trackHeight = 0.8f,
                                    isDownSlider = false
                                )
                            ) {
                                rotationSpeed = it
                            }

                            // Clicking the item cycles through Display Contexts
                            Clickable({
                                index = (index + 1) % ItemDisplayContext.entries.size
                            }) {
                                // Item rotation
                                var itemTransform by remember { mutableStateOf(Matrix4f().identity()) }

                                LaunchedEffect(Unit) {
                                    // Rotate item every tick
                                    while (isActive) {
                                        itemTransform = Matrix4f(itemTransform).rotateY(rotationSpeed * PI.toFloat() / 20f)
                                        delay(1.ticks)
                                    }
                                }

                                // Show item with context and rotation
                                Item(
                                    item,
                                    ItemDisplayContext.entries[index],
                                    itemTransform,
                                )
                            }

                            // Show Display Context name
                            Text(ItemDisplayContext.entries[index].name)
                        }
                    }
                }

                ref.startWatching(player)

                // Notes:
                // - ref and holder will not be removed after the player leaves using the code in this snippet
                // - The composable may keep running until the player is garbage collected or the holder entity is killed.
                // - The composable code runs off-thread; Depending on what you want to do, you may need to rely on `server.execute`

                0
            })
        }
    }
}
