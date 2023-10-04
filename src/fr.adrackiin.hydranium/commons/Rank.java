package fr.adrackiin.hydranium.commons;

public enum Rank { //Grades

    PLAYER("§7"),
    FRIEND("§5[§dAmi§5]§d "),
    BUILDER("§a[Builder] "),
    HOSTTEST("§b[Host-Test] "),
    HOST("§5[§bHost§6]§b "),
    LEADHOST("§3[Lead-Host] "),
    ADMIN("§6[§cAdmin§6]§c ");

    private final String prefix; //Préfixe devant pseudo

    Rank(String prefix){ //Setter
        this.prefix = prefix;
    }

    public String getPrefix() { //Getter
        return prefix;
    }
}