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
import oit.is.z3479.kaizi.janken.model.MatchInfo;
import oit.is.z3479.kaizi.janken.model.MatchInfoMapper;
import oit.is.z3479.kaizi.janken.model.MatchMapper;
import oit.is.z3479.kaizi.janken.model.User;
import oit.is.z3479.kaizi.janken.model.UserMapper;

@Controller
public class JankenController {

  @Autowired
  private Entry entry;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private MatchInfoMapper matchInfoMapper;

  /**
   * /janken へのGETリクエスト
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

    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectAllActiveMatches();
    model.addAttribute("activeMatches", activeMatches);

    return "janken.html";
  }

  @PostMapping("/janken")
  public String jankenPost(@RequestParam String userName, ModelMap model) {
    if (userName != null && !userName.isEmpty()) {
      this.entry.addUser(userName);
      model.addAttribute("userName", userName);
    }
    model.addAttribute("entry", this.entry);
    return "janken.html";
  }

  @GetMapping("/jankengame")
  public String jankenGame(@RequestParam String hand,
      @RequestParam(required = false) String userName,
      Principal prin,
      ModelMap model) {
    if (prin != null) {
      String loginUser = prin.getName();
      this.entry.addUser(loginUser);
      model.addAttribute("userName", loginUser);
    } else if (userName != null && !userName.isEmpty()) {
      model.addAttribute("userName", userName);
    }
    model.addAttribute("entry", this.entry);

    Janken janken = new Janken(hand);
    model.addAttribute("janken", janken);

    ArrayList<User> users = userMapper.selectAllUsers();
    model.addAttribute("users", users);

    ArrayList<Match> matches = matchMapper.selectAllMatches();
    model.addAttribute("matches", matches);

    ArrayList<MatchInfo> activeMatches = matchInfoMapper.selectAllActiveMatches();
    model.addAttribute("activeMatches", activeMatches);

    return "janken.html";
  }

  /**
   * 相手ユーザのidを受け取り試合画面へ移動する
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
   * task2: 相手を選び手を選んで wait.html へ遷移する（matchinfo に登録）
   */
  @GetMapping("/wait")
  @Transactional
  public String waitPage(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    String loginUser = (prin != null) ? prin.getName() : "ほんだ";
    model.addAttribute("myUser", loginUser);

    User myUserInfo = userMapper.selectByName(loginUser);
    int myUserId = (myUserInfo != null) ? myUserInfo.getId() : 2;

    MatchInfo matchInfo = new MatchInfo();
    matchInfo.setUser1(myUserId);
    matchInfo.setUser2(id);
    matchInfo.setUser1Hand(hand);
    matchInfo.setActive(true);
    matchInfoMapper.insertMatchInfo(matchInfo);

    return "wait.html";
  }

  /**
   * 旧来の対戦処理（互換性用）
   */
  @GetMapping("/fight")
  @Transactional
  public String fight(@RequestParam int id, @RequestParam String hand, Principal prin, ModelMap model) {
    String loginUser = (prin != null) ? prin.getName() : "ほんだ";
    model.addAttribute("myUser", loginUser);

    User myUserInfo = userMapper.selectByName(loginUser);
    User oppUser = userMapper.selectById(id);
    model.addAttribute("oppUser", oppUser);

    String cpuHand = "Gu";

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
