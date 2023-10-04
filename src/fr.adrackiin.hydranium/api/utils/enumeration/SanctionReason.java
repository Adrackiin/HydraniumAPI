package fr.adrackiin.hydranium.api.utils.enumeration;

public enum SanctionReason {

    CHEAT("Cheat", -1),
    DECO("Déconnexion", 3*24),
    SUICIDE("Suicide", 3*24),
    SUICIDE_PVP("Suicide (PvP)", 7*24),
    TOWER("Tower", 3*24),
    TRAP("Trap", 3*24),
    BUG("Comportement toxique", 30*24),
    CROSSTEAM("CrossTeam / NoKill / Partage de Stuff", 7*24),
    MINING("Minage non-autorisé", 7*24),
    DIGDOWN("DigDown", 3*24),
    IPVP("iPvP", 7*24),
    STALK("Stalk", 7*24),
    INSULTE("Insultes", 3*24),
    SPAM("Spam", 3*24),
    DOX("Dox", 3*24),
    SPOILSTUFF("Spoil de Stuff", 3*24),
    PROVOCATION("Provocation", 3*24);

    private final String reason;
    private final int time;

    SanctionReason(String reason, int time){
        this.reason = reason;
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public int getTime() {
        return time;
    }
}
