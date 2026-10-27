package oit.is.z3479.kaizi.janken.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.z3479.kaizi.janken.model.Match;
import oit.is.z3479.kaizi.janken.model.MatchMapper;

@Service
public class AsyncKekka {

  private final Logger logger = LoggerFactory.getLogger(AsyncKekka.class);

  @Autowired
  private MatchMapper matchMapper;

  public boolean dbUpdated = false;

  /**
   * matchesテーブルのisActive=trueのレコードを監視してSSEプッシュする
   */
  @Async
  public void asyncShowKekka(SseEmitter emitter) {
    logger.info("asyncShowKekka started");
    try {
      while (true) {
        TimeUnit.MILLISECONDS.sleep(500);

        Match activeMatch = matchMapper.selectActiveMatch();
        if (activeMatch != null) {
          logger.info("Active match found, sending: id=" + activeMatch.getId());
          emitter.send(activeMatch);

          // 送信後、二重処理を防ぐためにisActiveをFALSEに更新
          matchMapper.updateMatchActive(activeMatch.getId(), false);
          this.dbUpdated = false;
          break;
        }
      }
    } catch (InterruptedException e) {
      logger.warn("Interrupted in asyncShowKekka: " + e.getMessage());
    } catch (IOException e) {
      logger.warn("IOException in asyncShowKekka: " + e.getMessage());
    } finally {
      emitter.complete();
    }
  }

}
