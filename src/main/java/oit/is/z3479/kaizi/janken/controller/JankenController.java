package oit.is.z3479.kaizi.janken.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.z3479.kaizi.janken.model.Entry;
import oit.is.z3479.kaizi.janken.model.Janken;
import oit.is.z3479.kaizi.janken.model.Match;
import oit.is.z3479.kaizi.janken.model.MatchMapper;
import oit.is.z3479.kaizi.janken.model.User;
import oit.is.z3479.kaizi.janken.model.UserMapper;

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

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  /**
   * Task 1 & Task 2 & Task 3 & Task 4: /janken へのGETリクエスト
   * 認証済みユーザ名を取得し、DBから全ユーザ一覧および試合結果一覧を取得してModelMapに渡す
   */
  @GetMapping("/janken")
  public String jankenGet(Principal prin, ModelMap model) {
    if (prin != null) {
      String loginUser = prin.getName();
      this.entry.addUser(loginUser);
      model.addAttribute("userName", loginUser);
    }
    model.addAttribute("entry", this.entry);

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

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

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    return "janken.html";
  }

  /**
   * Task 5: 相手ユーザのidを受け取り試合画面へ移動する
   */
  @GetMapping("/match")
  public String match(@RequestParam int id, Principal prin, ModelMap model) {
    if (prin != null) {
      String loginUser = prin.getName();
      model.addAttribute("myUser", loginUser);
    }
    User oppUser = userMapper.selectById(id);
    model.addAttribute("oppUser", oppUser);
    return "match.html";
  }

  /**
   * Task 6: じゃんけんの試合を行い、結果をDBに登録してmatch.htmlに表示する
   */
  @GetMapping("/fight")
  @Transactional
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    String loginUser = (prin != null) ? prin.getName() : "ほんだ";
    model.addAttribute("myUser", loginUser);

    User myUserInfo = userMapper.selectByName(loginUser);
    User oppUser = userMapper.selectById(id);
    model.addAttribute("oppUser", oppUser);

    // CPUの手（仕様上、固定の手でよい）
    String cpuHand = "Gu";

    // 試合情報をDBに登録
    Match match = new Match();
    if (myUserInfo != null) {
      match.setUser1(myUserInfo.getId());
    } else {
      match.setUser1(2);
    }
    match.setUser2(id);
    match.setUser1Hand(hand);
    match.setUser2Hand(cpuHand);
    match.setActive(false);
    matchMapper.insertMatch(match);

    // 勝敗判定
    String result = "";
    if (hand.equals(cpuHand)) {
      result = "Draw";
    } else if ((hand.equals("Gu") && cpuHand.equals("Choki"))
        || (hand.equals("Choki") && cpuHand.equals("Pa"))
        || (hand.equals("Pa") && cpuHand.equals("Gu"))) {
      result = "You Win!";
    } else {
      result = "CPU Win!";
    }

    model.addAttribute("match", match);
    model.addAttribute("myHand", hand);
    model.addAttribute("cpuHand", cpuHand);
    model.addAttribute("result", result);

    return "match.html";
  }

}
