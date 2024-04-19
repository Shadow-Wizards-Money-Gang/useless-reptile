package nordmods.uselessreptile.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.Vec3d;
import nordmods.uselessreptile.common.entity.base.URRideableDragonEntity;
import nordmods.uselessreptile.common.network.PosSyncS2CPacket;

public class PosSyncPacket {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(PosSyncS2CPacket.POS_SYNC_PACKET, (client, handler, buffer, sender) -> {
            double x = buffer.readDouble();
            double y = buffer.readDouble();
            double z = buffer.readDouble();
            Vec3d pos = new Vec3d(x, y, z);
            URRideableDragonEntity dragon = client.world != null ? (URRideableDragonEntity) client.world.getEntityById(buffer.readInt()) : null;
            client.execute(() ->{
                if (dragon != null && dragon.getControllingPassenger() != client.player) dragon.setPosition(pos);
            });
        });
    }
}
