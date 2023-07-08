package com.aurora.models.boss.list_boss.nappa;

import com.aurora.models.boss.Boss;
import com.aurora.models.boss.BossID;
import com.aurora.models.boss.BossStatus;
import com.aurora.models.boss.BossesData;
import com.aurora.utils.Util;

/**
 *
 * @author Administrator
 */
public class MapDauDinh extends Boss {

    public MapDauDinh() throws Exception {
        super(BossID.MAP_DAU_DINH, BossesData.MAP_DAU_DINH);
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
