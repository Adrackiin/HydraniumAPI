package fr.adrackiin.hydranium.api.scoreboard;

import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.PacketUtils;
import fr.adrackiin.hydranium.api.utils.statics.StringUtils;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.Collection;

public class APScoreboard {

    private final APPlayer player;
    private final String objectiveName;
    private final String[] linesId = new String[16];
    private boolean created = false;

    public APScoreboard(APPlayer player, String objectiveName){
        this.player = player;
        this.objectiveName = objectiveName;
    }

    public void create(){
        if(created){
            return;
        }
        player.sendPacket(manageScoreboard(0));
        player.sendPacket(displayScoreboard(1));
        createTeamLines();
        created = true;
    }

    public void destroy(){
        if(!created){
            return;
        }
        player.sendPacket(manageScoreboard(1));
        removeTeams();
        created = false;
    }

    public void setLine(int line, String show){
        if(show.length() > 32){
            show = show.substring(0, 31);
        }
        if(show.length() > 16) {
            String[] toShow = StringUtils.optimizeCutString(show, 16);
            toShow[1] = ChatColor.getLastColors(toShow[0]) + toShow[1];
            player.sendPacket(PacketUtils.packetScoreboardTeam(linesId[line], 2, "", toShow[0], toShow[1]));
        } else {
            player.sendPacket(PacketUtils.packetScoreboardTeam(linesId[line], 2, "", show, ""));
        }
        player.sendPacket(PacketUtils.packetScoreboardScore(ChatColor.values()[line].toString() + "§r", 0, player.getName(), line));
    }

    public void hide(){
        displayScoreboard(-1);
    }

    public void show(){
        player.sendPacket(displayScoreboard(1));
    }

    public void removeLine(int line) {
        player.sendPacket(PacketUtils.packetScoreboardScore(ChatColor.values()[line].toString() + "§r", 1, player.getName(), 0));
    }

    private void removeTeams(){
        for(String id : linesId) {
            player.sendPacket(PacketUtils.packetScoreboardTeam(id, 1, "", "", ""));
        }
    }

    private Packet manageScoreboard(int mode){
        return PacketUtils.packetScoreboardObjective(player.getName(), objectiveName, mode, 0);
    }

    private Packet displayScoreboard(int mode){
        return PacketUtils.packetScoreboardDisplay(player.getName(), mode);
    }

    private void createTeamLines() {
        for(int i = 0; i < linesId.length; i ++){
            if(linesId[i] == null){
                linesId[i] = "APSc" + i;
                PacketPlayOutScoreboardTeam packet = PacketUtils.packetScoreboardTeam(linesId[i], 0, "", "", "");
                addEntry(packet, ChatColor.values()[i].toString() + "§r");
                player.sendPacket(packet);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addEntry(PacketPlayOutScoreboardTeam packet, String entry){
        try {
            Field packetEntry = packet.getClass().getDeclaredField("g");
            packetEntry.setAccessible(true);
            ((Collection) packetEntry.get(packet)).add(entry);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
