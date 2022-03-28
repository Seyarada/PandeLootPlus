package net.seyarada.pandeloot.flags;

import net.seyarada.pandeloot.Logger;
import net.seyarada.pandeloot.PandeLoot;
import net.seyarada.pandeloot.flags.conditions.*;
import net.seyarada.pandeloot.flags.effects.*;
import net.seyarada.pandeloot.flags.types.IFlag;

import java.util.HashMap;
import java.util.logging.Level;

public class FlagManager {

    static final HashMap<String, IFlag> flags = new HashMap<>();
    static final HashMap<IFlag, String> flagsInverted = new HashMap<>();
    static final HashMap<String, FlagEffect> flagsData = new HashMap<>();

    public FlagManager() {
        loadFlags();
        loadConditionFlags();
        Logger.log("Loaded %o flags", loadedFlags);
    }

    public static IFlag getFromID(String id) {
        return flags.get(id.toLowerCase());
    }

    public static String getFromClass(IFlag flagClass) {
        return flagsInverted.get(flagClass);
    }

    int loadedFlags;


    public void loadFlags() {
        registerFlag(new ActionbarFlag());
        registerFlag(new AmountFlag());
        registerFlag(new BeamFlag());
        registerFlag(new BroadcastFlag());
        registerFlag(new ColorFlag());
        registerFlag(new CommandFlag());
        registerFlag(new ConsumeItemFlag());
        registerFlag(new CustomModelDataFlag());
        registerFlag(new DelayFlag());
        registerFlag(new ExperienceFlag());
        registerFlag(new ExplodeFlag());
        registerFlag(new FireworkFlag());
        registerFlag(new GlowFlag());
        registerFlag(new HologramFlag());
        registerFlag(new MessageFlag());
        registerFlag(new PityFlag());
        registerFlag(new PreventPickupFlag());
        registerFlag(new RemoveFlag());
        registerFlag(new RollBagFlag());
        registerFlag(new SoundFlag());
        registerFlag(new StackableFlag());
        registerFlag(new StopFlag());
        registerFlag(new TitleFlag());
        registerFlag(new ToInventoryFlag());
        registerFlag(new ToastFlag());
        registerFlag(new VisibilityFlag());

        if (PandeLoot.discordEnabled) registerFlag(new DiscordFlag());
        if (PandeLoot.ecoEnabled) registerFlag(new EconomyFlag());
        if (PandeLoot.mythicEnabled) registerFlag(new MythicSkillFlag());
        if (PandeLoot.mmoItemsEnabled) registerFlag(new TypeFlag());
    }

    public void loadConditionFlags() {
        registerFlag(new ChanceFlag());
        registerFlag(new DamageFlag());
        registerFlag(new FirstHitFlag());
        registerFlag(new HoldingFlag());
        registerFlag(new LastHitFlag());
        registerFlag(new PermissionFlag());
        registerFlag(new TopFlag());

        if (PandeLoot.mythicEnabled) registerFlag(new MythicConditionFlag());
    }

    void registerFlag(IFlag flag) {
        FlagEffect flagEffect = flag.getClass().getAnnotation(FlagEffect.class);
        String flagID = flagEffect.id().toLowerCase();
        if(flagAlreadyRegistered(flagID)) return;

        /*
        // Auto assign flag scope
        if(Arrays.stream(flagEffect.scope()).toList().contains(FlagScope.AUTO)) {
            ArrayList<FlagScope> newScope = new ArrayList<>();
            if(flag instanceof IGeneralEvent) newScope.add(FlagScope.GENERAL);
            if(flag instanceof IItemEvent) newScope.add(FlagScope.ITEM);
            if(flag instanceof IPlayerEvent) newScope.add(FlagScope.PLAYER);
        }
         */
        loadedFlags++;

        flags.put(flagID, flag);
        flagsInverted.put(flag, flagID);
        flagsData.put(flagID, flagEffect);
    }

    boolean flagAlreadyRegistered(String id) {
        if(flags.containsKey(id)) {
            Logger.log(Level.WARNING, "Error registering flag by ID: "+id+", flag already exists");
            return true;
        }
        return false;
    }

}
