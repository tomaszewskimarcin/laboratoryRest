import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import project.structures.Course;
import project.structures.Grade;
import project.structures.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by student on 26.02.2017.
 */
public class Model {

    private static Model instance = null;

    private HashMap<ObjectId,Student> students;
    private HashMap<ObjectId, Course> przedmioty;
    private HashMap<ObjectId, Grade> oceny;
    private int indeksStudent = 0;
    private int przedmiotId = 0;
    private int gradeId = 0;
    final Morphia morphia = new Morphia();
    Datastore datastore;

    private Model(){
        students = new HashMap<ObjectId,Student>();
        przedmioty = new HashMap<ObjectId, Course>();
        oceny = new HashMap<ObjectId, Grade>();
        morphia.mapPackage("project.structures");
        datastore = morphia.createDatastore(new MongoClient(), "project");
        datastore.ensureIndexes();
        przykladoweDane();
    }

    private void przykladoweDane(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        Student s = new Student();
        s.setlId(134288);
        Date d = new Date();
        try {
            d = sdf.parse("1993/09/11");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        s.setDataUrodzenia(d);
        s.setImie("Marcin");
        s.setNazwisko("Tomaszewski");
        addStudent(s);

        s = new Student();
        s.setlId(134289);
        d = new Date();
        try {
            d = sdf.parse("1960/01/01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        s.setDataUrodzenia(d);
        s.setImie("Marcin");
        s.setNazwisko("Drugi");
        addStudent(s);

        s = new Student();
        s.setlId(11);
        s.setDataUrodzenia(new Date());
        s.setImie("Imie");
        s.setNazwisko("Nazwa");
        addStudent(s);

        Course p = new Course();
        p.setId(p.getID());
        p.setNazwa("Wytwarzanie Systemów Inteernetowych");
        p.setTeacher("Tomasz Pawlak");
        addPrzedmiot(p);

        p = new Course();
        p.setId(p.getID());
        p.setNazwa("Multimedialne Interfejsy Użytkownika");
        p.setTeacher("Bartłomiej Prędki");
        addPrzedmiot(p);

        Grade gr = new Grade();
        gr.setDataWystawienia(new Date());
        gr.setIdPrzedmiot(0);
        gr.setIdStudent(1);
        gr.setStopien(5.0);
        addGrade(gr,gr.getIdPrzedmiot());

        gr = new Grade();
        gr.setDataWystawienia(new Date());
        gr.setIdPrzedmiot(1);
        gr.setIdStudent(1);
        gr.setStopien(2.0);
        addGrade(gr,gr.getIdPrzedmiot());

        gr = new Grade();
        gr.setDataWystawienia(new Date());
        gr.setIdPrzedmiot(0);
        gr.setIdStudent(2);
        gr.setStopien(5.0);
        addGrade(gr,gr.getIdPrzedmiot());

    }

    public static Model getInstance(){
        if(instance == null){
            instance = new Model();
            return instance;
        }else{
            return instance;
        }
    }

    public HashMap<ObjectId, Grade> getGrades() {
        return oceny;
    }

    public HashMap<ObjectId, Course> getCourses() {

        return przedmioty;
    }

    public HashMap<ObjectId,Student> getStudents() {
        return students;
    }

    public boolean addStudent(Student stu){
        stu.setlId(indeksStudent);
        datastore.save(stu);
        students.put(stu.getID(),stu);
        indeksStudent++;
        return true;
    }

    public boolean addPrzedmiot(Course p){
        p.setId(p.getID());
        p.setlId(przedmiotId);
        datastore.save(p);
        przedmioty.put(p.getID(),p);
        przedmiotId++;
        return true;
    }

    public boolean addGrade(Grade gr, Integer przedmiotId){
        gr.setId(gr.getId());
        gr.setlId(gradeId);
        if(przedmiotId!=null){gr.setIdPrzedmiot(przedmiotId);}
        datastore.save(gr);
        oceny.put(gr.getId(),gr);
        gradeId++;
        return true;
    }

}
