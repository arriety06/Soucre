/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurora.models.boss.list_boss;
import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossStatus;
import com.aurora.models.boss.BossesData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.player.Player;
import com.aurora.models.skill.Skill;
import com.aurora.services.Service;
import com.aurora.services.TaskService;
import com.aurora.utils.Util;
import java.util.Random;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class Bubbles extends Boss{
   public Bubbles() throws Exception {
        super(BossID.BUBBLES, BossesData.BUBBLES);
    }
     @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, 16, 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
        Service.getInstance().dropItemMap(this.zone, it);
        }
    }
}
