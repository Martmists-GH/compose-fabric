package com.martmists.compose

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.runtime.Recomposer
import com.martmists.compose.impl.DisplayNodeApplier
import com.martmists.compose.impl.SnapshotManager
import com.martmists.compose.impl.node.DisplayRootNode
import com.martmists.compose.unit.ticks
import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment
import kotlinx.coroutines.*
import net.minecraft.entity.Entity
import org.joml.Matrix4f
import org.joml.Quaterniond
import org.joml.Quaternionf
import org.joml.Vector3d
import java.lang.Exception
import java.lang.ref.WeakReference
import kotlin.math.PI
import kotlin.time.Duration.Companion.milliseconds

interface DisplayRoot {
    fun close()
    suspend fun setRootTransform(transform: Matrix4f)
    suspend fun face(entity: Entity)
}

fun Entity.openDisplayGui(block: @Composable DisplayRoot.() -> Unit): ElementHolder {
    val holder = ElementHolder()
    val root = DisplayRootNode(WeakReference(holder))

    ComposeMod.EXECUTOR.submitCatching {
        SnapshotManager.ensureStarted()
    }

    ComposeMod.EXECUTOR.submitCatching {
        val frameClock = BroadcastFrameClock()
        val effectCoroutineContext = Job(coroutineContext[Job]) + frameClock
        val recomposer = Recomposer(effectCoroutineContext)
        val composeRoot = object : DisplayRoot {
            override fun close() {
                holder.watchingPlayers.toList().all {
                    holder.stopWatching(it)
                }
                cancel()
            }

            override suspend fun setRootTransform(transform: Matrix4f) {
                root.worldTransform = transform
            }

            override suspend fun face(entity: Entity) {
                var direction = Vector3d(entity.x, 0.0, -entity.z).sub(Vector3d(
                    x,
                    0.0,
                    -z
                ))

                // If this is 0, lookAlong will become NaN and other transforms tend to break
                if (direction.x == 0.0 && direction.z == 0.0) {
                    direction = Vector3d(1.0, 0.0, 1.0)
                }

                setRootTransform(Matrix4f().identity().rotate(Quaternionf(Quaterniond().lookAlong(
                    direction,
                    Vector3d(0.0, 1.0, 0.0)
                ))))
            }
        }

        val comp = Composition(DisplayNodeApplier(root), recomposer).apply {
            setContent { composeRoot.block() }
        }

        coroutineScope {
            launch(context = frameClock, start = CoroutineStart.UNDISPATCHED) {
                recomposer.runRecomposeAndApplyChanges()
            }

            launch {
                try {
                    root.layout()
                    root.render()

                    while (this@openDisplayGui.isAlive && holder.watchingPlayers.isNotEmpty()) {
                        frameClock.sendFrame(System.nanoTime())

                        root.layout()
                        root.render()

                        // Refresh rate for each frame
                        delay(1.ticks)
                    }
                } catch (e: Exception) {
                    if (e !is CancellationException) {
                        e.printStackTrace()
                    }
                } finally {
                    recomposer.close()
                }
            }
        }

        Unit
    }

    val attachment = EntityAttachment.ofTicking(holder, this)

    return holder
}
