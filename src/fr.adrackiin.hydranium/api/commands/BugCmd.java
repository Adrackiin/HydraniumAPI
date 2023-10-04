package fr.adrackiin.hydranium.api.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APHash;
import fr.adrackiin.hydranium.api.utils.enumeration.Prefix;
import fr.adrackiin.hydranium.api.utils.statics.PubSub;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class BugCmd implements APCommandListener  {

    private final APCommand command;
    private final APHash<UUID, Integer> lastBug = new APHash<>();

    public BugCmd(){
        this.command = new APCommand(
                "bug",
                new String[]{},
                "Reporter un bug (tout abus sera sanctionné)",
                new String[]{"<description du bug>"},
                "hydranium.api.command.bug"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 0){
            return -1;
        }
        return 0;
    }

    @Override
    public String onCommand(String command, APPlayer player, String[] args, short syntax){
        if(lastBug.contains(player.getUUID())){
            if((int)(System.currentTimeMillis() / 1000) < lastBug.get(player.getUUID()) + 60){
                return "§7Veuillez attendre une minute entre chaque report";
            }
        }
        lastBug.update(player.getUUID(), (int)(System.currentTimeMillis() / 1000));
        Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), ()-> {
            StringBuilder b = new StringBuilder();
            for(String s : args){
                b.append(s).append(" ");
            }
            String bug = b.toString();
            if(bug.contains("¤")){
                bug = bug.replace("¤", "");
            }
            PubSub.chatStaff("Rapport de bug (" + player.getName() + ")¤" + bug);
            File folder = new File("/home/minecraft/hydranium/bugreports/");
            int max = 0;
            int c = 0;
            String name;
            for(File f : Objects.requireNonNull(folder.listFiles())){
                name = f.getName();
                c = Integer.parseInt(Character.toString(name.charAt(name.length() - 1)));
                if(c > max){
                    max = c;
                }
            }
            File file = new File(folder.getAbsolutePath() + "/Rapport de bug - " + (max + 1));
            FileWriter writer;
            try {
                writer = new FileWriter(file);
                writer.write(player.getName() + ": " + bug);
                writer.flush();
                writer.close();
            } catch(IOException e){
                e.printStackTrace();
            }
            String finalBug = bug;
            Bukkit.getScheduler().scheduleSyncDelayedTask(APICore.getPlugin(), ()-> player.sendMessage(Prefix.hydranium + "§7Votre rapport de bug a été envoyé: §e" + finalBug));
        });
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
