package jp.co.gawain.server.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.co.gawain.server.constans.GawainMessageConstants;
import jp.co.gawain.server.dao.UserDao;
import jp.co.gawain.server.dto.UserDto;
import jp.co.gawain.server.util.DatabaseManager;
import jp.co.gawain.server.util.Utility;

public class createUserMultiMain {
    private static Logger logger = LoggerFactory.getLogger(createUserMultiMain.class);
    // スレッドプールのサイズ
    private static final int THREAD_POOL_SIZE = Integer.parseInt(Utility.getProp("connection.pool.size"));
    // 一定件数毎にコミットする閾値
    private static final int COMMIT_THRESHOLD = Integer.parseInt(Utility.getProp("commit.threshold"));

    public static void main(String[] args) {
        List<UserDto> newUserList = new ArrayList<UserDto>();

        for (int i = 0; i < 20000; i++) {
            UserDto dto = new UserDto();

            String name = "test" + String.format("%05d", i);
            String email = "test" + String.format("%05d", i) + "@example.com";

            dto.setUserName(name);
            dto.setEMail(email);

            newUserList.add(dto);
        }

        int size = newUserList.size();

        try {
            // スレッドプールの作成（10スレッド）
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            UserDao dao = new UserDao();

            // コミット件数のカウンターを作成
            AtomicInteger commitCounter = new AtomicInteger(0);

            for (int i = 0; i < size; i++) {
                final int index = i;
                executorService.execute(() -> {
                    try {
                        DatabaseManager.begin();
                        logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_003);
                        dao.create(newUserList.get(index));
                        logger.info(GawainMessageConstants.APPLICATION_INFO_MESSAGE_004);

                        // コミット件数のカウンターをインクリメント
                        int count = commitCounter.incrementAndGet();

                        // コミット件数の倍数に達したらコミットを行う
                        // 一定件数ごとにコミットする
                        if (count % COMMIT_THRESHOLD == 0) {
                            DatabaseManager.commit();
                        }
                    } catch (Exception e) {
                        logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
                        DatabaseManager.rollback();
                    } finally {
                        DatabaseManager.commit();
                    }
                });
            }

            // スレッドプールのシャットダウン
            executorService.shutdown();

            // スレッドの終了を待つ（タイムアウトは1時間）
            if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
                // タイムアウトした場合、スレッドプールを強制終了
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            logger.error(GawainMessageConstants.APPLICATION_ERROR_MESSAGE_001, e);
        } finally {
            DatabaseManager.close();
            DatabaseManager.closeDatasSource();
        }
    }
}
