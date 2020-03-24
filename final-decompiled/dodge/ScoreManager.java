// 
// Decompiled by Procyon v0.5.36
// 

package dodge;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.io.File;

public class ScoreManager
{
    private File dataFile;
    private File txtOutFile;
    private LinkedList<User> userList;
    
    public ScoreManager() {
        this.dataFile = new File("Scores.bin");
        this.txtOutFile = new File("Scores.txt");
        this.userList = new LinkedList<User>();
    }
    
    public int getExpectedGrade(final int result) {
        int grade = 0;
        int i;
        for (i = 0; i < this.userList.size() && result <= this.userList.get(i).result; ++i) {}
        grade = i + 1;
        return grade;
    }
    
    public String getExpectedGradeStr(final int result) {
        final int grade = this.getExpectedGrade(result);
        String suffix = null;
        switch (grade % 10) {
            case 1: {
                suffix = "st";
                break;
            }
            case 2: {
                suffix = "nd";
                break;
            }
            case 3: {
                suffix = "rd";
                break;
            }
            default: {
                suffix = "th";
                break;
            }
        }
        final String ret = String.valueOf(grade) + suffix + " place";
        return ret;
    }
    
    public void saveResult(final String name, final int time, final int score, final int result) {
        final User tmpUser = new User(name, time, score, result);
        this.addUserDescendingSorted(tmpUser);
        System.out.println("----------------------------------------------");
        int i = 0;
        for (final User u : this.userList) {
            ++i;
            System.out.println(String.valueOf(i) + ": " + u.name + "," + u.time + "," + u.score + "," + u.result);
        }
    }
    
    private void addUserDescendingSorted(final User user) {
        for (int i = 0; i < this.userList.size(); ++i) {
            if (user.result > this.userList.get(i).result) {
                this.userList.add(i, user);
                return;
            }
        }
        this.userList.add(user);
    }
    
    public void clearUserList() {
        this.userList.clear();
    }
    
    public void loadUserList() {
        try {
            if (this.dataFile.exists()) {
                final FileInputStream fis = new FileInputStream(this.dataFile);
                final ObjectInputStream ois = new ObjectInputStream(fis);
                this.userList = (LinkedList<User>)ois.readObject();
                fis.close();
            }
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }
    
    public void saveUserList() {
        try {
            final FileOutputStream bfos = new FileOutputStream(this.dataFile);
            final ObjectOutputStream boos = new ObjectOutputStream(bfos);
            boos.writeObject(this.userList);
            boos.flush();
            bfos.close();
            final PrintStream tout = new PrintStream(new FileOutputStream(this.txtOutFile));
            tout.print(this.printUserList());
            tout.close();
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }
    
    public String printUserList() {
        final String nl = System.getProperty("line.separator");
        final StringBuffer content = new StringBuffer("user name\tresult\ttime\tscores\t" + nl);
        content.append("-------------------------------------------------------" + nl);
        for (final User u : this.userList) {
            content.append(String.valueOf(u.name) + " \t\t " + u.result + " \t " + u.time + " \t " + u.score + nl);
        }
        return content.toString();
    }
}
