package com.aurora.models.boss.iboss;

import com.aurora.models.boss.BossStatus;
import com.aurora.models.player.Player;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public interface IBossNew {

    void update();

    void initBase();

    void changeStatus(BossStatus status);

    Player getPlayerAttack();

    void changeToTypePK();

    void changeToTypeNonPK();
    
    void moveToPlayer(Player player);
    
    void moveTo(int x, int y);
    
    void checkPlayerDie(Player player);
    
    void wakeupAnotherBossWhenAppear();
    
    void wakeupAnotherBossWhenDisappear();
    
    void reward(Player plKill);
    
    void attack();

    //loop
    void rest();

    void respawn();

    void joinMap();

    boolean chatS();
    
    void doneChatS();

    void active();
    
    void chatM();

    void die(Player plKill);

    boolean chatE();
    
    void doneChatE();

    void leaveMap();
    //end loop
}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girlkun75
 */
