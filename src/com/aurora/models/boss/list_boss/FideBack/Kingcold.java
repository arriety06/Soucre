/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurora.models.boss.list_boss.FideBack;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossesData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.player.Player;
import com.aurora.models.skill.Skill;
import com.aurora.services.PetService;
import com.aurora.services.Service;
import com.aurora.utils.Util;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class Kingcold extends Boss {

    public Kingcold() throws Exception {
        super(BossID.VUA_COLD, BossesData.VUA_COLD);
    }

      @Override
    public void reward(Player plKill) {
        int[] itemDos = new int[]{555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567};
        int[] NRs = new int[]{15, 16};
        int randomDo = new Random().nextInt(itemDos.length);
        int randomNR = new Random().nextInt(NRs.length);
        if (Util.isTrue(15, 100)) {
            if (Util.isTrue(1, 5)) {
                Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, 561, 1, this.location.x, this.location.y, plKill.id));
                return;
            }
            Service.getInstance().dropItemMap(this.zone, Util.ratiItem(zone, itemDos[randomDo], 1, this.location.x, this.location.y, plKill.id));
        } else {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(zone, NRs[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
    }
}
