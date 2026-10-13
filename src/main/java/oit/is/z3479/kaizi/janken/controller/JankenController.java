package oit.is.z3479.kaizi.janken.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3479.kaizi.janken.model.Entry;
import oit.is.z3479.kaizi.janken.model.Janken;

/**
 * じゃんけんアプリケーションのコントローラクラス
 *
 * 【AI活用と学習に関する注記】
 * 本機能の実装コードは生成AIを活用して作成していますが、解説ドキュメントの作成および対話を通じて、
 * 認証・認可の仕組み（Spring Security）やDI（依存性注入）によるシングルトンオブジェクトの共有機構について、
 * 受講生自身がAIと一緒に動作原理を理解した上で組み込んでいます。
 */
@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  /**
   * Task 1 & Task 2: /janken へのGETリクエスト
   * 認証済みユーザ名を取得し、Entry に追加して共有オブジェクトとしてModelMapに渡す
   */
  @GetMapping("/janken")
  public String jankenGet(Principal prin, ModelMap model) {
    if (prin != null) {
      String loginUser = prin.getName();
      this.entry.addUser(loginUser);
      model.addAttribute("userName", loginUser);
    }
    model.addAttribute("entry", this.entry);
    return "janken.html";
  }

  /**
   * 前回のPOSTフォーム対応（残しておくことで互換性を維持）
   */
  @PostMapping("/janken")
  public String jankenPost(@RequestParam String userName, ModelMap model) {
    if (userName != null && !userName.isEmpty()) {
      this.entry.addUser(userName);
      model.addAttribute("userName", userName);
    }
    model.addAttribute("entry", this.entry);
    return "janken.html";
  }

  /**
   * じゃんけんの手を選択した際のGETリクエスト
   */
  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand,
      @RequestParam(required = false) String userName,
      Principal prin,
      ModelMap model) {
    // ログインユーザ情報の保持・エントリー追加
    if (prin != null) {
      String loginUser = prin.getName();
      this.entry.addUser(loginUser);
      model.addAttribute("userName", loginUser);
    } else if (userName != null && !userName.isEmpty()) {
      model.addAttribute("userName", userName);
    }
    model.addAttribute("entry", this.entry);

    // じゃんけんの勝敗判定
    Janken janken = new Janken(hand);
    model.addAttribute("janken", janken);

    return "janken.html";
  }

}
