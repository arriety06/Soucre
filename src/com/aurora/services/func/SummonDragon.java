package com.aurora.services.func;

import java.util.HashMap;
import java.util.Map;
import com.aurora.models.item.Item;
import com.aurora.consts.ConstNpc;
import com.aurora.consts.ConstPlayer;
import com.aurora.models.item.Item.ItemOption;
import com.aurora.models.map.Zone;
import com.aurora.services.NpcService;
import com.aurora.models.player.Inventory;
import com.aurora.models.player.Player;
import com.aurora.services.Service;
import com.aurora.utils.Util;
import com.aurora.network.io.Message;
import com.aurora.services.ItemService;
import com.aurora.services.PlayerService;
import com.aurora.services.InventoryServiceNew;
import com.aurora.utils.Logger;
import java.util.List;

/**
 *
 * @author 💖 Trần Lại 💖
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SummonDragon {

    public static final byte WISHED = 0;
    public static final byte TIME_UP = 1;

    public static final byte DRAGON_SHENRON = 0;
    public static final byte DRAGON_PORUNGA = 1;

    public static final short NGOC_RONG_1_SAO = 14;
    public static final short NGOC_RONG_2_SAO = 15;
    public static final short NGOC_RONG_3_SAO = 16;
    public static final short NGOC_RONG_4_SAO = 17;
    public static final short NGOC_RONG_5_SAO = 18;
    public static final short NGOC_RONG_6_SAO = 19;
    public static final short NGOC_RONG_7_SAO = 20;

    public static final String SUMMON_SHENRON_TUTORIAL
            = "Có 3 cách gọi rồng thần. Gọi từ ngọc 1 sao, gọi từ ngọc 2 sao, hoặc gọi từ ngọc 3 sao\n"
            + "Các ngọc 4 sao đến 7 sao không thể gọi rồng thần được\n"
            + "Để gọi rồng 1 sao cần ngọc từ 1 sao đến 7 sao\n"
            + "Để gọi rồng 2 sao cần ngọc từ 2 sao đến 7 sao\n"
            + "Để gọi rồng 3 sao cần ngọc từ 3 sao đến 7sao\n"
            + "Điều ước rồng 3 sao: Capsule 3 sao, hoặc 2 triệu sức mạnh, hoặc 200k vàng\n"
            + "Điều ước rồng 2 sao: Capsule 2 sao, hoặc 20 triệu sức mạnh, hoặc 2 triệu vàng\n"
            + "Điều ước rồng 1 sao: Capsule 1 sao, hoặc 200 triệu sức mạnh, hoặc 20 triệu vàng, hoặc đẹp trai, hoặc....\n"
            + "Ngọc rồng sẽ mất ngay khi gọi rồng dù bạn có ước hay không\n"
            + "Quá 5 phút nếu không ước rồng thần sẽ bay mất";
    public static final String SHENRON_SAY
            = "Ta sẽ ban cho người 1 điều ước, ngươi có 5 phút, hãy suy nghĩ thật kỹ trước khi quyết định";

    public static final String[] SHENRON_1_STAR_WISHES_1
            = new String[]{"Giàu có\n+2 Tỏi\nVàng", "Găng tay\nđang mang\nlên 1 cấp", "Chí mạng\nGốc +2%",
                "Thay\nChiêu 2-3\nĐệ tử", "Điều ước\nkhác"};
    public static final String[] SHENRON_1_STAR_WISHES_2
            = new String[]{"Đẹp trai\nnhất\nVũ trụ", "Giàu có\n+10K\nNgọc", "+200 Tr\nSức mạnh\nvà tiềm\nnăng",
                "Găng tay đệ\nđang mang\nlên 1 cấp",
                "Điều ước\nkhác"};
    public static final String[] SHENRON_2_STARS_WHISHES
            = new String[]{"Giàu có\n+2K\nNgọc", "+20 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+200 Tr\nVàng"};
    public static final String[] SHENRON_3_STARS_WHISHES
            = new String[]{"Giàu có\n+200\nNgọc", "+2 Tr\nSức mạnh\nvà tiềm năng", "Giàu có\n+20 Tr\nVàng"};
    //--------------------------------------------------------------------------
    private static SummonDragon instance;
    private final Map pl_dragonStar;
    private long lastTimeShenronAppeared;
    private long lastTimeShenronWait;
//    private final int timeResummonShenron = 300000;
    private final int timeResummonShenron = 0;
    private boolean isShenronAppear;
    private final int timeShenronWait = 300000;

    private final Thread update;
    private boolean active;

    public boolean isPlayerDisconnect;
    public Player playerSummonShenron;
    private int playerSummonShenronId;
    private Zone mapShenronAppear;
    private byte shenronStar;
    private int menuShenron;
    private byte select;

    private SummonDragon() {
        this.pl_dragonStar = new HashMap<>();
        this.update = new Thread(() -> {
            while (active) {
                try {
                    if (isShenronAppear) {
                        if (isPlayerDisconnect) {

                            List<Player> players = mapShenronAppear.getPlayers();
                                for (Player plMap : players) {
                                    if (plMap.id == playerSummonShenronId) {
                                        playerSummonShenron = plMap;
                                        reSummonShenron();
                                        isPlayerDisconnect = false;
                                        break;
                                    }
                                }
                            
                        }
                        if (Util.canDoWithTime(lastTimeShenronWait, timeShenronWait)) {
                            shenronLeave(playerSummonShenron, TIME_UP);
                        }
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Logger.logException(SummonDragon.class, e);
                }
            }
        });
        this.active();
    }

    private void active() {
        if (!active) {
            active = true;
            this.update.start();
        }
    }

    public static SummonDragon gI() {
        if (instance == null) {
            instance = new SummonDragon();
        }
        return instance;
    }

    public void openMenuSummonShenron(Player pl, byte dragonBallStar) {
        this.pl_dragonStar.put(pl, dragonBallStar);
        NpcService.gI().createMenuConMeo(pl, ConstNpc.SUMMON_SHENRON, -1, "Bạn muốn gọi rồng thần ?",
                "Hướng\ndẫn thêm\n(mới)", "Gọi\nRồng Thần\n" + dragonBallStar + " Sao");
    }

    public void summonShenron(Player pl) {
        if (pl.zone.map.mapId == 0 || pl.zone.map.mapId == 7 || pl.zone.map.mapId == 14) {
            if (checkShenronBall(pl)) {
                if (isShenronAppear) {
                    Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    return;
                }

                if (Util.canDoWithTime(lastTimeShenronAppeared, timeResummonShenron)) {
                    //gọi rồng
                    playerSummonShenron = pl;
                    playerSummonShenronId = (int) pl.id;
                    mapShenronAppear = pl.zone;
                    byte dragonStar = (byte) pl_dragonStar.get(playerSummonShenron);
                    int begin = NGOC_RONG_1_SAO;
                    switch (dragonStar) {
                        case 2:
                            begin = NGOC_RONG_2_SAO;
                            break;
                        case 3:
                            begin = NGOC_RONG_3_SAO;
                            break;
                    }
                    for (int i = begin; i <= NGOC_RONG_7_SAO; i++) {
                        try {
                            InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, i), 1);
                        } catch (Exception ex) {
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(pl);
                    sendNotifyShenronAppear();
                    activeShenron(pl, true);
                    sendWhishesShenron(pl);
                } else {
                    int timeLeft = (int) ((timeResummonShenron - (System.currentTimeMillis() - lastTimeShenronAppeared)) / 1000);
                    Service.getInstance().sendThongBao(pl, "Vui lòng đợi " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa");
                }
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Chỉ được gọi rồng thần ở ngôi làng trước nhà");
        }
    }

    private void reSummonShenron() {
        activeShenron(playerSummonShenron, true);
        sendWhishesShenron(playerSummonShenron);
    }

    private void sendWhishesShenron(Player pl) {
        byte dragonStar;
        try {
            dragonStar = (byte) pl_dragonStar.get(pl);
            this.shenronStar = dragonStar;
        } catch (Exception e) {
            dragonStar = this.shenronStar;
        }
        switch (dragonStar) {
            case 1:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case 2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case 3:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
        }
    }

    private void activeShenron(Player pl, boolean appear) {
        Message msg;
        try {
            msg = new Message(-83);
            msg.writer().writeByte(appear ? 0 : (byte) 1);
            if (appear) {
                msg.writer().writeShort(pl.zone.map.mapId);
                msg.writer().writeShort(pl.zone.map.bgId);
                msg.writer().writeByte(pl.zone.zoneId);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeUTF("");
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                msg.writer().writeByte(DRAGON_SHENRON);
                lastTimeShenronWait = System.currentTimeMillis();
                isShenronAppear = true;
            }
            Service.getInstance().sendMessAllPlayer(msg);
        } catch (Exception e) {
        }
    }

    private boolean checkShenronBall(Player pl) {
        byte dragonStar = (byte) this.pl_dragonStar.get(pl);
        if (dragonStar == 1) {
            if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_2_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 2 sao");
                return false;
            }
            if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_3_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                return false;
            }
        } else if (dragonStar == 2) {
            if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_3_SAO)) {
                Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 3 sao");
                return false;
            }
        }
        if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_4_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 4 sao");
            return false;
        }
        if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_5_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 5 sao");
            return false;
        }
        if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_6_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 6 sao");
            return false;
        }
        if (!InventoryServiceNew.gI().isExistItemBag(pl, NGOC_RONG_7_SAO)) {
            Service.getInstance().sendThongBao(pl, "Bạn còn thiếu 1 viên ngọc rồng 7 sao");
            return false;
        }
        return true;
    }

    private void sendNotifyShenronAppear() {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(playerSummonShenron.name + " vừa gọi rồng thần tại "
                    + playerSummonShenron.zone.map.mapName + " khu vực " + playerSummonShenron.zone.zoneId);
            Service.getInstance().sendMessAllPlayerIgnoreMe(playerSummonShenron, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void confirmWish() {
        switch (this.menuShenron) {
            case ConstNpc.SHENRON_1_1:
                switch (this.select) {
                    case 0: //20 tr vàng
                        this.playerSummonShenron.inventory.gold = 2000000000;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //găng tay đang đeo lên 1 cấp
                        Item item = this.playerSummonShenron.inventory.itemsBody.get(2);
                        if (item.isNotNullItem()) {
                            int level = 0;
                            for (ItemOption io : item.itemOptions) {
                                if (io.optionTemplate.id == 72) {
                                    level = io.param;
                                    if (level < 7) {
                                        io.param++;
                                    }
                                    break;
                                }
                            }
                            if (level < 7) {
                                if (level == 0) {
                                    item.itemOptions.add(new ItemOption(72, 1));
                                }
                                for (ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id == 0) {
                                        io.param += (io.param * 10 / 100);
                                        break;
                                    }
                                }
                                InventoryServiceNew.gI().sendItemBody(playerSummonShenron);
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Găng tay của ngươi đã đạt cấp tối đa");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi hiện tại có đeo găng đâu");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                    case 2: //chí mạng +2%
                        if (this.playerSummonShenron.nPoint.critg < 9) {
                            this.playerSummonShenron.nPoint.critg += 2;
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                    case 3: //thay chiêu 2-3 đệ tử
                        if (playerSummonShenron.pet != null) {
                            if (playerSummonShenron.pet.playerSkill.skills.get(1).skillId != -1) {
                                playerSummonShenron.pet.openSkill2();
                                if (playerSummonShenron.pet.playerSkill.skills.get(2).skillId != -1) {
                                    playerSummonShenron.pet.openSkill3();
                                }
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi làm gì có đệ tử?");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                }
                break;
            case ConstNpc.SHENRON_1_2:
                switch (this.select) {
                    case 0: //đẹp trai nhất vũ trụ
                        if (InventoryServiceNew.gI().getCountEmptyBag(playerSummonShenron) > 0) {
                            byte gender = this.playerSummonShenron.gender;
                            Item avtVip = ItemService.gI().createNewItem((short) (gender == ConstPlayer.TRAI_DAT ? 227
                                    : gender == ConstPlayer.NAMEC ? 228 : 229));
                            avtVip.itemOptions.add(new ItemOption(97, Util.nextInt(5, 10)));
                            avtVip.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                            InventoryServiceNew.gI().addItemBag(playerSummonShenron, avtVip);
                            InventoryServiceNew.gI().sendItemBags(playerSummonShenron);
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Hành trang đã đầy");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                    case 1: //+1,5 ngọc
                        this.playerSummonShenron.inventory.gem += 10000;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 2: //+200 tr smtn
                        if (this.playerSummonShenron.nPoint.power >= 2000000) {
                            Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 200000000, false);
//                        } else {
//                            Service.getInstance().sendThongBao(playerSummonShenron, "Xin lỗi, điều ước này khó quá, ta không thể thực hiện.");
//                            reOpenShenronWishes(playerSummonShenron);
//                            return;
                        }
                        break;
                    case 3: //găng tay đệ lên 1 cấp
                        if (this.playerSummonShenron.pet != null) {
                            Item item = this.playerSummonShenron.pet.inventory.itemsBody.get(2);
                            if (item.isNotNullItem()) {
                                int level = 0;
                                for (ItemOption io : item.itemOptions) {
                                    if (io.optionTemplate.id == 72) {
                                        level = io.param;
                                        if (level < 7) {
                                            io.param++;
                                        }
                                        break;
                                    }
                                }
                                if (level < 7) {
                                    if (level == 0) {
                                        item.itemOptions.add(new ItemOption(72, 1));
                                    }
                                    for (ItemOption io : item.itemOptions) {
                                        if (io.optionTemplate.id == 0) {
                                            io.param += (io.param * 10 / 100);
                                            break;
                                        }
                                    }
                                    Service.getInstance().point(playerSummonShenron);
                                } else {
                                    Service.getInstance().sendThongBao(playerSummonShenron, "Găng tay của đệ ngươi đã đạt cấp tối đa");
                                    reOpenShenronWishes(playerSummonShenron);
                                    return;
                                }
                            } else {
                                Service.getInstance().sendThongBao(playerSummonShenron, "Đệ ngươi hiện tại có đeo găng đâu");
                                reOpenShenronWishes(playerSummonShenron);
                                return;
                            }
                        } else {
                            Service.getInstance().sendThongBao(playerSummonShenron, "Ngươi đâu có đệ tử");
                            reOpenShenronWishes(playerSummonShenron);
                            return;
                        }
                        break;
                }
                break;
            case ConstNpc.SHENRON_2:
                switch (this.select) {
                    case 0: //+150 ngọc
                        this.playerSummonShenron.inventory.gem += 2000;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //+20 tr smtn
                        Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 20000000, false);
                        break;
                    case 2: //2 tr vàng
                        if (this.playerSummonShenron.inventory.gold > 1800000000) {
                            this.playerSummonShenron.inventory.gold = Inventory.LIMIT_GOLD;
                        } else {
                            this.playerSummonShenron.inventory.gold += 200000000;
                        }
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                }
                break;
            case ConstNpc.SHENRON_3:
                switch (this.select) {
                    case 0: //+15 ngọc
                        this.playerSummonShenron.inventory.gem += 200;
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                    case 1: //+2 tr smtn
                        Service.getInstance().addSMTN(this.playerSummonShenron, (byte) 2, 2000000, false);
                        break;
                    case 2: //200k vàng
                        if (this.playerSummonShenron.inventory.gold > (2000000000 - 20000000)) {
                            this.playerSummonShenron.inventory.gold = Inventory.LIMIT_GOLD;
                        } else {
                            this.playerSummonShenron.inventory.gold += 20000000;
                        }
                        PlayerService.gI().sendInfoHpMpMoney(this.playerSummonShenron);
                        break;
                }
                break;
        }
        shenronLeave(this.playerSummonShenron, WISHED);
    }

    public void showConfirmShenron(Player pl, int menu, byte select) {
        this.menuShenron = menu;
        this.select = select;
        String wish = null;
        switch (menu) {
            case ConstNpc.SHENRON_1_1:
                wish = SHENRON_1_STAR_WISHES_1[select];
                break;
            case ConstNpc.SHENRON_1_2:
                wish = SHENRON_1_STAR_WISHES_2[select];
                break;
            case ConstNpc.SHENRON_2:
                wish = SHENRON_2_STARS_WHISHES[select];
                break;
            case ConstNpc.SHENRON_3:
                wish = SHENRON_3_STARS_WHISHES[select];
                break;
        }
        NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_CONFIRM, "Ngươi có chắc muốn ước?", wish, "Từ chối");
    }

    public void reOpenShenronWishes(Player pl) {
        switch (menuShenron) {
            case ConstNpc.SHENRON_1_1:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_1, SHENRON_SAY, SHENRON_1_STAR_WISHES_1);
                break;
            case ConstNpc.SHENRON_1_2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_1_2, SHENRON_SAY, SHENRON_1_STAR_WISHES_2);
                break;
            case ConstNpc.SHENRON_2:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_2, SHENRON_SAY, SHENRON_2_STARS_WHISHES);
                break;
            case ConstNpc.SHENRON_3:
                NpcService.gI().createMenuRongThieng(pl, ConstNpc.SHENRON_3, SHENRON_SAY, SHENRON_3_STARS_WHISHES);
                break;
        }
    }

    public void shenronLeave(Player pl, byte type) {
        if (type == WISHED) {
            NpcService.gI().createTutorial(pl, -1, "Điều ước của ngươi đã trở thành sự thật\nHẹn gặp ngươi lần sau, ta đi ngủ đây, bái bai");
        } else {
            NpcService.gI().createMenuRongThieng(pl, ConstNpc.IGNORE_MENU, "Ta buồn ngủ quá rồi\nHẹn gặp ngươi lần sau, ta đi đây, bái bai");
        }
        activeShenron(pl, false);
        this.isShenronAppear = false;
        this.menuShenron = -1;
        this.select = -1;
        this.playerSummonShenron = null;
        this.playerSummonShenronId = -1;
        this.shenronStar = -1;
        this.mapShenronAppear = null;
        lastTimeShenronAppeared = System.currentTimeMillis();
    }

    //--------------------------------------------------------------------------
}
