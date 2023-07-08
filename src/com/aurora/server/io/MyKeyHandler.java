package com.aurora.server.io;

import com.aurora.data.DataGame;
import com.aurora.network.session.ISession;
import com.aurora.network.example.KeyHandler;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class MyKeyHandler extends KeyHandler {

    @Override
    public void sendKey(ISession session) {
        super.sendKey(session);
        DataGame.sendVersionRes((MySession) session);
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girlkun75
 */
