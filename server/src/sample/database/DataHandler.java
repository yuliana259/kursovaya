package sample.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public abstract class DataHandler{
    public static void Serialize(User user) throws IOException {
        FileOutputStream fileOut = new FileOutputStream("tmp/employee.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(user);
        out.close();
        fileOut.close();
    }
    public static User Deserialize() throws IOException, ClassNotFoundException {
        User user = null;
        FileInputStream fileIn = new FileInputStream("tmp/employee.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        user = (User) in.readObject();
        in.close();
        fileIn.close();
        return user;
    }
    public static String createDocument(String command) throws IOException, ClassNotFoundException {
        User user = (User) Deserialize();
        XWPFDocument docx = new XWPFDocument();
        int count = 0;
        XWPFDocument document = new XWPFDocument();
        if (command.equals("уволить"))
            docx = new XWPFDocument(new FileInputStream("увольнение.docx"));
        else if(command.equals("принять"))
            docx = new XWPFDocument(new FileInputStream("прием.docx"));
        XWPFWordExtractor we = new XWPFWordExtractor(docx);
        String text = we.getText() ;
        if(text.contains("УВОЛИТЬ")){
            text = text.replace("УВОЛИТЬ", "УВОЛИТЬ:\n "+user.getLastName()+" "+user.getFirstName()
            +" "+user.getPatronymic()+" ");
            text = text.replace("выплатить", "Выплатить "+user.getLastName());
            text=text.replace("Заявление", "Заявление "+user.getLastName());
        }
        else if(text.contains("ПРИНЯТЬ"))
        {
            text=text.replace("ПРИНЯТЬ", "ПРИНЯТЬ:\n"+user.getLastName()+" "+
                    user.getFirstName()+" "+user.getPatronymic()+" "+" на должность "+
                    user.getPosition() + " с "+user.getAcceptDay());
            text = text.replace("Заявление ", "Заявление "+user.getLastName()+" ");
        }
        char[] c = text.toCharArray();
        for(int i= 0; i < c.length;i++){

            if(c[i] == '\n'){
                count ++;
            }
        }
        StringTokenizer st = new StringTokenizer(text,"\n");
        XWPFParagraph para = document.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setFontSize(36);
        List<XWPFParagraph> paragraphs = new ArrayList<XWPFParagraph>();
        List<XWPFRun>runs = new ArrayList<XWPFRun>();
        int k = 0;
        for(k=0;k<count+1;k++){
            paragraphs.add(document.createParagraph());
        }
        k=0;
        while(st.hasMoreElements()){
            paragraphs.get(k).setAlignment(ParagraphAlignment.LEFT);
            paragraphs.get(k).setSpacingAfter(0);
            paragraphs.get(k).setSpacingBefore(0);
            run = paragraphs.get(k).createRun();
            run.setText(st.nextElement().toString());
            k++;
        }
        if (command.equals("уволить"))
            document.write(new FileOutputStream("увольнение"+user.getLastName()+".docx"));
        else if(command.equals("принять"))
            document.write(new FileOutputStream("прием"+user.getLastName()+".docx"));
        return "Success form";
}
}
