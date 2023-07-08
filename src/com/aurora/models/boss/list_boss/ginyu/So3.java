package com.aurora.models.boss.list_boss.ginyu;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossStatus;
import com.aurora.models.boss.BossesData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.player.Player;
import com.aurora.services.Service;
import com.aurora.services.TaskService;
import com.aurora.utils.Util;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class So3 extends Boss {

    public So3() throws Exception {
        super(BossID.SO_3, BossesData.SO_3);
    }


@Override
    public void reward(Player plKill) {
        int[] itemRan = new int[]{17,18,19,20,430};
        int itemId = itemRan[2];
        if (Util.isTrue(15, 100)) {
            ItemMap it = new ItemMap(this.zone, itemId, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.getInstance().dropItemMap(this.zone, it);
        }
        TaskService.gI().checkDoneTaskKillBoss(plKill, this);
    }
    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.parentBoss == null) {
            return;
        }
        for (Boss boss : this.parentBoss.bossAppearTogether[this.parentBoss.currentLevel]) {
            if (boss.id == BossID.SO_2 || boss.id == BossID.SO_1) {
                boss.changeToTypePK();
            }
        }
    }  @Override
    public void active() {
        super.active(); //To change body of generated methods, choose Tools | Templates.
        if(Util.canDoWithTime(st,90000)){
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public void joinMap() {
        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
        st= System.currentTimeMillis();
    }
    private long st;
}






















/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức.
 * Hãy tôn trọng tác giả của mã nguồn này.
 * Xin cảm ơn! - Girlkun75
 */
