package com.noxcrew.noxesium.network.clientbound;

import com.noxcrew.noxesium.network.NoxesiumPacket;
import com.noxcrew.noxesium.network.NoxesiumPackets;
import com.noxcrew.noxesium.network.NoxesiumPayloadType;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Resets the stored value for extra data on an entity.
 */
public record ClientboundResetExtraEntityDataPacket(int entityId, IntList indices) implements NoxesiumPacket {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundResetExtraEntityDataPacket> STREAM_CODEC =
            CustomPacketPayload.codec(
                    ClientboundResetExtraEntityDataPacket::write, ClientboundResetExtraEntityDataPacket::new);

    private ClientboundResetExtraEntityDataPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readIntIdList());
    }

    private void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeIntIdList(indices);
    }

    @Override
    public NoxesiumPayloadType<?> noxesiumType() {
        return NoxesiumPackets.CLIENT_RESET_EXTRA_ENTITY_DATA;
    }
}
