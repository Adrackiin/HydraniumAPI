package fr.adrackiin.hydranium.api.utils.packet;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public abstract class APPacketListener {

    public abstract Object onPacket(Player player, Packet packet);

}
