/*
 * 사용자들의 점수를 저장하고 관리한다.
 * 점수리스트의 파일 IO 또한 관리한다.
 */
package dodge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class ScoreManager {
    private File dataFile = new File("Scores.bin");
    private File txtOutFile = new File("Scores.txt");	
    private LinkedList<User> userList = new LinkedList<>();

    
    public int getExpectedGrade(int result){
        int grade = 0;
        int i;
        for(i = 0; i < userList.size(); i++){
            if(result > userList.get(i).result){
                break;
            }			
        }
        grade = i+1;
        return grade;
    }
    
    public String getExpectedGradeStr(int result){
        int grade = getExpectedGrade(result);
                
        String suffix = null;
        switch(grade % 10){
        case 1:
            suffix = "st";
            break;
        case 2:
            suffix = "nd";
            break;
        case 3:
            suffix = "rd";
            break;
        default:
            suffix = "th";
            break;				
        }
        
        String ret = grade + suffix + " place";
        return ret;
    }
    
    public void saveResult(String name, int time, int score, int result){
        User tmpUser = new User(name, time, score, result);
        addUserDescendingSorted(tmpUser);
        
        System.out.println("----------------------------------------------");
        int i = 0;
        for(User u : userList){
            i++;
            System.out.println(i + ": " + u.name + "," + u.time + "," + u.score + "," + u.result);
        }
    }
    
    private void addUserDescendingSorted(User user){
        for(int i = 0; i < userList.size(); i++){
            if(user.result > userList.get(i).result){
                userList.add(i, user);
                return;
            }			
        }
        userList.add(user);
    }
    
    public void clearUserList(){
        userList.clear();
    }
    
    public void loadUserList(){
        try{
            if(dataFile.exists()){
                FileInputStream fis = new FileInputStream(dataFile); 
                ObjectInputStream ois = new ObjectInputStream(fis); 
                userList = (LinkedList<User>)ois.readObject(); 
                fis.close();
            }
        } 
    catch(Throwable e) 
    {
        System.err.println(e); 
    } 		
    }	
    public void saveUserList(){
        try{ 
            //.bin 이진파일 아카이브
            FileOutputStream bfos = new FileOutputStream(dataFile); 
            ObjectOutputStream boos = new ObjectOutputStream(bfos); 
        boos.writeObject(userList); 
        boos.flush(); 
        bfos.close(); 
        
        //.txt 등수 결과 출력 
        PrintStream tout = new PrintStream(new FileOutputStream(txtOutFile));
        tout.print(printUserList());
        tout.close(); 			   
      } 
      catch(Throwable e)  
      { 
          System.err.println(e); 
      }    
    }		
    
    public String printUserList(){
        String nl = System.getProperty("line.separator");
        
        StringBuffer content 
            = new StringBuffer("user name\tresult\ttime\tscores\t" + nl);
        content.append("-------------------------------------------------------" + nl);
        for(User u : userList){
            content.append(u.name + " \t\t " + u.result + " \t " + u.time + " \t " + u.score + nl);
        }
        
        return content.toString();
    }
}
