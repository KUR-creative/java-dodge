/*
 * 사용자 한 사람의 점수를 저장하는 클래스이다. 
 * 단순 자료구조에 가까운 클래스이므로 모두 public으로 설정한다.
 */
package dodge;

import java.io.Serializable;

class User implements Serializable {
    public String name;
    public int time;
    public int score;
    public int result;
    public User(String name, int time, int score, int result) {
        this.name = name;
        this.time = time;
        this.score = score;
        this.result = result;
    }
}	
