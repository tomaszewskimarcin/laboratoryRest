package project.structures;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by student on 26.02.2017.
 */
@Entity("courses")
@XmlRootElement
public class Course {

    @Id()
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    @XmlTransient
    private ObjectId id;
    @Property("lId")
    private int lId;
    @Property("name")
    private String nazwa;
    @Property("teacher")
    private String teacher;

    public Course(){

    }

    public void setId(ObjectId id){
        this.id = id;
    }

    /*@XmlAttribute
    public int getID(){return id;}*/

    @XmlTransient
    public ObjectId getID(){return id;}

    @XmlElement
    public String getNazwa() {

        return nazwa;
    }

    public void setNazwa(String nazwa) {

        this.nazwa = nazwa;
    }

    @XmlElement
    public int getlId() {
        return lId;
    }

    public void setlId(int lId) {
        this.lId = lId;
    }

    @XmlElement
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
