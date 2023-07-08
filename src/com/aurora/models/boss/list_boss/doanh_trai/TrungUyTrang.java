package com.aurora.models.boss.list_boss.doanh_trai;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossesData;
import com.aurora.models.map.ItemMap;
import com.aurora.models.player.Player;
import com.aurora.services.Service;
import com.aurora.utils.Util;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class TrungUyTrang extends Boss{

    public TrungUyTrang() throws Exception{
        super(BossID.TRUNG_UY_TRANG, BossesData.TRUNG_UY_TRANG);
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
    
}






















/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức.
 * Hãy tôn trọng tác giả của mã nguồn này.
 * Xin cảm ơn! - Girlkun75
 */