package com.aurora.models.boss.list_boss.doanh_trai;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.map.doanhtrai.DoanhTrai;
import com.aurora.models.player.Player;
import com.aurora.services.Service;
import com.aurora.utils.Util;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class BossDoanhTrai extends Boss {

    private DoanhTrai doanhTrai;
    private int xHp;
    private int xDame;

    public BossDoanhTrai(DoanhTrai doanhTrai, int xHp, int xDame, int id, BossData... data) throws Exception {
        super(id, data);
        this.doanhTrai = doanhTrai;
    }
    @Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{17,18,19,20};
        int itemId = itemRan[2];
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.getInstance().dropItemMap(this.zone, it);
        }
    }

    @Override
    public void initBase() {
        if (this.doanhTrai.getClan() == null) {
            return;
        }
        BossData data = this.data[this.currentLevel];
        this.name = String.format(data.getName(), Util.nextInt(0, 100));
        this.gender = data.getGender();
        this.nPoint.mpg = 7_5_2002;
        long totalDame = 0;
        long totalHp = 0;
        for (Player pl : this.doanhTrai.getClan().membersInGame) {
            totalDame += pl.nPoint.dame;
            totalHp += pl.nPoint.hpMax;
        }
        this.nPoint.hpg = (int) (totalDame * xHp);
        this.nPoint.dameg = (int) (totalHp / xDame);
        this.nPoint.calPoint();
        this.initSkill();
        this.resetBase();
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girlkun75
 */
