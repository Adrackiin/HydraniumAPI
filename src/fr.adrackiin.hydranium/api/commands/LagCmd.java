package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.statics.MathUtils;
import fr.adrackiin.hydranium.api.utils.statics.Server;

import java.text.NumberFormat;

public class LagCmd implements APCommandListener {

    private final APCommand command;

    public LagCmd(){
        this.command = new APCommand(
                "lag",
                new String[]{"ms", "tps", "ping"},
                "Voir sa latence et les performances du serveur",
                new String[]{},
                "hydranium.api.command.lag"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        int pingPlayer = player.getPing();
        player.sendMessage(Prefix.hydranium + "§7Latence avec le serveur: " + ((pingPlayer < 50) ? "§2" : (pingPlayer < 100) ? "§a" : (pingPlayer < 250) ? "§6" :
                (pingPlayer < 500) ? "§c" : "§4") + pingPlayer + "§7" + " ms ");
        double[] tpsAll = Server.getTPS();
        double tps = MathUtils.round(tpsAll[0], 2);
        if(tps > 20){
            tps = 20.00;
        }
        player.sendMessage(Prefix.hydranium + "§7Tps du serveur: " + ((tps > 18) ? "§2" : (tps > 16) ? "§a" : (tps > 14) ? "§6" :
                (tps < 12) ? "§4" : "§c") + tps);
        if(player.hasPermission("hydranium.api.command.lag.ram")){
            player.sendMessage(Prefix.hydranium + "§7Ram utitlisé: §f" + NumberFormat.getInstance().format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
