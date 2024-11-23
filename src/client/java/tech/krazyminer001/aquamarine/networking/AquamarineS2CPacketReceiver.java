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

        var setMultiblockRendererMultiblockLayeredHandler = new ClientPlayNetworking.PlayPayloadHandler<AquamarineData.SetMultiblockRendererMultiblockLayered>() {
            @Override
            public void receive(AquamarineData.SetMultiblockRendererMultiblockLayered data, ClientPlayNetworking.Context context) {
                MultiblockRenderer.setMultiblock(data.multiblock(), data.controllerPos(), data.controllerDirection(), data.layered());
            }
        };

        var setMultiblockRendererMultiblockLayeredOffsetHandler = new ClientPlayNetworking.PlayPayloadHandler<AquamarineData.SetMultiblockRendererMultiblockLayeredOffset>() {
            @Override
            public void receive(AquamarineData.SetMultiblockRendererMultiblockLayeredOffset data, ClientPlayNetworking.Context context) {
                MultiblockRenderer.setMultiblock(data.multiblock(), data.controllerPos(), data.controllerDirection(), data.layered(), data.offset());
            }
        };

        ClientPlayNetworking.registerGlobalReceiver(AquamarineData.SetMultiblockRendererMultiblock.PACKET_ID, setMultiblockRendererMultiblockHandler);
        ClientPlayNetworking.registerGlobalReceiver(AquamarineData.SetMultiblockRendererMultiblockLayered.PACKET_ID, setMultiblockRendererMultiblockLayeredHandler);
        ClientPlayNetworking.registerGlobalReceiver(AquamarineData.SetMultiblockRendererMultiblockLayeredOffset.PACKET_ID, setMultiblockRendererMultiblockLayeredOffsetHandler);
    }
}
