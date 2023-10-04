package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import org.bukkit.Bukkit;

public class GCCmd implements APCommandListener {

    private final APCommand command;

    public GCCmd(){
        this.command = new APCommand(
                "gc",
                new String[]{},
                "Appelle le Garbage Collector (Dangereux)",
                new String[]{},
                "hydranium.api.command.gc"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        player.performCommand("ms");
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            System.gc();
            Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> player.performCommand("ms"));
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
