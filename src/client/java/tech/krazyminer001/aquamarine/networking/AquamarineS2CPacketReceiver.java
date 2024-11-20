package tech.krazyminer001.aquamarine.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import tech.krazyminer001.aquamarine.rendering.MultiblockRenderer;

public class AquamarineS2CPacketReceiver {
    public static void registerS2CPacketReceivers() {
        var setMultiblockRendererMultiblockHandler = new ClientPlayNetworking.PlayPayloadHandler<AquamarineData.SetMultiblockRendererMultiblock>() {
            @Override
            public void receive(AquamarineData.SetMultiblockRendererMultiblock data, ClientPlayNetworking.Context context) {
                MultiblockRenderer.setMultiblock(data.multiblock(), data.controllerPos(), data.controllerDirection());
            }
        };

        ClientPlayNetworking.registerGlobalReceiver(AquamarineData.SetMultiblockRendererMultiblock.PACKET_ID, setMultiblockRendererMultiblockHandler);
    }
}
