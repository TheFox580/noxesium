package com.noxcrew.noxesium.paper.api

import com.noxcrew.noxesium.paper.api.network.NoxesiumPacket
import io.netty.buffer.Unpooled
import net.kyori.adventure.key.Key
import net.minecraft.network.FriendlyByteBuf
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.messaging.PluginMessageListener
import org.slf4j.Logger
import java.util.concurrent.atomic.AtomicBoolean

/** Forms the basis for a Noxesium listener. */
public abstract class BaseNoxesiumListener(
    public val plugin: Plugin,
    public val logger: Logger,
    public val manager: NoxesiumManager,
) {

    public companion object {
        /** Base namespace of the plugin channel for Noxesium messages. */
        public const val NOXESIUM_NAMESPACE: String = "noxesium"
    }

    private val incomingPluginChannels = mutableMapOf<Key, PluginMessageListener>()
    private val outgoingPluginChannels = mutableSetOf<Key>()
    private val registered = AtomicBoolean(false)

    /**
     * Defines a plugin message [listener] that will be registered and unregistered with
     * this listener.
     */
    protected fun registerIncomingPluginChannel(channel: Key, listener: PluginMessageListener) {
        incomingPluginChannels[channel] = listener
        if (registered.get()) {
            plugin.server.messenger.registerIncomingPluginChannel(plugin, channel.asString(), listener)
        }
    }

    /**
     * Defines an outgoing plugin channel that will be registered and unregistered with
     * this listener.
     */
    protected fun registerOutgoingPluginChannel(channel: Key) {
        outgoingPluginChannels += channel
        if (registered.get()) {
            plugin.server.messenger.registerOutgoingPluginChannel(plugin, channel.asString())
        }
    }

    /** Registers this listener. */
    public fun register(): BaseNoxesiumListener {
        require(registered.compareAndSet(false, true)) { "Cannot register when registered" }
        for ((channel, listener) in incomingPluginChannels) {
            plugin.server.messenger.registerIncomingPluginChannel(plugin, channel.asString(), listener)
        }
        for (channel in outgoingPluginChannels) {
            plugin.server.messenger.registerOutgoingPluginChannel(plugin, channel.asString())
        }
        return this
    }

    /** Un-registers this listener. */
    public fun unregister() {
        require(registered.compareAndSet(true, false)) { "Cannot un-register when not registered" }
        for ((channel, listener) in incomingPluginChannels) {
            plugin.server.messenger.unregisterIncomingPluginChannel(plugin, channel.asString(), listener)
        }
        for (channel in outgoingPluginChannels) {
            plugin.server.messenger.unregisterOutgoingPluginChannel(plugin, channel.asString())
        }
    }

    /** Sends this packet to the given [player]. */
    public abstract fun sendPacket(player: Player, packet: NoxesiumPacket)

    /** Sends a plugin message to a player. */
    public fun Player.sendPluginMessage(channel: Key, initialCapacity: Int? = null, writer: (buffer: FriendlyByteBuf) -> Unit) {
        sendPluginMessage(
            plugin,
            channel.asString(),
            FriendlyByteBuf(initialCapacity?.let(Unpooled::buffer) ?: Unpooled.buffer()).apply(writer).array()
        )
    }

    /** Reads a byte array using [reader]. */
    public fun <T> ByteArray.readPluginMessage(reader: (buffer: FriendlyByteBuf) -> T): T =
        FriendlyByteBuf(Unpooled.wrappedBuffer(this)).let(reader)
}