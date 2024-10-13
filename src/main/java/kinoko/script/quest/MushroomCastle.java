package kinoko.script.quest;

import kinoko.provider.map.PortalInfo;
import kinoko.script.common.Script;
import kinoko.script.common.ScriptHandler;
import kinoko.script.common.ScriptManager;
import kinoko.script.common.ScriptMessageParam;
import kinoko.util.Tuple;
import kinoko.world.field.mob.MobAppearType;
import kinoko.world.quest.QuestRecordType;
import kinoko.world.user.User;

import java.util.List;
import java.util.Map;
import java.util.Random;

public final class MushroomCastle extends ScriptHandler {

    static Random random = new Random();

    @Script("q2314s")
    public static void q2314s(ScriptManager sm) {
        // Exploring Mushroom Forest (1) (2314 - start)
        if(!sm.askAccept("In order to rescue the princess, you must first investigate the Mushroom Forest. King Pepe has somehow set up a powerful barrier preventing anyone from entering the castle. Please investigate this matter for us right away.")) {
            sm.sayOk("Please do not lose faith in the Mushroom Kingdom.");
            return;
        }

        sm.forceStartQuest(2314);
        sm.sayNext("You'll run into the barrier in the Mushroom Forest if you head over to the east from your current location. Please be careful, though. From what I've heard, the area is infested with many atrocious monsters.");
    }

    @Script("investigate1")
    public static void investigate1(ScriptManager sm) {
        // Mushroom Castle : Deep Inside Mushroom Forest (106020300)
        //   investigate1 (1112, -18)
        if(sm.hasQuestStarted(2314) && !sm.hasQRValue(QuestRecordType.MushroomCastleInvestigation, "1")) {
            sm.sayNext("This looks to be a type of \"Mushroom Spore\" that has been transformed by magic into a strong defense barrier. It doesn't appear penetrable through physical force. Return to the #bSecretary of Domestic Affairs#k and report this matter.", ScriptMessageParam.PLAYER_AS_SPEAKER);
            sm.avatarOriented("Effect/OnUserEff.img/normalEffect/mushroomcastle/chatBalloon1");
            sm.setQRValue(QuestRecordType.MushroomCastleInvestigation, "1");
            return;
        }
        if(sm.hasItem(2430014) && !sm.hasQuestCompleted(2338)) {
            sm.sayOk("It seems as if the barrier could be broken using a Killer Mushroom Spore.", ScriptMessageParam.PLAYER_AS_SPEAKER);
        }
    }

    @Script("q2314e")
    public static void q2314e(ScriptManager sm) {
        sm.sayOk("I see that you have thoroughly investigated the barrier in the Mushroom Forest. What was it like?");
        sm.forceCompleteQuest(2314);
        sm.addExp(1650);
        sm.sayOk("So I see it wasn't an ordinary barrier by any means. Great work! If it weren't for your help, we wouldn't have had a clue.");
    }

    @Script("q2322s")
    public static void q2322s(ScriptManager sm) {
        // Over the Castle Walls (2) (2322 - start)
        if(!sm.askAccept("Like I told you, we can't be relieved just because the barrier has been broken. The castle of the Mushking Empire is impenetrable from the outside, so it won't be easy for you to enter. First, would you mind investigating the outer walls of the castle?")) {
            sm.sayOk("Oh, really? You think you have a better idea?! Come talk to me when you get stuck outside the Castle Walls.");
            return;
        }

        sm.forceStartQuest(2322);
        sm.sayNext("Head over to the castle from the #bSplit Road of Destiny#k, past the Mushroom Forest. Good luck.");
    }

    @Script("obstacle")
    public static void obstacle(ScriptManager sm) {
        // Mushroom Castle : Deep Inside Mushroom Forest (106020300)
        //   obstacle (1313, -11)
        if(!sm.hasQuestCompleted(2338)) {
            if(!sm.hasItem(4000507)) {
                sm.message(!sm.hasItem(2430014) ? "The overgrown vines are blocking the way." : "You must remove the barrier by using the Killer Mushroom Spore first.");
                sm.scriptProgressMessage("You cannot move forward due to the barrier.");
                return;
            }
            sm.removeItem(4000507, 1);
            sm.message("You have used a Poison Spore to pass through the barrier.");
        }
        sm.playPortalSE();
        sm.warp(106020400, "left00");
    }

    @Script("killarmush")
    public static void killarmush(ScriptManager sm) {  //Unsure if GMS-like, Referenced Vertisy for these ones
        // Killer Mushroom Spore (2430014)
        if (sm.askAccept("#bDo you want to use the killer mushroom Spore?#k\r\n\r\n#r#e<Caution>\r\n#nNot for human consumption!\r\nIf ingested, seek medical attention immediately!")) {
            if(sm.getFieldId() != 106020300) {
                sm.sayOk("It doesn't look like there's anything to use the Killer Mushroom Spore on around here!");
                return;
            }

            PortalInfo ptObstacle = sm.getField().getPortalByName("obstacle").get();
            boolean close = (sm.getUser().getX() >= (ptObstacle.getX() - 300)); //210 in Vertisy
            if (!close) {
                sm.message("You must be closer in order to use the Killer Mushroom Spore."); //Not GMS-like, unsure what this should say.
                return;
            }
            sm.sayNext("Success! The barrier is broken!");
            sm.scriptProgressMessage("Mushroom Forest Barrier Removal Complete 1/1");
            sm.message("The Mushroom Forest Barrier has been removed and penetrated.");
            sm.removeItem(2430014);

            if(!sm.hasQuestStarted(2338))
                sm.forceStartQuest(2338);
            sm.forceCompleteQuest(2338);

            sm.playPortalSE();
            sm.warp(106020400, "left00");
        }
    }

    @Script("gotocastle")
    public static void gotocastle(ScriptManager sm) {
        // Mushroom Castle : Split Road of Destiny (106020400)
        //   right00 (1388, -24)
        boolean thorns = !sm.hasQRValue(QuestRecordType.MushroomCastleThornRemover, "1");
        sm.playPortalSE();
        sm.warp(thorns ? 106020500 : 106020501);
    }

    @Script("investigate2")
    public static void investigate2(ScriptManager sm) {
        // Mushroom Castle : Castle Wall Edge (106020500)
        //   investigate2 (258, -14)
        //   investigate2-1 (258, -114)
        if(!sm.hasQRValue(QuestRecordType.MushroomCastleInvestigation2, "1")) {
            sm.setNotCancellable(true);
            sm.sayNext("The colossal castle wall is covered with thorny vines. It's going to be difficult getting into the castle. For now, go report this to the #b#p1300003##k.", ScriptMessageParam.PLAYER_AS_SPEAKER);
            sm.setQRValue(QuestRecordType.MushroomCastleInvestigation2, "1");
            sm.scriptProgressMessage("Castle Wall Investigation Completed 1/1");
        }
    }

    @Script("removethorns")
    public static void removethorns(ScriptManager sm) {
        // Thorn Remover (2430015)
        if (sm.getFieldId() != 106020500) {
            sm.sayOk("There's nothing to use the #bThorn Remover#k on around here.");
            return;
        }
        if (sm.askAccept("Do you wish to use the #bThorn Remover#k?")) {
            sm.setQRValue(QuestRecordType.MushroomCastleThornRemover, "1");
            sm.removeItem(2430015);
            sm.warp(106020502);
        }
    }

    @Script("q2338s")
    public static void q2338s(ScriptManager sm) {
        // Killer Mushroom Spores, Again (2338 - start)
        User user = sm.getUser();
        boolean hasSpores = user.getInventoryManager().hasItem(2430014, 1);
        if(!sm.hasQuestCompleted(2338)) {
            if (hasSpores) {
                sm.sayOk("Just use the #bKiller Mushroom Spores#k I gave you."); //Not GMS-like
                return;
            }
            sm.sayOk("You need more Killer Mushroom Spores?"); //Not GMS-like
            sm.forceStartQuest(2338);
        }

    }

    @Script("TD_MC_jump")
    public static void TD_MC_jump(ScriptManager sm) {
        // Mushroom Castle : Shadow Cliffs (106020403)
        //   top00 (1174, -970)
        int which = random.nextInt(2);
        sm.warp(106020600 + which);
    }

    @Script("TD_MC_faild")
    public static void TD_MC_faild(ScriptManager sm) {
        // Mushroom Castle : On the Watch (106020601)
        //   trap00 (-936, -139)
        sm.reservedEffect("Effect/Direction2.img/mushCatle/nugu");
        sm.message("You've been spotted by the guard and will now be sent to the bottom of the cliff.");

        // As seen here: https://youtu.be/E-oFRZcYbF4?t=277
        // For some reason this effect has no field node, unless I used the wrong one.
        try {
            Thread.sleep(2000); // Wait for 2 seconds (2000 milliseconds)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle the interruption
            e.printStackTrace();
        }

        gotocastle(sm);
    }

    @Script("go_secretroom")
    public static void go_secretroom(ScriptManager sm) {
        // Mushroom Castle : Skyscraper 3 (106021000)
        //   in00 (927, -97)
        if(sm.hasQuestStarted(2335) && sm.hasItem(4032405)) {
            sm.message("You used the Secret Key to enter.");
            sm.removeItem(4032405);
            sm.warpInstance(106021001, "out00", 106021000, 60 * 60);
            return;
        }
        sm.message("You can't enter without the Secret Key."); //Not GMS-like
    }

    @Script("in_secretroom")
    public static void in_secretroom(ScriptManager sm) {
        // Mushroom Castle : Security Room (106021001)
        // Mushroom Castle : Security Room (106021002)
        // Mushroom Castle : Security Room (106021003)
        // Mushroom Castle : Security Room (106021004)
        // Mushroom Castle : Security Room (106021005)
        // Mushroom Castle : Security Room (106021006)
        // Mushroom Castle : Security Room (106021007)
        // Mushroom Castle : Security Room (106021008)
        // Mushroom Castle : Security Room (106021009)
        // Mushroom Castle : Security Room (106021010)
        sm.forceCompleteQuest(2335);
        sm.message("Quest Complete: Eliminate the Minions");
        sm.addExp(1200);
    }

    @Script("find_james")
    public static void find_james(ScriptManager sm) {
        // Mushroom Castle : Central Castle Tower (106021201)
        //   find_james (-46, 96)
        if (!sm.hasQuestCompleted(2325)) {
            sm.avatarOriented("Effect/OnUserEff.img/normalEffect/mushroomcastle/chatBalloon2");
        }
    }

    @Script("q2325e")
    public static void q2325e(ScriptManager sm) {
        // James's Whereabouts (1) (2325 - end)
        sm.sayNext("H-h-help! I'm so scared!");
        sm.setNpcAction(1300008, "out");
        sm.sayBoth("What? My brother sent you here? Whew... I'm safe now. Thank you so much.");
        sm.addExp(800);
        sm.forceCompleteQuest(2325);
    }

    @Script("q2327s")
    public static void q2327s(ScriptManager sm) {
        // James's Whereabouts (3) (2327 - start)
        if(!sm.askYesNo("Okay, it's time to use the #t4001317#")) {
            return;
        }
        sm.sayNext("Thank you so much. Let me put on this disguise before we start.");
        sm.setNpcAction(1300008, "hat");
        sm.forceStartQuest(2327);
        sm.forceCompleteQuest(2327);
        sm.addExp(800);
    }

    @Script("TD_MC_keycheck")
    public static void TD_MC_keycheck(ScriptManager sm) {
        // Mushroom Castle : East Castle Tower (106021400)
        User user = sm.getUser();
        boolean hasKey = user.getInventoryManager().hasItem(4032388, 1);
        if(hasKey && !sm.hasQuestStarted(2332)) {
            sm.scriptProgressMessage("Acquired the Wedding Hall Key 1/1");
            sm.sayNext("This is #b#t4032388##k! This will allow us to enter the #m106021600#, where #bPrincess #p1300002##k is imprisoned.", ScriptMessageParam.PLAYER_AS_SPEAKER);
        }
    }

    @Script("q2332s")
    public static void q2332s(ScriptManager sm) {
        // Where's Violetta? (2332 - start)
        sm.forceStartQuest(2332);
    }

    @Script("TD_MC_Egate")
    public static void TD_MC_Egate(ScriptManager sm) {
        // Mushroom Castle : East Castle Tower (106021400)
        //   left00 (-190, 141)
        sm.playPortalSE();
        sm.warp(106021300, "right00");
    }

    @Script("TD_MC_enterboss1")
    public static void TD_MC_enterboss1(ScriptManager sm) {
        // Mushroom Castle : East Castle Tower (106021400)
        //   TD_MC_enterboss1 (268, -699)
        sm.setSpeakerId(1300012);
        final List<Tuple<Integer, String>> maps = List.of(
                Tuple.of(106021500, "1. Bringing Down King Pepe (Party: 2-6 / Level: 30 or higher)"),
                Tuple.of(106021401, "2. Saving Violetta (Solo only / Level: 30 or higher)")
        );
        final Map<Integer, String> options = createOptions(maps, Tuple::getRight);
        final int choice = sm.askMenu("You will be moved to the #b#m106021401##k. Where would you like to go?\r\n", options);
        final int mapId = maps.get(choice).getLeft();
        if(choice == 0) {
            //if(sm.checkParty(2, 30)) {
                sm.playPortalSE();
                sm.partyWarpInstance(mapId, "out01", 106021400, 10 * 60);
                return;
            //}
            //sm.sayOk("You must be in a party of 2-6 members at level 30 or higher to continue.");
            //return;
        }
        if(!sm.hasItem(4032388)) {
            sm.sayOk("You cannot enter without the #t4032388#.");
            return;
        }
        if(sm.checkParty(1, 30) || sm.checkParty(0, 30)) {
            sm.playPortalSE();
            sm.warp(mapId, "out00");
        }

    }

    @Script("TD_MC_bossEnter")
    public static void TD_MC_bossEnter(ScriptManager sm) {
        // Door to East Castle Tower (1300012)
        //   Mushroom Castle : East Castle Tower (106021400)
        TD_MC_enterboss1(sm);
    }

    @Script("TD_MC_enterboss2")
    public static void TD_MC_enterboss2(ScriptManager sm) {
        // Mushroom Castle : The Last Castle Tower (106021402)
        //   right00 (255, 141)
        if(!sm.hasQuestCompleted(2332)) { //Unsure if GMS-like, don't think it matters though.
            sm.setSpeakerId(1300013);
            if (!sm.hasItem(4032388) || !sm.askYesNo("You can enter the #m106021600# using the #t4032388#")) {
                return;
            }
        }
        sm.playPortalSE();
        sm.warpInstance(106021600, "left00", 106021402, 10 * 60); //GMS-like would have you warp to "sp" instead.
    }

    @Script("TD_MC_violetaEnter")
    public static void TD_MC_violetaEnter(ScriptManager sm) {
        // Blocked Entrance (1300013)
        //   Mushroom Castle : The Last Castle Tower (106021402)
        TD_MC_enterboss2(sm);
    }

    @Script("summon_pepeking")
    public static void summon_pepeking(ScriptManager sm) {
        // Mushroom Castle : Entrance to Wedding Hall (106021500)
        // Mushroom Castle : Castle Tower that leads to the Top (106021501)
        // Mushroom Castle : Entrance to Wedding Hall (106021502)
        // Mushroom Castle : Entrance to Wedding Hall (106021503)
        // Mushroom Castle : Entrance to Wedding Hall (106021504)
        // Mushroom Castle : Entrance to Wedding Hall (106021505)
        // Mushroom Castle : Entrance to Wedding Hall (106021506)
        // Mushroom Castle : Entrance to Wedding Hall (106021507)
        // Mushroom Castle : Entrance to Wedding Hall (106021508)
        // Mushroom Castle : Entrance to Wedding Hall (106021509)
        int variant = random.nextInt(3);
        sm.setInstanceVariable("pepeVariant", String.valueOf(variant));
        sm.spawnMob(3300005 + variant, MobAppearType.NORMAL, 100, -100, true);
    }

    @Script("out_pepeking")
    public static void out_pepeking(ScriptManager sm) {
        // Mushroom Castle : Entrance to Wedding Hall (106021500)
        //   out01 (-487, -66)


        if(sm.hasQRValue(QuestRecordType.MushroomCastlePepe, "001001001") && !sm.getUser().getInventoryManager().hasItem(4032388, 1)) {
            if(sm.getField().getMobPool().isEmpty()) {
                if (!sm.addItem(4032388, 1)) {
                    sm.message("Please make room in your Etc inventory."); //Unsure if GMS-like
                    return;
                }
                sm.message("You have acquired a key to the Wedding Hall. King Pepe must have dropped it.");
            }
        }
        sm.playPortalSE();
        sm.warp(106021400, "TD_MC_enterboss1"); //portalName not GMS-like, QOL so you don't have to climb every time.
    }

    @Script("findvioleta")
    public static void findvioleta(ScriptManager sm) {
        // Mushroom Castle : Wedding Hall (106021600)
        // Mushroom Castle : Wedding Hall (106021601)
        // Mushroom Castle : Wedding Hall (106021602)
        // Mushroom Castle : Wedding Hall (106021603)
        // Mushroom Castle : Wedding Hall (106021604)
        // Mushroom Castle : Wedding Hall (106021605)
        // Mushroom Castle : Wedding Hall (106021606)
        // Mushroom Castle : Wedding Hall (106021607)
        // Mushroom Castle : Wedding Hall (106021608)
        // Mushroom Castle : Wedding Hall (106021609)
        if(sm.hasQuestStarted(2332)) {
            sm.forceCompleteQuest(2332);
            sm.addExp(800);
            sm.scriptProgressMessage("<Where is Violetta?> Quest Complete 1/1");
        }
    }

    @Script("q2333s")
    public static void q2333s(ScriptManager sm) {
        // The Story of Betrayal (2333 - start)
        sm.sayNext("Ah, you're the brave hero that has come to save me, #b#h0##k! I knew you'd come! *Sniff sniff*", ScriptMessageParam.FLIP_SPEAKER);
        sm.sayBoth("Are you alright, Princess?", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("Yes, I'm fine. But my father... how is my father? Is he alright?", ScriptMessageParam.FLIP_SPEAKER);
        sm.sayBoth("Yes, #b#p1300000##k is in a safe place outside the castle with his ministers.", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.setSpeakerId(1300001);
        sm.sayBoth("How dare you step foot in here! You're terribly mistaken if you think this is how it ends!", ScriptMessageParam.SPEAKER_ON_RIGHT);
        sm.setSpeakerId(1300002);
        sm.sayBoth("Watch out! It's dangerous. He's trying to summon the one who's behind all of this!", ScriptMessageParam.FLIP_SPEAKER);
        sm.sayBoth("The one who's behind all of this? Are you saying there is someone else that's responsible for this?", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.setSpeakerId(1300001);
        sm.sayBoth("Silence! He'll be here soon!", ScriptMessageParam.SPEAKER_ON_RIGHT);
        sm.setSpeakerId(1300002);
        sm.sayBoth("#bThe #o3300008#! Please defeat the #o3300008#!", ScriptMessageParam.FLIP_SPEAKER);
        sm.spawnMob(3300008, MobAppearType.PRIMEMINISTER, 215, 142, true);
        sm.forceStartQuest(2333);
        sm.scriptProgressMessage("New Mission! Defeat the Prime Minister!");
    }

    @Script("q2333e")
    public static void q2333e(ScriptManager sm) {
        // The Story of Betrayal (2333 - end)
        if(!sm.canAddItem(4032386, 1)) {
            sm.sayOk("Please have at least 1 slot empty in your Etc window.", ScriptMessageParam.FLIP_SPEAKER);
            return;
        }
        sm.sayNext("You did it, #b#h0##k! I don't know how to thank you.", ScriptMessageParam.FLIP_SPEAKER);
        sm.setSpeakerId(1300001);
        sm.sayBoth("No way! Even the #o3300008#?!", ScriptMessageParam.SPEAKER_ON_RIGHT);
        sm.sayBoth("#b#p1300001##k! This is where your foolhardy dreams end! I will spare your life, but you must head back to where you came from. Go back to #bIce Land#k at once!", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("Wait! Before you go, I must get something that can serve as evidence that I defeated you in a battle.", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("Grrrr...", ScriptMessageParam.SPEAKER_ON_RIGHT);
        sm.sayBoth("Give me your crown! Princess, please take the crown.", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("You mark my words. This isn't over between us!", ScriptMessageParam.SPEAKER_ON_RIGHT);
        sm.forceCompleteQuest(2333);
        //sm.addItem(4032386, 1); //You get both items afterwards
        sm.addExp(3000);
        sm.warp(106021700);
    }

    @Script("q2334s")
    public static void q2334s(ScriptManager sm) {
        // The Identity of the Princess (2334 - start)
        sm.sayNext("Thank you so much, #b#h0##k. You are the hero that has saved our empire from danger. I'm so grateful for what you've done, I don't know how to thank you. And please understand why I can't show you my face.", ScriptMessageParam.FLIP_SPEAKER);
        sm.sayBoth("It's humiliating to say this, but ever since I was a baby, my family has kept my face veiled from the world. They feared of men falling hopelessly in love with me. I've grown so accustomed to it that I even shy away from women. I know, it's rude of me to have my back turned against the hero, but I'll need some time to muster my courage before I can greet you face to face.", ScriptMessageParam.FLIP_SPEAKER);
        sm.sayBoth("I see... \r\n#b(Wow, how pretty could she be?)", ScriptMessageParam.PLAYER_AS_SPEAKER);

        sm.forceStartQuest(2334);
        sm.forceCompleteQuest(2334);
        sm.addExp(1000);
        sm.setNpcAction(1300002, "face");

        sm.sayBoth("#b(What the--)", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("#b(Is that what's considered pretty in the world of mushrooms?!)", ScriptMessageParam.PLAYER_AS_SPEAKER);
        sm.sayBoth("I'm so shy, I'm blushing. Anyways, thank you, #b#h0##k.", ScriptMessageParam.FLIP_SPEAKER);
    }

    @Script("q2335s")
    public static void q2335s(ScriptManager sm) {
        // Eliminating the Rest (2335 - start)
        sm.sayNext("This is not the end, #b#h0##k. Minions of the #b#o3300008##k can still be found scattered throughout the castle.", ScriptMessageParam.FLIP_SPEAKER);
        if(sm.askAccept("From what I've heard, there is a place near #b#m##k where a group of the #o3300008#'s minions can be found. I've picked up a key that the #o3300008# has dropped the other day. Here, use this key.", ScriptMessageParam.FLIP_SPEAKER)) {
            if(!sm.addItem(4032405, 1)) {
                sm.sayOk("Please have at least 1 slot empty in your Etc window.", ScriptMessageParam.FLIP_SPEAKER);
                return;
            }
            sm.sayNext("For one last time, good luck.", ScriptMessageParam.FLIP_SPEAKER);
            sm.forceStartQuest(2335);
        }
    }

    @Script("pepeking_effect")
    public static void pepeking_effect(ScriptManager sm) { //TODO: This part doesn't always work for some reason.
        // Mushroom Castle : Entrance to Wedding Hall (106021500)
        // Mushroom Castle : Castle Tower that leads to the Top (106021501)
        // Mushroom Castle : Entrance to Wedding Hall (106021502)
        // Mushroom Castle : Entrance to Wedding Hall (106021503)
        // Mushroom Castle : Entrance to Wedding Hall (106021504)
        // Mushroom Castle : Entrance to Wedding Hall (106021505)
        // Mushroom Castle : Entrance to Wedding Hall (106021506)
        // Mushroom Castle : Entrance to Wedding Hall (106021507)
        // Mushroom Castle : Entrance to Wedding Hall (106021508)
        // Mushroom Castle : Entrance to Wedding Hall (106021509)
        int variant = Integer.parseInt(sm.getInstanceVariable("pepeVariant"));
        String which = variant == 2 ? "W" : (variant == 1 ? "G" : "B");
        sm.screenEffect("pepeKing/frame/W");
        sm.screenEffect("pepeKing/pepe/pepe" + which);
        sm.screenEffect("pepeKing/frame/B");
        sm.screenEffect("pepeKing/chat/nugu");
    }

    @Script("TD_MC_first")
    public static void TD_MC_first(ScriptManager sm) {
        // Singing Mushroom Forest : Ghost Mushroom Forest (100020400)
        //   TD00 (-1094, 214)
        if (sm.getLevel() < 30) {
            sm.message("A strange force is blocking you from entering.");
        } else if (sm.getQRValue(QuestRecordType.MushroomCastleOpening).equals("1")) {
            sm.playPortalSE();
            sm.warp(106020000, "left00"); // Mushroom Castle : Mushroom Forest Field
        } else {
            sm.warp(106020001); // TD_MC_Openning
        }
    }

    @Script("TD_MC_title")
    public static void TD_MC_title(ScriptManager sm) {
        // Mushroom Castle : Mushroom Forest Field (106020000)
        sm.screenEffect("temaD/enter/mushCatle");
    }

    @Script("TD_MC_Openning")
    public static void TD_MC_Openning(ScriptManager sm) {
        // null (106020001)
        sm.setDirectionMode(true, 0);
        sm.reservedEffect("Effect/Direction2.img/open/back0");
        sm.reservedEffect("Effect/Direction2.img/open/back1");
        sm.reservedEffect("Effect/Direction2.img/open/light");
        sm.reservedEffect("Effect/Direction2.img/open/pepeKing");
        sm.reservedEffect("Effect/Direction2.img/open/line");
        sm.reservedEffect("Effect/Direction2.img/open/violeta0");
        sm.reservedEffect("Effect/Direction2.img/open/violeta1");
        sm.reservedEffect("Effect/Direction2.img/open/frame");
        sm.reservedEffect("Effect/Direction2.img/open/chat");
        sm.reservedEffect("Effect/Direction2.img/open/out");
        sm.setDirectionMode(false, 12000);
    }

    @Script("TD_MC_gasi")
    public static void TD_MC_gasi(ScriptManager sm) {
        // null (106020502)
        sm.setDirectionMode(true, 0);
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi1");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi2");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi22");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi3");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi4");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi5");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi6");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi7");
        sm.reservedEffect("Effect/Direction2.img/gasi/gasi8");
        //sm.setDirectionMode(false, 13000); //Already done in TD_MC_gasi2
    }

    @Script("TD_MC_gasi2")
    public static void TD_MC_gasi2(ScriptManager sm) {
        // Mushroom Castle : Castle Wall Edge (106020501)
        sm.setDirectionMode(false, 0);
    }
}
