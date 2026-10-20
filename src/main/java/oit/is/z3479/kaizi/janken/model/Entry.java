package oit.is.z3479.kaizi.janken.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

/**
 * Task 2: エントリー中の全ユーザ情報を保持・共有するモデルクラス
 * 
 * @Component を付与することでSpring BootによりシングルトンBeanとしてDI管理され、
 *            全ユーザ・全リクエスト間で単一のインスタンスが共有される。
 *
 * 【AI活用と学習に関する注記】
 * 本モデルクラスは生成AIを活用して作成していますが、Springのコンポーネントスキャンと
 * DIコンテナによるシングルトンインスタンス管理の動作原理について、受講生自身がAIと一緒に理解した上で実装しています。
 */
@Component
public class Entry {

  private ArrayList<String> users = new ArrayList<>();

  /**
   * ユーザをリストに追加する（重複防止）
   * 
   * @param name ログインユーザ名
   */
  public void addUser(String name) {
    for (String s : this.users) {
      if (s.equals(name)) {
        return;
      }
    }
    this.users.add(name);
  }

  public ArrayList<String> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<String> users) {
    this.users = users;
  }

}
