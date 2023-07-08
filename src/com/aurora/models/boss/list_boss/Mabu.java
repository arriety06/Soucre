/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurora.models.boss.list_boss;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossesData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.player.Player;
import com.aurora.services.Service;
import com.aurora.utils.Util;

/**
 *
 * @author Administrator
 */
public class Mabu extends Boss {

    public Mabu() throws Exception {
        super(BossID.MABU, BossesData.MABU);
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 568, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.getInstance().dropItemMap(this.zone, it);
        }
    }

//    @Override
//    public void active() {
//        super.active(); //To change body of generated methods, choose Tools | Templates.
//        if(Util.canDoWithTime(st,900000)){
//            this.changeStatus(BossStatus.LEAVE_MAP);
//        }
//    }
//
//    @Override
//    public void joinMap() {
//        super.joinMap(); //To change body of generated methods, choose Tools | Templates.
//        st= System.currentTimeMillis();
//    }
//    private long st;
}
