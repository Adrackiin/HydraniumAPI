package fr.adrackiin.hydranium.api.commands;

public class Compte /*implements CommandExecutor, TabCompleter*/ {

    /*public Compte(){
        APICore.getPlugin().getCommand("compte").setExecutor(this);
    }

    private static APICore apiCore = APICore.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(APICore.getPlugin().getCommandManager().checkPermissions(sender, "")){
            commandCompte(APICore.getPlugin().getAPPlayer(((Player)sender).getUniqueId()), args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }

    private void commandCompte(APPlayer sender, String[] args){
        if(args.length <= 1){
            sender.sendMessage(Commands.COMPTE.getSyntax());
        } else {
            switch (args[0].toLowerCase()){
                case "grade":
                    rank(sender, args);
                    break;
                case "hydras":
                    break;
                case "all":
                    all(sender, args);
                    break;
                case "whitelist":
                    break;
                default:
                    sender.sendMessage(Commands.COMPTE.getSyntax());
                    break;
            }
        }
    }

    public void all(APPlayer sender, String[] args) {
        UUID uuid = Players.getOfflinePlayer(args[1]).getUniqueId();
        if (args.length == 2) { // /compte all <joueur> --> OK
            Bukkit.getScheduler().runTaskAsynchronously(apiCore, () -> { //Récupérer info = temps
                Account account = APICore.getPlugin().getAccountManager().getAccountRedis(uuid);
                if (account == null) { //Si compte n'existe pas dans Redis
                    try {
                        account = APICore.getPlugin().getAccountManager().getAccountDatabase(uuid); //Prendre de BDD
                    } catch (AccountNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (account == null) { //Si toujours inexistant
                        sender.sendMessage(Prefix.hydranium + "§c" + "Ce joueur n'existe pas");
                        return;
                    }
                }
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                sender.sendMessage(Prefix.hydranium + "§c«§8§m-----§8▌§b §a " + player.getName() + " §8§m▌-----§c»");
                sender.sendMessage("     §6» §eGrade §f– §a " + account.getRank().getPrefix());
                sender.sendMessage("     §6» §eHydras §f– §a " + account.getHydras());
                sender.sendMessage("     §6» §eWhitelists §f– §a " + account.getWhitelist());
                sender.sendMessage("     §6» §eVictoires §f– §a " + account.getWhitelist());
            });
        } else {
            sender.sendMessage(Commands.COMPTE.getSyntax());
        }
    }

    public void rank(APPlayer sender, String[] args) {
        if (args.length == 2) {
            if(args[1].equals("info")) { //OK
                sender.sendMessage(Prefix.hydranium + "§6» §eGrades disponibles §f– §badmin §6| §bbuilder §6| §bleadhost §6| §bhost §6| §bhosttest §6| §bfriend §6| §bplayer");
            } else {
                sender.sendMessage(Commands.COMPTE.getSyntax());
            }
        } else if (args.length < 5) { //OK
            Bukkit.getScheduler().runTaskAsynchronously(APICore.getPlugin(), () -> { //Temps
                OfflinePlayer target = Players.getOfflinePlayer(args[1]);
                UUID uuid = target.getUniqueId();
                Account account = APICore.getPlugin().getAccountManager().getAccountRedis(uuid); //Prend de Redis
                if (account == null) { //Compte inexistant dans Redis
                    try {
                        account = APICore.getPlugin().getAccountManager().getAccountDatabase(uuid); //Prend de BDD
                    } catch (AccountNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (account == null) { //Toujours inexistant = joueur inexistant
                        sender.sendMessage(Prefix.hydranium + "§c" + "Ce joueur n'existe pas");
                        return;
                    }
                }
                Rank targetRank = account.getRank();
                if(args.length == 3){
                    sender.sendMessage(Commands.COMPTE.getSyntax());
                } else if(args[2].equalsIgnoreCase("set")) { // /compte grade joueur set grade
                    Rank rank;
                    try {
                        rank = Rank.valueOf(args[3].toUpperCase());
                    } catch (IllegalArgumentException e) { //Si invalide
                        sender.sendMessage(Prefix.hydranium + "§c" + "Veuillez entrer un grade valide (/compte grade info)");
                        return;
                    }
                    if(rank == targetRank){ //Si le grade est indentique au grade actuel
                        sender.sendMessage(Prefix.hydranium + "§c" + "Grade identique à celui actuel");
                        return;
                    }
                    APICore.getPlugin().getDataManager().set(uuid, DataManager.Data.RANK, rank); //Envoie nouveau trade
                    sender.sendMessage(Prefix.hydranium + "§7Le joueur §b " + target.getName() + " §7est maintenant §b " + (rank == Rank.PLAYER ? "Joueur" : rank.getPrefix())); //Confirmer changement
                } else { // /compte grade joueur ... ...
                    sender.sendMessage(Commands.COMPTE.getSyntax());
                }
            });
        } else { //Trop d'args
            sender.sendMessage(Commands.COMPTE.getSyntax());
        }
    }*/
}
