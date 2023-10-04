package fr.adrackiin.hydranium.api.utils.statics;

import net.minecraft.server.v1_8_R3.MinecraftServer;

public class Server {

    public static double[] getTPS(){
        return MinecraftServer.getServer().recentTps;
    }

}
