package fr.adrackiin.hydranium.api.utils;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class PacketUtils {

    public static Packet packetScoreboardObjective(String id, String name, int mode, int display){
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), id);
            setField(packet, packet.getClass().getDeclaredField("d"), mode);
            if(mode != 1) {
                setField(packet, packet.getClass().getDeclaredField("b"), name);
                if (display == 0) {
                    setField(packet, packet.getClass().getDeclaredField("c"), IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
                } else if (display == 1) {
                    setField(packet, packet.getClass().getDeclaredField("c"), IScoreboardCriteria.EnumScoreboardHealthDisplay.HEARTS);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetScoreboardDisplay(String id, int pos){
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), pos);
            setField(packet, packet.getClass().getDeclaredField("b"), id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static PacketPlayOutScoreboardTeam packetScoreboardTeam(String id, int mode, String name, String prefix, String suffix){
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), id);
            setField(packet, packet.getClass().getDeclaredField("h"), mode);
            if(mode == 0 || mode == 2){
                setField(packet, packet.getClass().getDeclaredField("b"), name);
                setField(packet, packet.getClass().getDeclaredField("c"), prefix);
                setField(packet, packet.getClass().getDeclaredField("d"), suffix);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetScoreboardScore(String name, int mode, String id, int score){
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), name);
            if(mode == 0){
                setField(packet, packet.getClass().getDeclaredField("d"), PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
            } else if(mode == 1){
                setField(packet, packet.getClass().getDeclaredField("d"), PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE);
            }
            setField(packet, packet.getClass().getDeclaredField("b"), id);
            setField(packet, packet.getClass().getDeclaredField("c"), score);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetActionBar(IChatBaseComponent message){
        PacketPlayOutChat packet = new PacketPlayOutChat();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), message);
            setField(packet, packet.getClass().getDeclaredField("b"), (byte)2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetTitle(PacketPlayOutTitle.EnumTitleAction action, IChatBaseComponent message, int fadeIn, int stay, int fadeOut){
        PacketPlayOutTitle packet = new PacketPlayOutTitle();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), action);
            setField(packet, packet.getClass().getDeclaredField("b"), message);
            setField(packet, packet.getClass().getDeclaredField("c"), fadeIn);
            setField(packet, packet.getClass().getDeclaredField("d"), stay);
            setField(packet, packet.getClass().getDeclaredField("e"), fadeOut);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetParticules(EnumParticle particle, float x, float y, float z, float offX, float offY, float offZ, float pData, int nb, int... data){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), particle);
            setField(packet, packet.getClass().getDeclaredField("j"), false);
            setField(packet, packet.getClass().getDeclaredField("b"), x);
            setField(packet, packet.getClass().getDeclaredField("c"), y);
            setField(packet, packet.getClass().getDeclaredField("d"), z);
            setField(packet, packet.getClass().getDeclaredField("e"), offX);
            setField(packet, packet.getClass().getDeclaredField("f"), offY);
            setField(packet, packet.getClass().getDeclaredField("g"), offZ);
            setField(packet, packet.getClass().getDeclaredField("i"), nb);
            setField(packet, packet.getClass().getDeclaredField("k"), data);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetEquipementChange(Player player, short slot, ItemStack item){
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), player.getEntityId());
            setField(packet, packet.getClass().getDeclaredField("b"), slot);
            setField(packet, packet.getClass().getDeclaredField("c"), CraftItemStack.asNMSCopy(item));
        } catch(IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
        }
        return packet;
    }

    public static Packet packetTabComplete(String[] strings){
        PacketPlayOutTabComplete packet = new PacketPlayOutTabComplete();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), strings);
        } catch(IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
        }
        return packet;
    }

    public static void setGameProfile(GameProfile profile, String name){
        try {
            setField(profile, profile.getClass().getDeclaredField("name"), name);
        } catch(IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
        }
    }

    public static Packet packetDestroyEntity(int[] ids){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
        try {
            setField(packet, packet.getClass().getDeclaredField("a"), ids);
        } catch(IllegalAccessException | NoSuchFieldException e){
            e.printStackTrace();
        }
        return packet;
    }

    private static void setField(Object packet, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(packet, value);
        field.setAccessible(false);
    }
}
