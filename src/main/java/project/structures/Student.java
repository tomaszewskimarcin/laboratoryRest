package project.structures;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by student on 26.02.2017.
 */
@Entity("students")
@XmlRootElement
public class Student {

    @Id()
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    @XmlTransient
    private ObjectId id;
    @Property("lId")
    private int lId;
    @Property("firstname")
    private String imie;
    @Property("lastname")
    private String nazwisko;
    @Property("birth_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CET")
    private Date dataUrodzenia;

    public Student(){

    }

    public void setDataUrodzenia(Date dataUrodzenia) {

        this.dataUrodzenia = dataUrodzenia;
    }

    public void setNazwisko(String nazwisko) {

        this.nazwisko = nazwisko;
    }

    public void setImie(String imie) {

        this.imie = imie;
    }

    @XmlElement
    @XmlSchemaType(name="date")
    public Date getDataUrodzenia() {

        return dataUrodzenia;
    }

    @XmlElement
    public String getNazwisko() {

        return nazwisko;
    }

    @XmlElement
    public String getImie() {

        return imie;
    }

    @XmlTransient
    public ObjectId getID() {
        return id;
    }


    public void setID(ObjectId id) {

        this.id = id;
    }

    @XmlElement
    public int getlId() {
        return lId;
    }

    public void setlId(int lId) {
        this.lId = lId;
    }
}
