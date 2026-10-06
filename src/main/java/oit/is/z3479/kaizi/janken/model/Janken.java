package oit.is.z3479.kaizi.janken.model;

import java.util.Random;

/**
 * Janken モデルクラス
 * じゃんけんの手の保持、CPUの手の決定、勝敗判定ロジックを担当する（ビジネスロジック）
 */
public class Janken {
  private String myHand;
  private String cpuHand;
  private String result;

  /**
   * コンストラクタ
   * 
   * @param hand ユーザが出した手（"Gu", "Choki", "Pa" または "グー", "チョキ", "パー"）
   */
  public Janken(String hand) {
    // 1. ユーザの手の表記を日本語に統一
    if ("Gu".equalsIgnoreCase(hand) || "グー".equals(hand)) {
      this.myHand = "グー";
    } else if ("Choki".equalsIgnoreCase(hand) || "チョキ".equals(hand)) {
      this.myHand = "チョキ";
    } else if ("Pa".equalsIgnoreCase(hand) || "パー".equals(hand)) {
      this.myHand = "パー";
    } else {
      this.myHand = hand;
    }

    // 2. CPUの手をランダムに決定
    String[] hands = { "グー", "チョキ", "パー" };
    Random random = new Random();
    this.cpuHand = hands[random.nextInt(3)];

    // 3. 勝敗判定
    if (this.myHand.equals(this.cpuHand)) {
      this.result = "あいこ";
    } else if ((this.myHand.equals("グー") && this.cpuHand.equals("チョキ")) ||
        (this.myHand.equals("チョキ") && this.cpuHand.equals("パー")) ||
        (this.myHand.equals("パー") && this.cpuHand.equals("グー"))) {
      this.result = "勝ち";
    } else {
      this.result = "負け";
    }
  }

  // --- Getter / Setter ---

  public String getMyHand() {
    return myHand;
  }

  public void setMyHand(String myHand) {
    this.myHand = myHand;
  }

  public String getCpuHand() {
    return cpuHand;
  }

  public void setCpuHand(String cpuHand) {
    this.cpuHand = cpuHand;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
