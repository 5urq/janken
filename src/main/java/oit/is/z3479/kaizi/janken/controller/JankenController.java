package oit.is.z3479.kaizi.janken.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3479.kaizi.janken.model.Janken;

@Controller
public class JankenController {

  /**
   * Task 1 [AC2]: 直接 /janken にアクセスされた場合
   * ユーザ名は渡さず、janken.html の初期画面（リンクのみ）を表示する
   */
  @GetMapping("/janken")
  public String jankenGet() {
    return "janken.html";
  }

  /**
   * Task 1 [AC1]: index.html のフォームからユーザ名を入力して参加した場合
   * POST で送られた userName を ModelMap に格納して janken.html を表示する
   */
  @PostMapping("/janken")
  public String jankenPost(@RequestParam String userName, ModelMap model) {
    model.addAttribute("userName", userName);
    return "janken.html";
  }

  /**
   * Task 2 [AC]: グー・チョキ・パーのリンクをクリックした場合
   * クエリパラメータで手を受け取り、Janken モデルクラスで勝敗判定を行い、結果を渡す
   */
  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand,
      @RequestParam(required = false) String userName,
      ModelMap model) {
    // 1. じゃんけんモデルを生成（勝敗判定とCPU手決定）
    Janken janken = new Janken(hand);
    model.addAttribute("janken", janken);

    // 2. ユーザ名があれば引き継ぐ
    if (userName != null && !userName.isEmpty()) {
      model.addAttribute("userName", userName);
    }

    return "janken.html";
  }

}
