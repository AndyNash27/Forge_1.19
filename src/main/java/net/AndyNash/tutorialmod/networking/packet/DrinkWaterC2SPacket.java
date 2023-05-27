package net.AndyNash.tutorialmod.networking.packet;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrinkWaterC2SPacket {
    private static final String MESSAGE_DRINK_WATER = "message.tutorialmod.drink_water";
    private static final String MESSAGE_NO_WATER = "message.tutorialmod.no_water";

    public DrinkWaterC2SPacket() {

    }

    public DrinkWaterC2SPacket(FriendlyByteBuf buf) {

    }

    public void ToBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context>  supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //在SERVER端口
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            if(hasWaterAroundThem(player, level, 2)) {
                //告诉玩家水已经喝下了
                player.sendSystemMessage(Component.translatable(MESSAGE_DRINK_WATER).withStyle(ChatFormatting.DARK_AQUA));
                //播放喝水的声音
                level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS,
                        0.5f, level.random.nextFloat() * 0.1f + 0.9f);
                //增加或者降低口渴程度


            } else{
                //告诉玩家周围没有水
                player.sendSystemMessage(Component.translatable(MESSAGE_NO_WATER).withStyle(ChatFormatting.RED));
                //输出口渴程度

            }
            //确认玩家是否在水旁边

        });
        return true;
    }

    private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }
}
